package spark.examples.models

import java.util

/**
  * Created by utente on 13/04/17.
  */
class BPI_6SubModelVariables extends ModelVariables{
  //override val lastEvent : String = "end_of_the_world"
  //val options : String = "[trace_max_length(43)]"
  override val options : String = "[ trace_max_length(43), generate_current_time(no), activities_have_start_and_end, double_chained_translation, events_contain_traceId(yes)  ]"
  override val submodels = Array(
    "H(event(TraceId, a_partlysubmitted,complete,_R1), T1) ---> EN(event(TraceId, a_partlysubmitted,complete,_R2), T2) /\\\\ T2<>T1." +
      "H(event(TraceId, a_submitted,complete,_R1), T1) ---> EN(event(TraceId, a_submitted,complete,_R2), T2) /\\\\ T2<>T1." +
      "H(event(TraceId, a_submitted,complete,_R1), T1) ---> E(event(TraceId, a_partlysubmitted,complete,_R2), T2) /\\\\ T2>T1"+
                                                    "/\\\\ EN(event(TraceId, _act, _lifecyle, _R3), Tin) /\\\\ Tin>T1 /\\\\ Tin<T2." +
      "H(event(TraceId, a_partlysubmitted,complete,_R2), T2) ---> E(event(TraceId, a_submitted,complete,_R1), T1) /\\\\ T2>T1"+
                                                    "/\\\\ EN(event(TraceId, _act, _lifecyle, _R3), Tin) /\\\\ Tin>T1 /\\\\ Tin<T2." +
      "true --->   E(event(TraceId, a_submitted,complete,_R1), T1)"+
                                              "/\\\\ E(event(TraceId, a_partlysubmitted,complete,_R2), T2) /\\\\ T2>T1"+
                                              "/\\\\ EN(event(TraceId, _act, _lifecyle, _Rbefore), Tbefore) /\\\\ Tbefore<T1"+
                                              "/\\\\ EN(event(TraceId, _act, _lifecyle, _Rin), Tin) /\\\\ Tin>T1 /\\\\ Tin<T2.",                      //0

    "H(event(TraceId, w_afhandelen_leads, schedule, _R1), T1) ---> E(event(TraceId, w_afhandelen_leads, start,_R2), T2) /\\\\ T2>T1"+
                                              "/\\\\ EN(event(TraceId, w_afhandelen_leads, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin1<T2"+
                                              "/\\\\ EN(event(TraceId, w_afhandelen_leads, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
                                              "/\\\\ EN(event(TraceId, w_afhandelen_leads, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2." +
      "H(event(TraceId, w_afhandelen_leads, start, _R1), T1) ---> E(event(TraceId, w_afhandelen_leads, complete,_R2), T2) /\\\\ T2>T1"+
                                              "/\\\\ EN(event(TraceId, w_afhandelen_leads, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin1<T2"+
                                              "/\\\\ EN(event(TraceId, w_afhandelen_leads, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
                                              "/\\\\ EN(event(TraceId, w_afhandelen_leads, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2." +
      "H(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) ---> E(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) /\\\\ T2>T1." +
      "H(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) ---> E(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) /\\\\ T2>T1." +
      "H(event(TraceId, w_beoordelen_fraude, schedule, _R1), T1) ---> E(event(TraceId, w_beoordelen_fraude, start,_R2), T2) /\\\\ T2>T1"+
                                              "/\\\\ EN(event(TraceId, w_beoordelen_fraude, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin<T2"+
                                              "/\\\\ EN(event(TraceId, w_beoordelen_fraude, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
                                              "/\\\\ EN(event(TraceId, w_beoordelen_fraude, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2.",     //1

    "H(event(TraceId, w_beoordelen_fraude, start, _R1), T1) ---> E(event(TraceId, w_beoordelen_fraude, complete,_R2), T2) /\\\\ T2>T1"+
                                            "/\\\\ EN(event(TraceId, w_beoordelen_fraude, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin<T2"+
                                            "/\\\\ EN(event(TraceId, w_beoordelen_fraude, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
                                            "/\\\\ EN(event(TraceId, w_beoordelen_fraude, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2." +
      "H(event(TraceId, o_selected, complete, _R1), T1) ---> E(event(TraceId, o_created, complete, _R2), T2) /\\\\ T2>T1." +
      "H(event(TraceId, o_created, complete, _R1), T1) ---> E(event(TraceId, o_sent, complete, _R2), T2) /\\\\ T2>T1." +
      "H(event(TraceId, o_sent, complete, _R2), T2) ---> E(event(TraceId, o_created, complete, _R1), T1) /\\\\ T2>T1." +
      "H(event(TraceId, o_created, complete, _R2), T2) ---> E(event(TraceId, o_selected, complete, _R1), T1) /\\\\ T2>T1.",                            //2

    "H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_activated, complete, _R), T)." +
      "H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_registered, complete, _R), T)." +
      "H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_approved, complete, _R), T)." +
      "H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_declined, complete, _R), T)." +
      "H(event(TraceId, w_beoordelen_fraude, schedule, _R1), T1) ---> EN(event(TraceId, w_wijzigen_contractgegevens, schedule, _R), T).",             //3

    //"H(event(TraceId, a_accepted, _LifeCycle, _R1), T1) ---> EN(event(TraceId, a_declined, _LifeCycle, _R), T)." +
      "H(event(TraceId, a_submitted, complete, _R1), T1) ---> E(event(TraceId, a_partlysubmitted, complete, _R2), T2) /\\\\ T2>T1 /\\\\ T2<=T1+22000." +
      "H(event(TraceId, a_partlysubmitted, complete, _R2), T2) ---> E(event(TraceId, a_submitted, complete, _R1), T1) /\\\\ T2>T1 /\\\\ T2<=T1+22000." +
      "H(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) ---> E(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) /\\\\ T2>T1+22000 /\\\\ T2<=T1+239368000.",  //4
      //+"H(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) ---> E(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) /\\\\ T2>T1+22000 /\\\\ T2<=T1+239368000.",     //4

    "H(event(TraceId, a_submitted, complete, R), T1) ---> E(event(TraceId, a_partlysubmitted, complete, R), T2)." +
      "H(event(TraceId, a_partlysubmitted, complete, R), T1) ---> E(event(TraceId, a_submitted, complete, R), T2)." +
      "H(event(TraceId, a_accepted, _LifeCycle, R), T1) ---> E(event(TraceId, a_finalized, _LifeCycle, R), T2)." +
      "H(event(TraceId, a_finalized, _LifeCycle, R), T1) ---> E(event(TraceId, a_accepted, _LifeCycle, R), T2)." +
      "H(event(TraceId, a_submitted, _LifeCycle, R), T1) ---> EN(event(TraceId, a_finalized, _LifeCycle, R), T2)." +
      "H(event(TraceId, a_finalized, _LifeCycle, R), T1) ---> EN(event(TraceId, a_submitted, _LifeCycle, R), T2)."                            //5
  )

  override val subtraceCompleteIf = Array(
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //0
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //1
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //2
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //3
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent))),                                             //4
    Array(new util.ArrayList[String](util.Arrays.asList(lastEvent)))                                             //5
  )

  override val modelsPerEvent : Map[String,String] = Map(
    lastEvent->"0,1,2,3,4,5",
    "a_partlysubmitted"->"0,4,5",
    "a_submitted"->"0,4,5",
    "w_afhandelen_leads" -> "1",
    "w_completeren_aanvraag" ->"4",
    "w_beoordelen_fraude" ->"1,2,3",
    "o_selected" -> "2",
    "o_created" -> "2",
    "o_sent" -> "2",
    "a_cancelled"->"3",
    "a_activated" ->"3",
    "a_registered" ->"3",
    "a_approved" ->"3",
    "a_declined" ->"3",
    "w_wijzigen_contractgegevens" -> "3",
    "a_accepted" -> "5",
    "a_finalized" -> "5"
  )


  override val submodelWithLastEvent = 5 //a_finalized ????
}
