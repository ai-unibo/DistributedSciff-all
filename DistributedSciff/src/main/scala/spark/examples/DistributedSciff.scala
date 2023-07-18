// scalastyle:off println
package spark.examples

import java.net._

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._
import spark.examples.ModelID.ModelID
//import spark.examples.Partitioning.Partitioning
import spark.examples.models.{Kumar_4SubModelVariables, ModelVariables}


object DistributedSciff {



  def main(args: Array[String]) {
    if (args.length < 8) {
      System.err.println("Usage: DistributedSciff <hostname> <port> <AlpBPM_path> <swipl_path> <batchms> <repartitions>" +
                                                " <subtraceCompleteCheck[true/false]> <model_id> <nTraces> [debug/run]")
      System.exit(1)
    }
    /******** LETTURA PARAMETRI *********/
    val hostname = args(0)
    val port : Int= args(1).toInt
    val AlpBPM_path :String = args(2)
    val swipl_path :String = args(3)
    val batchms : Int= args(4).toInt
    val rep : Int = args(5).toInt
    val subtraceCompleteCheck : Boolean = args(6).toBoolean
    val model_id : ModelID = ModelID.withName(args(7))
    val nTraces : Int = args(8).toInt
    val DEBUG: Boolean = if (args(9)=="debug") true else false

    val sv: ModelVariables = ModelID.getVariables(model_id)

    val sparkConf = new SparkConf().setAppName(args(7)+"_"+args(8))
    val ssc = new StreamingContext(sparkConf, Milliseconds(batchms)) //, StorageLevel.MEMORY_ONLY_SER)
    ssc.checkpoint("./tmp/")
    ssc.addStreamingListener(new MyListener)


    val lines = ssc.socketTextStream(hostname, port )
    //spacchettando per linee trova sempre end_of_the_world per ogni traccia nel batch e non lancia mai il client,
    // però se metto un invio a metà di una linea allora lo lancia (funzionante)
    if(rep!=0){
      lines.repartition(rep)
    }

    var count=0
    if (DEBUG) {
      lines.foreachRDD(r => {
        println("++++ RDD "+count +": "+ r.getNumPartitions +" partitions")
        /*val i = 0
        r.foreachPartition(p => {
          println("++++ Partition " + i + ":")
          p.foreach(s => println("++++ El: " + s))
        })*/
        count=count+1
      })
    }

    var runner : Runner =null

    if (ModelID.isModelPartitioned(model_id))
      AlgoPartitionedModel.run(lines, rep,sv,nTraces,DEBUG,subtraceCompleteCheck,ModelID.getInputFormat(model_id),AlpBPM_path,swipl_path )
    else
      AlgoCompleteModel.run(lines,rep,sv,nTraces,DEBUG,subtraceCompleteCheck,ModelID.getInputFormat(model_id),AlpBPM_path,swipl_path )

    /*partitioning_id match{
      case Partitioning.PL =>
        runner = new AlgoCompleteModel(rep,sv,nTraces,DEBUG)
      case Partitioning.CLCM => runner = new AlgoPartitionedModel(rep,sv,nTraces,DEBUG,subtraceCompleteCheck)
    }*/

    //runner.run(lines)

    ssc.start()
    ssc.awaitTermination()

/*
    val eventDstream = lines.flatMap(_.split("\\.")).filter(_.nonEmpty).flatMap(parseKV)

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

    ssc.start()
    ssc.awaitTermination()
    */
  }


/*
  def launchSciffServer(values: Seq[(Int,String)]) : Option[ReturnObject] = {
    if (values.nonEmpty ) {
      val sm = values.head._1
      //if (sm == -1) System.err.println("SM=-1 PER "+values.head._2)
      if(sm!=sv.submodelWithLastEvent) { //ridondante:launchSciffServer non dovrebbe mai esser chiamata con sm==submodelWithLastEvent
      val sciffLauncher: SciffLauncher = new SciffLauncher(sv.submodels(sm), sv.observabilities(sm),
        sv.durationConstrs(sm), sv.interTaskConstrs(sm), sv.options, DEBUG)
        val result: Option[String] = {
          if (subtraceCompleteCheck )
            Some(sciffLauncher.launch(values.map(_._2).toArray,sm, sv.subtraceCompleteIf(sm).clone(), sv.lastEvent))
          else
            Some(sciffLauncher.launch(values.map(_._2).toArray, sm , Array(), sv.lastEvent))
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


  def getId(event: String) : String = {
    event.split("t\\(")(1).split(",")(0)
  }
  def getH(event: String) : String = {
    if (event.contains(sv.lastEvent))
      sv.lastEvent
    else
      "h(" + event.split("h\\(")(1).dropRight(1)
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
    h.split("h\\(event\\(")(1).split(",")(0).split("_")(0)
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
  def parseKV(event : String) : Array[((Int, String),(Int,String))] = {
    val traceId = getId(event)
    val h : String = getH(event)
    val sms : Array[Int] = getSubmodels(h)
    sms.map(sm =>((sm,traceId),(sm,h)))
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

*/
}
// scalastyle:on println
