package spark.examples

import spark.examples.InputFormat.InputFormat
import spark.examples.models._

/**
  * Created by utente on 27/09/17.
  */

object ModelID extends Enumeration {
  type ModelID = Value
  val KUMAR,  //FRACTURE TREATMENT
  KUMAR_4SM,
  KUMAR_MaxSM,
  EV810,
  EV810_15SM,
  EV810_MaxSM,    //810 SUBSEQUENT EVENTS - 810 SUBMODELS
  BPI,             //BPI2012 MODEL
  BPI_6SM,
  BPI_MaxSM
  = Value

  /*type InputFormat = Value
  val
  H_COMPLETE,
  T
  = Value*/


  def getVariables(modelID: ModelID) : ModelVariables = {
    modelID match {
      case ModelID.KUMAR => new Kumar_ModelVariables()
      case ModelID.KUMAR_4SM => new Kumar_4SubModelVariables()
      case ModelID.KUMAR_MaxSM => new Kumar_MaxSubModelVariables()
      case ModelID.EV810 => new Ev810_ModelVariables()
      case ModelID.EV810_15SM => new Ev810_15SubModelVariables()
      case ModelID.EV810_MaxSM => new Ev810_810SubModelVariables()
      case ModelID.BPI => new BPI_ModelVariables
      case ModelID.BPI_6SM => new BPI_6SubModelVariables
      case ModelID.BPI_MaxSM =>  new BPI_MaxSubModelVariables
//TODO
    }
  }

  def isModelPartitioned(modelID: ModelID) : Boolean = {
    modelID match {
      case ModelID.KUMAR => false
      case ModelID.KUMAR_4SM => true
      case ModelID.KUMAR_MaxSM => true
      case ModelID.EV810 => false
      case ModelID.EV810_15SM => true
      case ModelID.EV810_MaxSM => true
      case ModelID.BPI => false
      case ModelID.BPI_6SM => true
      case ModelID.BPI_MaxSM => true
    }
  }

  def getInputFormat(modelID: ModelID) : spark.examples.InputFormat.InputFormat = {
    modelID match {
      case ModelID.KUMAR => InputFormat.T
      case ModelID.KUMAR_4SM => InputFormat.T
      case ModelID.KUMAR_MaxSM => InputFormat.T
      case ModelID.EV810 => InputFormat.T
      case ModelID.EV810_15SM => InputFormat.T
      case ModelID.EV810_MaxSM => InputFormat.T
      case ModelID.BPI => InputFormat.H_COMPLETE
      case ModelID.BPI_6SM => InputFormat.H_COMPLETE
      case ModelID.BPI_MaxSM => InputFormat.H_COMPLETE
    }
  }

}

