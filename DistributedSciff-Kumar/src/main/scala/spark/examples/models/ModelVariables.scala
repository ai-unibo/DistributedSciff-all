package spark.examples.models

import java.util

/**
  * Created by utente on 28/09/17.
  */
abstract class ModelVariables extends Serializable{
  val lastEvent : String = "end_of_the_world"
  val options : String = "[ trace_max_length(43), generate_current_time(no), activities_have_start_and_end, double_chained_translation ]"

  val model : String = ""
  val observability : String = ""
  val durationConstr : String = ""
  val interTaskConstr : String = ""

  val submodels : Array[String] = Array("")
  val observabilities : Array[String] = Array("")
  val durationConstrs : Array[String] = Array("")
  val interTaskConstrs : Array[String] = Array("")
  val subtraceCompleteIf : Array[Array[util.ArrayList[String]]] = Array(Array(new util.ArrayList[String](util.Arrays.asList(""))))
  val modelsPerEvent : Map[String,String] = Map(""->"")
  val submodelWithLastEvent :Int = -1

}
