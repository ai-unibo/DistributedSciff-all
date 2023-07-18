// scalastyle:off println
package spark.examples

import java.net._

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import spark.examples.models.{Kumar_4SubModelVariables, ModelVariables}


object AlgoPartitionedModel
   extends Runner{

 /* var rep : Int =1
  var sv:ModelVariables =_
  var nTraces:Int=1
  var DEBUG: Boolean = false
  var subtraceCompleteCheck : Boolean = false
  var path_to_AlpBPM: String = ""
  var path_to_swipl: String = ""*/

  override def run(lines: ReceiverInputDStream[String], rep : Int, sv:ModelVariables, nTraces:Int=1,
                   DEBUG: Boolean = false, subtraceCompleteCheck : Boolean = false,
                   path_to_AlpBPM: String,
                   path_to_swipl: String) {

    /*this.rep=rep
    this.sv = sv
    this.nTraces = nTraces
    this.DEBUG = DEBUG
    this.subtraceCompleteCheck = subtraceCompleteCheck
    this.path_to_AlpBPM = path_to_AlpBPM
    this.path_to_swipl = path_to_swipl*/

    def getId(event: String) : String = {
      event.split("t\\(")(1).split(",")(0)
    }

    def getH(event: String, traceId:Int) : String = {
      if (event.contains(sv.lastEvent))
        sv.lastEvent
      else
        "h(event("+traceId+","+event.split("event\\(")(1).dropRight(1)
    }

    def parseKV(event : String) : Array[((Int, String),(Int,String))] = {
      val traceId :Int= getId(event).toInt
      val groupId :Int = traceId/nTraces

      val h : String = getH(event, traceId)
      val sms : Array[Int] = getSubmodels(h)
      if (h!=sv.lastEvent || ( (traceId+1)/nTraces != groupId ))
        sms.map(sm =>((sm,groupId.toString),(sm,h)))
      else
        Array(((-1,""),(-1,"")))
    }

    def launchSciffClient(values: Seq[(Int,String)], r: ReturnObject) : Option[ReturnObject] = {
      val sciffClient : SciffClient = new SciffClient(r.serverName, r.port, values.map(_._2).toArray,DEBUG)
      r.result = Some(sciffClient.startClient()) //"Yes" "No" "Ni"
      Some(r)
    }

    def getSubmodels(h: String) : Array[Int] = {
      val activity:String = getActivity(h)
      //sv.modelsPerEvent.getOrElse(activity,Array(-1))
      sv.modelsPerEvent.getOrElse(activity,"-1").split(",").map(_.toInt).distinct
    }

    def getActivity(h: String) : String = {
      if (h == sv.lastEvent)
        return sv.lastEvent
      //h.split("h\\(event\\(")(1).split(",")(0).split("_")(0) //without traceid inside the h atom
      h.split("h\\(event\\(")(1).split(",")(1).split("_")(0)   //with traceid inside the h atom
    }

    def launchSciffServer(values: Seq[(Int,String)]) : Option[ReturnObject] = {
      if (values.nonEmpty ) {
        val sm = values.head._1
        //if (sm == -1) System.err.println("SM=-1 PER "+values.head._2)
        if(sm!=sv.submodelWithLastEvent) { //ridondante:launchSciffServer non dovrebbe mai esser chiamata con sm==submodelWithLastEvent
          val sciffLauncher: SciffLauncher = new SciffLauncher(sv.submodels(sm), sv.observabilities(sm),
            sv.durationConstrs(sm), sv.interTaskConstrs(sm), sv.options, DEBUG,path_to_AlpBPM,path_to_swipl)
          val result: Option[String] = {
            if (subtraceCompleteCheck )
              Some(sciffLauncher.launchPartitionedModel(values.map(_._2).toArray,sm, sv.subtraceCompleteIf(sm).clone(), sv.lastEvent))
            else
              Some(sciffLauncher.launchPartitionedModel(values.map(_._2).toArray, sm , Array(), sv.lastEvent))
          }
          if (result == Some("Ni")) {
            // || result.isEmpty) { //non dovrebbe mai essere null
            val hostname: String = InetAddress.getLocalHost.getHostName
            sciffLauncher.launchTheServer('H')
            Some(new ReturnObject(hostname, sciffLauncher.getPort, result))
          }else
            Some(new ReturnObject("", -1, result)) //"No" || "Yes"
        }else
          Some(new ReturnObject("", -1, Some("Yes")))// Some("Ending")))
      }else
        None
    }

    def updateFunc(values: Seq[(Int,String)], state: Option[ReturnObject]) : Option[ReturnObject] = {
      if (values.nonEmpty && values.head._1 == sv.submodelWithLastEvent)
        Some(new ReturnObject("", -1, Some("Yes"))) // Some("Ending")))
      else {
        state match {
          case None =>{
            if (values.size == 1 && values.head._2 == sv.lastEvent)
              None  //evito di lanciare la sciff se ho ricevuto solo lastEvent per questo sottomodello
            else
              launchSciffServer(values)
          }
          case Some(a: ReturnObject) =>
            a.result match {
              case Some("Ni") => launchSciffClient(values, a)
              case default =>  {
                //saveUserSession(state)
                Some(a)
              } //"Yes" or "No", caso None non dovrebbe mai accadere
              // i values arrivati dopo che si è già prodotto un result Yes/No vengono ignorati
            }
        }
      }
    }
    //ottengo coppia (k,v) =>( traceid, (sm,"Ni"/"No"/"Yes") )
    // oppure per end_w (k,v) =>( traceid, (submodelWithLastEvent,"Yes") )      //"Ending") )
    def updateAndFunc(values: Seq[(Int,Option[String])], state: Option[String]) : Option[String] = {
      state match {
        case Some("No") => Some("No")
        case default =>{  //Yes, Ni // None non dovrebbe esserci
          if (values.map(_._2).contains(Some("No")) )  Some("No")
          //suppongo che alla ricezione di submodelWithLastEvent tra i valori, tutti i Ni sospesi si siano tramutati in Yes
          else if (values.map(_._1).contains(sv.submodelWithLastEvent) ){
            var allYes:Boolean = true
            values.foreach(x => allYes = (allYes && x._2==Some("Yes")) )
            if (allYes) Some("Yes")
            else Some("Ni")
          }
          else  {
            //if (state==Some("Yes"))
            //  System.err.println("Converting a Yes result back to Ni. This should not be happening...")
            Some("Ni")
          }
        }
      }
    }


    /**************** EXECUTION STARTS HERE ****************************/

    val eventDstream = lines
      .flatMap(_.split("\\."))
      .filter(_.nonEmpty)
      .flatMap(parseKV)
      .filter(!_._1._1.equals(-1))

    if (DEBUG) {
      eventDstream.foreachRDD(rdd => {
        println("\n+++++ Some after flatMap: " + rdd.count())
        rdd.collect().foreach(x => println("Some parsed Trace [(" + x._1._1.toString + "," + x._1._2 + "),(" + x._2 + ")]"))
      })
    }

    val reduceStream = {
      rep match {
        case 0 => eventDstream
          .updateStateByKey(updateFunc _)
        case _=> eventDstream
          .updateStateByKey(updateFunc _,rep)
      }}
    //ottengo coppie (k,v) =>( (sm,traceid), ReturnObject )
    if (DEBUG) {
      reduceStream.foreachRDD(rdd => {
        System.out.println("\n+++++ Some results after reduce: " + rdd.count())
        rdd.collect().foreach(x => System.out.println("Some Trace [" + x._1 + "," + x._2 + "]"))
      })
    }

    val reduceStream2 = {
      rep match {
        case 0 => reduceStream
          .map(el => (el._1._2, (el._1._1, el._2.result)))
          .updateStateByKey(updateAndFunc _)
        case _=> reduceStream
          .map(el => (el._1._2, (el._1._1, el._2.result)))
          .updateStateByKey(updateAndFunc _,rep)
      }}

    // ottengo coppia (k,v) =>( traceid, (sm,"Ni"/"No"/"Yes") )
    // oppure per end_w (k,v) =>( traceid, (submodelWithLastEvent,"Ending") )
    if (DEBUG) {
      reduceStream2.foreachRDD(rdd => {
        System.out.println("\n+++++ Some results after AND: " + rdd.count())
        rdd.collect().foreach(x => System.out.println("Some Trace after AND [" + x._1 + "," + x._2 + "]"))
      })
    }else{
      reduceStream2.print(10)
    }

    /*val outRDDs = lines.flatMap(_.split("\\.")).filter(_.nonEmpty).flatMap(parseKV)
      .updateStateByKey(updateFunc _,Integer.parseInt(args(3)))
      .map(el => (el._1._2, (el._1._1, el._2.result))).updateStateByKey(updateAndFunc _,Integer.parseInt(args(3)))
    outRDDs.print(10)
    outRDDs.foreachRDD(rdd => {
      System.out.println("\nSome results after AND: " + rdd.count())
      rdd.collect().foreach(x => System.out.println("Some Trace after AND [" + x._1 + "," + x._2 + "]"))
    })*/

  }




}
// scalastyle:on println
