package spark.examples

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
  RL,             //BPI2012 MODEL
  RL_4SM,         //BPI2012 MODEL
  RL_MaxSM        //BPI2012 MODEL
  = Value


  def getVariables(modelID: ModelID) : ModelVariables = {
    modelID match {
      case ModelID.KUMAR => new Kumar_ModelVariables()
      case ModelID.KUMAR_4SM => new Kumar_4SubModelVariables()
      case ModelID.KUMAR_MaxSM => new Kumar_CSubModelVariables()
      case ModelID.EV810 => new Ev810_ModelVariables()
      case ModelID.EV810_15SM => new Ev810_15SubModelVariables()
      case ModelID.EV810_MaxSM => new Ev810_810SubModelVariables()
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
      case ModelID.RL => false
      case ModelID.RL_4SM => true
      case ModelID.RL_MaxSM => true
    }
  }

}

