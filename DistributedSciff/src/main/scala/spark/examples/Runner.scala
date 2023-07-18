package spark.examples

import org.apache.spark.streaming.dstream.ReceiverInputDStream
import spark.examples.InputFormat.InputFormat
import spark.examples.models.ModelVariables

/**
  * Created by utente on 29/09/17.
  */
trait Runner extends Serializable {
  def run(lines: ReceiverInputDStream[String], rep : Int, sv:ModelVariables, nTraces:Int=1,
          DEBUG: Boolean = false, subtraceCompleteCheck : Boolean = false,
          inputFormat : InputFormat,
          path_to_AlpBPM: String,
          path_to_swipl: String)
}