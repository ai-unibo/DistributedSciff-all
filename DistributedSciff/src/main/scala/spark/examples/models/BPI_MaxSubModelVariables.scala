package spark.examples.models

import java.util

/**
  * Created by utente on 13/04/17.
  */
class BPI_MaxSubModelVariables extends ModelVariables{
  //override val lastEvent : String = "end_of_the_world"
  //val options : String = "[trace_max_length(43)]"
  override val options : String = "[ trace_max_length(43), generate_current_time(no), activities_have_start_and_end, double_chained_translation, events_contain_traceId(yes)  ]"
  override val submodels = Array(
    "H(event(TraceId, a_partlysubmitted,complete,_R1), T1) ---> EN(event(TraceId, a_partlysubmitted,complete,_R2), T2) /\\\\ T2<>T1.",      //0
    "H(event(TraceId, a_submitted,complete,_R1), T1) ---> EN(event(TraceId, a_submitted,complete,_R2), T2) /\\\\ T2<>T1.",                  //1
    "H(event(TraceId, a_submitted,complete,_R1), T1) ---> E(event(TraceId, a_partlysubmitted,complete,_R2), T2) /\\\\ T2>T1"+
    "/\\\\ EN(event(TraceId, _act, _lifecyle, _R3), Tin) /\\\\ Tin>T1 /\\\\ Tin<T2.",                                                       //2
    "H(event(TraceId, a_partlysubmitted,complete,_R2), T2) ---> E(event(TraceId, a_submitted,complete,_R1), T1) /\\\\ T2>T1"+
    "/\\\\ EN(event(TraceId, _act, _lifecyle, _R3), Tin) /\\\\ Tin>T1 /\\\\ Tin<T2.",                                                       //3
    "true --->   E(event(TraceId, a_submitted,complete,_R1), T1)"+
    "/\\\\ E(event(TraceId, a_partlysubmitted,complete,_R2), T2) /\\\\ T2>T1"+
    "/\\\\ EN(event(TraceId, _act, _lifecyle, _Rbefore), Tbefore) /\\\\ Tbefore<T1"+
    "/\\\\ EN(event(TraceId, _act, _lifecyle, _Rin), Tin) /\\\\ Tin>T1 /\\\\ Tin<T2.",                                                      //4
    "H(event(TraceId, w_afhandelen_leads, schedule, _R1), T1) ---> E(event(TraceId, w_afhandelen_leads, start,_R2), T2) /\\\\ T2>T1"+
    "/\\\\ EN(event(TraceId, w_afhandelen_leads, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin1<T2"+
    "/\\\\ EN(event(TraceId, w_afhandelen_leads, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
    "/\\\\ EN(event(TraceId, w_afhandelen_leads, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2.",                                     //5
    "H(event(TraceId, w_afhandelen_leads, start, _R1), T1) ---> E(event(TraceId, w_afhandelen_leads, complete,_R2), T2) /\\\\ T2>T1"+
    "/\\\\ EN(event(TraceId, w_afhandelen_leads, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin1<T2"+
    "/\\\\ EN(event(TraceId, w_afhandelen_leads, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
    "/\\\\ EN(event(TraceId, w_afhandelen_leads, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2.",                                     //6
    "H(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) ---> E(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) /\\\\ T2>T1.", //7
    "H(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) ---> E(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) /\\\\ T2>T1.", //8
    "H(event(TraceId, w_beoordelen_fraude, schedule, _R1), T1) ---> E(event(TraceId, w_beoordelen_fraude, start,_R2), T2) /\\\\ T2>T1"+
    "/\\\\ EN(event(TraceId, w_beoordelen_fraude, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin<T2"+
    "/\\\\ EN(event(TraceId, w_beoordelen_fraude, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
    "/\\\\ EN(event(TraceId, w_beoordelen_fraude, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2.",                                    //9
    "H(event(TraceId, w_beoordelen_fraude, start, _R1), T1) ---> E(event(TraceId, w_beoordelen_fraude, complete,_R2), T2) /\\\\ T2>T1"+
    "/\\\\ EN(event(TraceId, w_beoordelen_fraude, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin<T2"+
    "/\\\\ EN(event(TraceId, w_beoordelen_fraude, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
    "/\\\\ EN(event(TraceId, w_beoordelen_fraude, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2.",                                    //10
    "H(event(TraceId, o_selected, complete, _R1), T1) ---> E(event(TraceId, o_created, complete, _R2), T2) /\\\\ T2>T1.",                   //11
    "H(event(TraceId, o_created, complete, _R1), T1) ---> E(event(TraceId, o_sent, complete, _R2), T2) /\\\\ T2>T1.",                       //12
    "H(event(TraceId, o_sent, complete, _R2), T2) ---> E(event(TraceId, o_created, complete, _R1), T1) /\\\\ T2>T1.",                       //13
    "H(event(TraceId, o_created, complete, _R2), T2) ---> E(event(TraceId, o_selected, complete, _R1), T1) /\\\\ T2>T1.",                   //14
    "H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_activated, complete, _R), T).",                             //15
    "H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_registered, complete, _R), T).",                            //16
    "H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_approved, complete, _R), T).",                              //17
    "H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_declined, complete, _R), T).",                              //18
    "H(event(TraceId, w_beoordelen_fraude, schedule, _R1), T1) ---> EN(event(TraceId, w_wijzigen_contractgegevens, schedule, _R), T).",     //19
    //"H(event(TraceId, a_accepted, _LifeCycle, _R1), T1) ---> EN(event(TraceId, a_declined, _LifeCycle, _R), T).",                           //20
    "H(event(TraceId, a_submitted, complete, _R1), T1) ---> E(event(TraceId, a_partlysubmitted, complete, _R2), T2) /\\\\ T2>T1 /\\\\ T2<=T1+22000.",  //20
    "H(event(TraceId, a_partlysubmitted, complete, _R2), T2) ---> E(event(TraceId, a_submitted, complete, _R1), T1) /\\\\ T2>T1 /\\\\ T2<=T1+22000.",  //21
    "H(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) ---> E(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) /\\\\ T2>T1+22000 /\\\\ T2<=T1+239368000.", //22
    //"H(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) ---> E(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) /\\\\ T2>T1+22000 /\\\\ T2<=T1+239368000.", //24
    "H(event(TraceId, a_submitted, complete, R), T1) ---> E(event(TraceId, a_partlysubmitted, complete, R), T2).",                          //23
    "H(event(TraceId, a_partlysubmitted, complete, R), T1) ---> E(event(TraceId, a_submitted, complete, R), T2).",                          //24
    "H(event(TraceId, a_accepted, _LifeCycle, R), T1) ---> E(event(TraceId, a_finalized, _LifeCycle, R), T2).",                             //25
    "H(event(TraceId, a_finalized, _LifeCycle, R), T1) ---> E(event(TraceId, a_accepted, _LifeCycle, R), T2).",                             //26
    "H(event(TraceId, a_submitted, _LifeCycle, R), T1) ---> EN(event(TraceId, a_finalized, _LifeCycle, R), T2).",                           //27
    "H(event(TraceId, a_finalized, _LifeCycle, R), T1) ---> EN(event(TraceId, a_submitted, _LifeCycle, R), T2)."                            //28
  )

