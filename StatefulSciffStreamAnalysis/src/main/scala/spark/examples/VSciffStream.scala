// scalastyle:off println
package spark.examples

import java.net._

import org.apache.spark.SparkConf
import org.apache.spark.streaming._


object VSciffStream {


  val sv:SubmodelVariables = new SubmodelVariables()

  //*******************************************


  var DEBUG: Boolean = false

  /*  class ReturnObject(var serverName : String,
                       var port : Int,
                       var result : Option[String]) extends java.io.Serializable{}  //values in "Yes","No","Ni"
  */

  def main(args: Array[String]) {
    if (args.length < 4) {
      System.err.println("Usage: VSciffStream <hostname> <port> <milliseconds-batchsize> <repartitions> [debug]")
      System.exit(1)
    }
    if (args.length > 4 && args(4)=="debug") DEBUG = true

    val sparkConf = new SparkConf().setAppName("VSciffStream")
    // Create the context with a args(2) millisecond batch size
    val ssc = new StreamingContext(sparkConf, Milliseconds(args(2).toInt)) //, StorageLevel.MEMORY_ONLY_SER)
    ssc.checkpoint("./tmp/")
    ssc.addStreamingListener(new MyListener)

    val rep = Integer.parseInt(args(3))


    val lines = ssc.socketTextStream(args(0), args(1).toInt )
    //spacchettando per linee trova sempre end_of_the_world per ogni traccia nel batch e non lancia mai il client,
    // però se metto un invio a metà di una linea allora lo lancia (funzionante)
    if(rep!=0){
      lines.repartition(rep)
    }




    val eventDstream = lines
      .flatMap(_.split("\\."))
      .filter(_.nonEmpty)
      //  .flatMap(parseKV)
      .map{x => ( getId(x), getH(x) )}

    if (DEBUG) {
      eventDstream.foreachRDD(rdd => {
        println("\n+++++ Some after map: " + rdd.count()) //println("\n+++++ Some after flatMap: " + rdd.count())
        rdd.collect().foreach(x => println("Some mapped Trace [(" + x._1 + "," + x._2 + ")]"))
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

    ssc.start()
    ssc.awaitTermination()
  }
  def launchSciffServer(values: Seq[String]) : Option[ReturnObject] = {
    if (values.nonEmpty ) {
      /*val sm = values.head._1
      if(sm!=sv.submodelWithLastEvent) { //ridondante:launchSciffServer non dovrebbe mai esser chiamata con sm==submodelWithLastEvent
      */
      val sciffLauncher: SciffLauncher = new SciffLauncher(sv.model, sv.observability,
        sv.durationConstr, sv.interTaskConstr, sv.options, DEBUG)
      val result: Option[String] = Some(sciffLauncher.launch(values.toArray, sv.lastEvent))
      if (result == Some("Ni")) {
        // || result.isEmpty) { //non dovrebbe mai essere null
        val hostname: String = InetAddress.getLocalHost.getHostName
        sciffLauncher.launchTheServer('V')
        Some(new ReturnObject(hostname, sciffLauncher.getPort, result))
      }else
        Some(new ReturnObject("", -1, result)) //"No" || "Yes"
      /*}else
        Some(new ReturnObject("", -1, Some("Yes")))// Some("Ending")))
      */
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

  def launchSciffClient(values: Seq[String], r: ReturnObject) : Option[ReturnObject] = {
    val sciffClient : SciffClient = new SciffClient(r.serverName, r.port, values.toArray,DEBUG)
    r.result = Some(sciffClient.startClient()) //"Yes" "No" "Ni"
    Some(r)
  }


  /*
    def getSubmodels(h: String) : Array[Int] = {
      val activity:String = getActivity(h)
      sv.modelsPerEvent.getOrElse(activity,Array(-1))
    }

    def getActivity(h: String) : String = {
      if (h == sv.lastEvent)
        return sv.lastEvent
      h.split("h\\(event\\(")(1).split(",")(0)
    }
  */
  def updateFunc(values: Seq[String], state: Option[ReturnObject]) : Option[ReturnObject] = {
    /*if (values.nonEmpty && values.head._1 == sv.submodelWithLastEvent)
      Some(new ReturnObject("", -1, Some("Yes"))) // Some("Ending")))
    else {*/
    state match {
      case None =>{
        if (values.size == 1 && values.head == sv.lastEvent)
          None  //evito di lanciare la sciff se ho ricevuto solo lastEvent per questo modello - utile probabilmente solo in H
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
    //}
  }
  /*def parseKV(event : String) : Array[((Int, String),(Int,String))] = {
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
  }*/


}
// scalastyle:on println
