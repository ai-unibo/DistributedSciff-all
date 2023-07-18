package spark.examples.models

/**
  * Created by utente on 13/04/17.
  */
class BPI_ModelVariables extends ModelVariables{
  override val lastEvent : String = "end_of_the_world"
  //val options : String = "[trace_max_length(43)]"
  override val options : String = "[ trace_max_length(43), generate_current_time(no), activities_have_start_and_end, double_chained_translation, events_contain_traceId(yes)  ]"
  override val model : String =
    "H(event(TraceId, a_partlysubmitted,complete,_R1), T1) ---> EN(event(TraceId, a_partlysubmitted,complete,_R2), T2) /\\\\ T2<>T1."+
"H(event(TraceId, a_submitted,complete,_R1), T1) ---> EN(event(TraceId, a_submitted,complete,_R2), T2) /\\\\ T2<>T1."+
"H(event(TraceId, a_submitted,complete,_R1), T1) ---> E(event(TraceId, a_partlysubmitted,complete,_R2), T2) /\\\\ T2>T1"+
                                                            "/\\\\ EN(event(TraceId, _act, _lifecyle, _R3), Tin) /\\\\ Tin>T1 /\\\\ Tin<T2."+
"H(event(TraceId, a_partlysubmitted,complete,_R2), T2) ---> E(event(TraceId, a_submitted,complete,_R1), T1) /\\\\ T2>T1"+
                                                            "/\\\\ EN(event(TraceId, _act, _lifecyle, _R3), Tin) /\\\\ Tin>T1 /\\\\ Tin<T2."+
"true --->   E(event(TraceId, a_submitted,complete,_R1), T1)"+
            "/\\\\ E(event(TraceId, a_partlysubmitted,complete,_R2), T2) /\\\\ T2>T1            "+
            "/\\\\ EN(event(TraceId, _act, _lifecyle, _Rbefore), Tbefore) /\\\\ Tbefore<T1"+
            "/\\\\ EN(event(TraceId, _act, _lifecyle, _Rin), Tin) /\\\\ Tin>T1 /\\\\ Tin<T2."+
"H(event(TraceId, w_afhandelen_leads, schedule, _R1), T1) ---> E(event(TraceId, w_afhandelen_leads, start,_R2), T2) /\\\\ T2>T1"+
                                                            "/\\\\ EN(event(TraceId, w_afhandelen_leads, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin1<T2"+
                                                            "/\\\\ EN(event(TraceId, w_afhandelen_leads, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
                                                            "/\\\\ EN(event(TraceId, w_afhandelen_leads, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2."+
"H(event(TraceId, w_afhandelen_leads, start, _R1), T1) ---> E(event(TraceId, w_afhandelen_leads, complete,_R2), T2) /\\\\ T2>T1"+
                                                            "/\\\\ EN(event(TraceId, w_afhandelen_leads, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin1<T2"+
                                                            "/\\\\ EN(event(TraceId, w_afhandelen_leads, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
                                                            "/\\\\ EN(event(TraceId, w_afhandelen_leads, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2."+
"H(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) ---> E(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) /\\\\ T2>T1."+
"H(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) ---> E(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) /\\\\ T2>T1."+
"H(event(TraceId, w_beoordelen_fraude, schedule, _R1), T1) ---> E(event(TraceId, w_beoordelen_fraude, start,_R2), T2) /\\\\ T2>T1"+
                                                            "/\\\\ EN(event(TraceId, w_beoordelen_fraude, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin<T2"+
                                                            "/\\\\ EN(event(TraceId, w_beoordelen_fraude, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
                                                            "/\\\\ EN(event(TraceId, w_beoordelen_fraude, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2."+
"H(event(TraceId, w_beoordelen_fraude, start, _R1), T1) ---> E(event(TraceId, w_beoordelen_fraude, complete,_R2), T2) /\\\\ T2>T1"+
                                                            "/\\\\ EN(event(TraceId, w_beoordelen_fraude, schedule, _Rin1), Tin1) /\\\\ Tin1>T1 /\\\\ Tin<T2"+
                                                            "/\\\\ EN(event(TraceId, w_beoordelen_fraude, start, _Rin2), Tin2) /\\\\ Tin2>T1 /\\\\ Tin2<T2"+
                                                            "/\\\\ EN(event(TraceId, w_beoordelen_fraude, complete, _Rin3), Tin3) /\\\\ Tin3>T1 /\\\\ Tin3<T2."+
"H(event(TraceId, o_selected, complete, _R1), T1) ---> E(event(TraceId, o_created, complete, _R2), T2) /\\\\ T2>T1."+
"H(event(TraceId, o_created, complete, _R1), T1) ---> E(event(TraceId, o_sent, complete, _R2), T2) /\\\\ T2>T1."+
"H(event(TraceId, o_sent, complete, _R2), T2) ---> E(event(TraceId, o_created, complete, _R1), T1) /\\\\ T2>T1."+
"H(event(TraceId, o_created, complete, _R2), T2) ---> E(event(TraceId, o_selected, complete, _R1), T1) /\\\\ T2>T1."+
"H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_activated, complete, _R), T)."+
"H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_registered, complete, _R), T)."+
"H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_approved, complete, _R), T)."+
"H(event(TraceId, a_cancelled, complete, _R1), T1) ---> EN(event(TraceId, a_declined, complete, _R), T)."+
"H(event(TraceId, w_beoordelen_fraude, schedule, _R1), T1) ---> EN(event(TraceId, w_wijzigen_contractgegevens, schedule, _R), T)."+
//"H(event(TraceId, a_accepted, _LifeCycle, _R1), T1) ---> EN(event(TraceId, a_declined, _LifeCycle, _R), T)."+
"H(event(TraceId, a_submitted, complete, _R1), T1) ---> E(event(TraceId, a_partlysubmitted, complete, _R2), T2) /\\\\ T2>T1 /\\\\ T2<=T1+22000."+
"H(event(TraceId, a_partlysubmitted, complete, _R2), T2) ---> E(event(TraceId, a_submitted, complete, _R1), T1) /\\\\ T2>T1 /\\\\ T2<=T1+22000."+
"H(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) ---> E(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) /\\\\ T2>T1+22000 /\\\\ T2<=T1+239368000."+
//"H(event(TraceId, w_completeren_aanvraag, complete, _R2), T2) ---> E(event(TraceId, w_completeren_aanvraag, schedule, _R1), T1) /\\\\ T2>T1+22000 /\\\\ T2<=T1+239368000."+
"H(event(TraceId, a_submitted, complete, R), T1) ---> E(event(TraceId, a_partlysubmitted, complete, R), T2)."+
"H(event(TraceId, a_partlysubmitted, complete, R), T1) ---> E(event(TraceId, a_submitted, complete, R), T2)."+
"H(event(TraceId, a_accepted, _LifeCycle, R), T1) ---> E(event(TraceId, a_finalized, _LifeCycle, R), T2)."+
"H(event(TraceId, a_finalized, _LifeCycle, R), T1) ---> E(event(TraceId, a_accepted, _LifeCycle, R), T2)."+
"H(event(TraceId, a_submitted, _LifeCycle, R), T1) ---> EN(event(TraceId, a_finalized, _LifeCycle, R), T2)."+
"H(event(TraceId, a_finalized, _LifeCycle, R), T1) ---> EN(event(TraceId, a_submitted, _LifeCycle, R), T2)."

}