  override val subtraceCompleteIf = Array(
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //0
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //1
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //2
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //3
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //4
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //5
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //6
    Array(new util.ArrayList[String](util.Arrays.asList("w_completeren_aanvraag,schedule","w_completeren_aanvraag,complete"))),  //7
    Array(new util.ArrayList[String](util.Arrays.asList("w_completeren_aanvraag,schedule","w_completeren_aanvraag,complete"))),  //8
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //9
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //10
    Array(new util.ArrayList[String](util.Arrays.asList("o_selected,complete","o_created,complete"))),            //11
    Array(new util.ArrayList[String](util.Arrays.asList("o_sent,complete","o_created,complete"))),                //12
    Array(new util.ArrayList[String](util.Arrays.asList("o_sent,complete","o_created,complete"))),                //13
    Array(new util.ArrayList[String](util.Arrays.asList("o_selected,complete","o_created,complete"))),            //14
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //15
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //16
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //17
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //18
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //19
    //Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //20
    Array(new util.ArrayList[String](util.Arrays.asList("a_submitted,complete","a_partlysubmitted,complete"))),   //20
    Array(new util.ArrayList[String](util.Arrays.asList("a_submitted,complete","a_partlysubmitted,complete"))),   //21
    Array(new util.ArrayList[String](util.Arrays.asList("w_completeren_aanvraag,schedule","w_completeren_aanvraag,complete"))),   //22
    //Array(new util.ArrayList[String](util.Arrays.asList("w_completeren_aanvraag,schedule","w_completeren_aanvraag,complete"))),   //24
    Array(new util.ArrayList[String](util.Arrays.asList("a_submitted,complete","a_partlysubmitted,complete"))),   //23
    Array(new util.ArrayList[String](util.Arrays.asList("a_submitted,complete","a_partlysubmitted,complete"))),   //24
    Array(new util.ArrayList[String](util.Arrays.asList("a_accepted","a_finalized"))),                            //25
    Array(new util.ArrayList[String](util.Arrays.asList("a_accepted","a_finalized"))),                            //26
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //27
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent)))                                              //28

  )

  // before removing 24 :
  /*override val modelsPerEvent : Map[String,String] = Map(
    lastEvent->"0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30",
    "a_partlysubmitted"->"0,2,3,4,21,22,25,26",
    "a_submitted"->"1,2,3,4,21,22,25,26,29,30",
    "w_afhandelen_leads" -> "5,6",
    "w_completeren_aanvraag" ->"7,8,23,24",
    "w_beoordelen_fraude" ->"9,10,19",
    "o_selected" -> "11,14",
    "o_created" -> "11,12,13,14",
    "o_sent" -> "12,13",
    "a_cancelled"->"15,16,17,18",
    "a_activated" ->"15",
    "a_registered" ->"16",
    "a_approved" ->"17",
    "a_declined" ->"18,20",
    "w_wijzigen_contractgegevens" -> "19",
    "a_accepted" -> "20,27,28",
    "a_finalized" -> "27,28,29,30"
  )*/
  override val modelsPerEvent : Map[String,String] = Map(
    lastEvent->"0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28",
    "a_partlysubmitted"->"0,2,3,4,20,21,23,24",
    "a_submitted"->"1,2,3,4,20,21,23,24,27,28",
    "w_afhandelen_leads" -> "5,6",
    "w_completeren_aanvraag" ->"7,8,22",
    "w_beoordelen_fraude" ->"9,10,19",
    "o_selected" -> "11,14",
    "o_created" -> "11,12,13,14",
    "o_sent" -> "12,13",
    "a_cancelled"->"15,16,17,18",
    "a_activated" ->"15",
    "a_registered" ->"16",
    "a_approved" ->"17",
    "a_declined" ->"18",
    "w_wijzigen_contractgegevens" -> "19",
    "a_accepted" -> "25,26",
    "a_finalized" -> "25,26,27,28"
  )

  override val submodelWithLastEvent = 28       //30 //a_finalized ????
}
