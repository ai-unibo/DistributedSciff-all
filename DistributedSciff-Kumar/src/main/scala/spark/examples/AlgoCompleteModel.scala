// scalastyle:off println
package spark.examples

import java.net._

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.ReceiverInputDStream
//import spark.examples.AlgoCompleteModel.{DEBUG, path_to_AlpBPM, path_to_swipl, sv}
import spark.examples.models.ModelVariables


object AlgoCompleteModel
  extends Runner{

  /*var rep : Int =1
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

    def parseKV(event : String) : (String, String) = {
      val traceId :Int= getId(event).toInt
      val groupId :Int = traceId/nTraces

      val h : String = getH(event, traceId)
      if (h!=sv.lastEvent || ( (traceId+1)/nTraces != groupId ))
        (groupId.toString,h)
      else
        ("","")
    }

    def launchSciffServer(values: Seq[String]) : Option[ReturnObject] = {
      if (values.nonEmpty ) {
        val sciffLauncher: SciffLauncher = new SciffLauncher(sv.model, sv.observability,
          sv.durationConstr, sv.interTaskConstr, sv.options, DEBUG,path_to_AlpBPM,path_to_swipl)
        val result: Option[String] = Some(sciffLauncher.launchCompleteModel(values.toArray, sv.lastEvent))
        if (result == Some("Ni")) {
          // || result.isEmpty) { //non dovrebbe mai essere null
          val hostname: String = InetAddress.getLocalHost.getHostName
          sciffLauncher.launchTheServer('V')
          Some(new ReturnObject(hostname, sciffLauncher.getPort, result))
        }else
          Some(new ReturnObject("", -1, result)) //"No" || "Yes"
      }else
        None
    }

    def launchSciffClient(values: Seq[String], r: ReturnObject) : Option[ReturnObject] = {
      val sciffClient : SciffClient = new SciffClient(r.serverName, r.port, values.toArray,DEBUG)
      r.result = Some(sciffClient.startClient()) //"Yes" "No" "Ni"
      Some(r)
    }

    def updateFunc(values: Seq[String], state: Option[ReturnObject]) : Option[ReturnObject] = {
      /*if (values.nonEmpty && values.head._1 == sv.submodelWithLastEvent)
        Some(new ReturnObject("", -1, Some("Yes"))) // Some("Ending")))
      else {*/
      state match {
        case None =>{
          if (values.size == 1 && (values.head == sv.lastEvent || values.head =="") )
            None  //evito di lanciare la sciff se ho ricevuto solo lastEvent per questo modello - utile probabilmente solo in H
          else
            launchSciffServer(values)
        }
        case Some(a: ReturnObject) =>
          a.result match {
            case Some("Ni") => launchSciffClient(values, a)
            case default =>  {
              Some(a)
            } //"Yes" or "No", caso None non dovrebbe mai accadere
            // i values arrivati dopo che si è già prodotto un result Yes/No vengono ignorati
          }
      }
      //}
    }

    /**************** EXECUTION STARTS HERE ****************************/

    val eventDstream = lines
      .flatMap(_.split("\\."))
      .filter(_.nonEmpty)
      //.map{x => ( getId(x) , getH(x) )}
      .map{parseKV(_)}
      .filter(_._1.nonEmpty)

    if (DEBUG) {
      eventDstream.foreachRDD(rdd => {
        println("\n+++++ Some after map: " + rdd.count()) //println("\n+++++ Some after flatMap: " + rdd.count())
        rdd.collect().foreach(x => {
          println("Some mapped Trace [(" + x._1 + "," + x._2 + ")]")
        })
      })
    }


    val reduceStream = {
      rep match {
        case 0 => eventDstream
          .updateStateByKey(updateFunc _)
        case _=> eventDstream
          .updateStateByKey(updateFunc _,rep)
      }}
    //ottengo coppie (k,v) =>( traceid, ReturnObject )
    if (DEBUG) {
      reduceStream.foreachRDD(rdd => {
        System.out.println("\n+++++ Some results after reduce: " + rdd.count())
        rdd.collect().foreach(x => System.out.println("Some Trace [" + x._1 + "," + x._2 + "]"))
      })
    }else
      reduceStream.print(10)

  }



}
// scalastyle:on println
