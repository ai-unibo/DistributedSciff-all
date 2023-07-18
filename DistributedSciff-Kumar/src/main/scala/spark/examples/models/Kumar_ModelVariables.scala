package spark.examples.models

import java.util

/**
  * Created by utente on 13/04/17.
  */
class Kumar_ModelVariables extends ModelVariables{
  override val lastEvent : String = "end_of_the_world"
  //val options : String = "[trace_max_length(43)]"
  override val options : String = "[ trace_max_length(43), generate_current_time(no), activities_have_start_and_end, double_chained_translation ]"
  override val model : String = "[seq(a1,a2),xor_split(a2,[a3,a5]),seq(a3,a4),seq(a5,a6),xor_split(a6,[a7,a8]),and_split(a8,[a9,a10])," +
    "and_join([a9,a10],a11),seq(a11,a12),xor_join([a4, a7, a12], a13),seq(a13,a14)]"
  //  without xor_join:
  // override val model : String = "[seq(start, a1), seq(a1, a2), xor_split(a2,[ a3, a5 ]), seq(a3, a4), seq(a5, a6), xor_split(a6,[ a7, a8 ])," +
  //" and_split(a8, [ a9, a10 ] ), and_join( [ a9, a10 ], a11 ), seq(a11, a12), seq(a4, a13), seq(a7, a13), seq(a12, a13), seq(a13, a14), seq(a14, stop)]"

  override val observability : String = "[obs(start,never),obs(a1,always),obs(a2,always),obs(a3,always),obs(a4,always),obs(a5,always)," +
    "obs(a6,always),obs(a7,always),obs(a8,always),obs(a9,always),obs(a10,always),obs(a11,always),obs(a12,always),obs(a13,always),obs(a14,always),obs(stop,never)]"
  override val durationConstr : String = "[(a1,(5,10)),(a2,(5,10)),(a3,(30,40))," +
    "(a4,(150,200)),(a5,(20,40)),(a6,(5,10)),(a7,(200,225)),(a8,(10,20)),(a9,(30,40)),(a10,(80,80)),(a11,(100,120)),(a12,(200,400)),(a13,(15,20)),(a14,(5,10))]"
  override val interTaskConstr : String = "[(a1, '_start', a2, '_end', 1, [0,30] ), " +
    "(a8, '_start', a11, '_start', 2, [100,140] ), (a10, '_end', a11, '_start', 1, [20,10000] ), (a10, '_start', a11, '_end', 1, [0,250] )]"

}
