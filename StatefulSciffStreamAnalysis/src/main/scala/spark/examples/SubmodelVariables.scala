package spark.examples

import java.util

/**
  * Created by utente on 13/04/17.
  */
class SubmodelVariables {
  val model : String = "[seq(a1,a2),xor_split(a2,[a3,a5]),seq(a3,a4),seq(a5,a6),xor_split(a6,[a7,a8]),and_split(a8,[a9,a10])," +
    "and_join([a9,a10],a11),seq(a11,a12),xor_join([a4, a7, a12], a13),seq(a13,a14)]"
  val lastEvent : String = "end_of_the_world"
  val observability : String = "[obs(start,never),obs(a1,always),obs(a2,always),obs(a3,always),obs(a4,always),obs(a5,always)," +
    "obs(a6,always),obs(a7,always),obs(a8,always),obs(a9,always),obs(a10,always),obs(a11,always),obs(a12,always),obs(a13,always),obs(a14,always),obs(stop,never)]"
  val durationConstr : String = "[(a1,(5,10)),(a2,(5,10)),(a3,(30,40))," +
    "(a4,(150,200)),(a5,(20,40)),(a6,(5,10)),(a7,(200,225)),(a8,(10,20)),(a9,(30,40)),(a10,(80,80)),(a11,(100,120)),(a12,(200,400)),(a13,(15,20)),(a14,(5,10))]"
  val interTaskConstr : String = "[(a1, '_start', a2, '_end', 1, [0,30] ), " +
    "(a8, '_start', a11, '_start', 2, [100,140] ), (a10, '_end', a11, '_start', 1, [20,10000] ), (a10, '_start', a11, '_end', 1, [0,250] )]"

  //val options : String = "[trace_max_length(43)]"
  val options : String = "[ trace_max_length(43), generate_current_time(no), activities_have_start_and_end, double_chained_translation ]"


  val submodels = Array("[ seq(a1, a2), xor_split(a2,[ a3, a5 ]), seq(a3, a4) ]",     //0
    "[ seq(a5, a6), xor_split(a6,[ a7, a8 ]) ]",                                      //1
    "[ and_split(a8, [ a9, a10 ] ), and_join( [ a9, a10 ], a11 ) , seq(a11, a12) ]",  //2
    "[ xor_join([a4, a7, a12], a13), seq(a13, a14) ]")                                //3

  val observabilities = Array("[obs(start,never),obs(a1,always),obs(a2,always),obs(a3,always),obs(a4,always),obs(a5,always)]",
    "[obs(a5,always),obs(a6,always),obs(a7,always),obs(a8,always)]",
    "[obs(a8,always),obs(a9,always),obs(a10,always),obs(a11,always),obs(a12,always)]",
    "[obs(a4,always),obs(a7,always),obs(a12,always),obs(a13,always),obs(a14,always),obs(stop,never)]"
  )

  val durationConstrs = Array("[ (a1, (5,10)), (a2, (5,10)), (a3, (30,40)) ]",
    "[ (a5, (20,40)), (a6, (5,10)), (a8, (10,20))]",
    "[ (a9, (30,40)), (a10, (80,80)) , (a11, (100,120)) ]",
    "[ (a4, (150,200)), (a7, (200,225)), (a12, (200,400)), (a13, (15,20)), (a14, (5,10)) ]")

  val interTaskConstrs = Array("[ (a1, '_start', a2, '_end', 1, [0,30] ) ]",
    "[]",
    "[ (a8, '_start', a11, '_start', 2, [100,140] ), (a10, '_end', a11, '_start', 1, [20,10000] ), (a10, '_start', a11, '_end', 1, [0,250] )]",
    "[]"
  )


  //end_of_the_world vero (presente nello stream) è volutamente escluso dagli eventi necessari perchè il sottomodello sia competo.
  val subtraceCompleteIf = Array(
    Array(new util.ArrayList[String](util.Arrays.asList("a1_start","a1_end","a2_start","a2_end","a5_start","a5_end")),
      new util.ArrayList[String](util.Arrays.asList("a1_start","a1_end","a2_start","a2_end","a3_start","a3_end","a4_start","a4_end"))),
    Array(new util.ArrayList[String](util.Arrays.asList("a5_start","a5_end","a6_start","a6_end","a8_start","a8_end")),
      new util.ArrayList[String](util.Arrays.asList("a5_start","a5_end","a6_start","a6_end","a7_start","a7_end"))),
    Array(new util.ArrayList[String](util.Arrays.asList("a8_start","a8_end","a9_start","a9_end","a10_start","a10_end","a11_start","a11_end","a12_start","a12_end"))),
    Array(new util.ArrayList[String](util.Arrays.asList("a4_start","a4_end","a13_start","a13_end","a14_start","a14_end")),
      new util.ArrayList[String](util.Arrays.asList("a7_start","a7_end","a13_start","a13_end","a14_start","a14_end")),
      new util.ArrayList[String](util.Arrays.asList("a12_start","a12_end","a13_start","a13_end","a14_start","a14_end")))
  )

  val modelsPerEvent : Map[String,Array[Int]] = Map(
    lastEvent->Array(0,1,2,3,4),
    "a1_start"->Array(0),"a1_end"->Array(0),
    "a2_start"->Array(0),"a2_end"->Array(0),
    "a3_start"->Array(0),"a3_end"->Array(0),
    "a4_start"->Array(0,3),"a4_end"->Array(0,3),
    "a5_start"->Array(0,1),"a5_end"->Array(0,1),
    "a6_start"->Array(1),"a6_end"->Array(1),
    "a7_start"->Array(1,3),"a7_end"->Array(1,3),
    "a8_start"->Array(1,2),"a8_end"->Array(1,2),
    "a9_start"->Array(2),"a9_end"->Array(2),
    "a10_start"->Array(2),"a10_end"->Array(2),
    "a11_start"->Array(2),"a11_end"->Array(2),
    "a12_start"->Array(2,3),"a12_end"->Array(2,3),
    "a13_start"->Array(3),"a13_end"->Array(3),
    "a14_start"->Array(3),"a14_end"->Array(3)
  )

  val submodelWithLastEvent = 4
}
