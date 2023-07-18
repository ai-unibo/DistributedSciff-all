package spark.examples

/**
  * Created by utente on 25/05/17.
  */
class ReturnObject(var serverName : String,
                   var port : Int,
                   var result : Option[String]) extends java.io.Serializable{
  override def toString: String = {
    return serverName+":"+port+" "+result.getOrElse("")
  }
}
