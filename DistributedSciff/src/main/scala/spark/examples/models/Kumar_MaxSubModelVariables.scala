package spark.examples.models

import java.util

/**
  * Created by utente on 13/04/17.
  */
class Kumar_MaxSubModelVariables extends ModelVariables{

  override val submodels = Array(
    "[ seq(a1, a2) ]",                                                  //0
    "[ xor_split(a2,[ a3, a5 ]) ]",                                     //1
    "[ seq(a3, a4) ]",                                                  //2
    "[ seq(a5, a6) ]",                                                  //3
    "[ xor_split(a6,[ a7, a8 ]) ]",                                     //4
    "[ and_split(a8, [ a9, a10 ] )]",                                   //5
    "[ and_join( [ a9, a10 ], a11 ) ]",                                 //6
    "[ seq(a11, a12) ]",                                                //7
    "[ xor_join([a4, a7, a12], a13) ]",                                 //8
    "[ seq(a13, a14) ]",                                                //9
    "[]",                                     //10
    "[]",                                     //11
    "[]",                                     //12
    "[]",                                     //13
    "[]",                                     //14
    "[]",                                     //15
    "[]",                                     //16
    "[]",                                     //17
    "[]",                                     //18
    "[]",                                     //19
    "[]",                                     //20
    "[]",                                     //21
    "[]",                                     //22
    "[]",                                     //23
    "[]",                                     //24
    "[]",                                     //25
    "[]",                                     //26
    "[]"                                     //27
  )


  override val observabilities = Array(
    "[]",                                     //0
    "[]",                                     //1
    "[]",                                     //2
    "[]",                                     //3
    "[]",                                     //4
    "[]",                                     //5
    "[]",                                     //6
    "[]",                                     //7
    "[]",                                     //8
    "[]",                                     //9
    "[]",                                     //10
    "[]",                                     //11
    "[]",                                     //12
    "[]",                                     //13
    "[]",                                     //14
    "[]",                                     //15
    "[]",                                     //16
    "[]",                                     //17
    "[]",                                     //18
    "[]",                                     //19
    "[]",                                     //20
    "[]",                                     //21
    "[]",                                     //22
    "[]",                                     //23
    "[]",                                     //24
    "[]",                                     //25
    "[]",                                     //26
    "[]"                                      //27
  )


  override val durationConstrs = Array(
    "[]",                                     //0
    "[]",                                     //1
    "[]",                                     //2
    "[]",                                     //3
    "[]",                                     //4
    "[]",                                     //5
    "[]",                                     //6
    "[]",                                     //7
    "[]",                                     //8
    "[]",                                     //9
    "[(a1, (5,10))]",                           //10
    "[(a2, (5,10))]",                           //11
    "[(a3, (30,40))]",                          //12
    "[(a4,(150,200))]",                         //13
    "[(a5, (20,40))]",                          //14
    "[(a6, (5,10))]",                           //15
    "[(a7,(200,225))]",                         //16
    "[(a8, (10,20))]",                          //17
    "[(a9, (30,40))]",                          //18
    "[(a10, (80,80))]",                         //19
    "[(a11, (100,120)) ]",                      //20
    "[(a12, (200,400))]",                       //21
    "[(a13, (15,20))]",                         //22
    "[(a14, (5,10)) ]",                         //23
    "[]",                                     //24
    "[]",                                     //25
    "[]",                                     //26
    "[]"                                      //27
  )


  override val interTaskConstrs = Array(
    "[]",                                     //0
    "[]",                                     //1
    "[]",                                     //2
    "[]",                                     //3
    "[]",                                     //4
    "[]",                                     //5
    "[]",                                     //6
    "[]",                                     //7
    "[]",                                     //8
    "[]",                                     //9
    "[]",                                     //10
    "[]",                                     //11
    "[]",                                     //12
    "[]",                                     //13
    "[]",                                     //14
    "[]",                                     //15
    "[]",                                     //16
    "[]",                                     //17
    "[]",                                     //18
    "[]",                                     //19
    "[]",                                     //20
    "[]",                                     //21
    "[]",                                     //22
    "[]",                                     //23
    "[(a1, '_start', a2, '_end', 1, [0,30] )]",       //24
    "[(a8, '_start', a11, '_start', 2, [100,140] )]", //25
    "[(a10, '_end', a11, '_start', 1, [20,10000] )]", //26
    "[(a10, '_start', a11, '_end', 1, [0,250] )]"     //27
  )

  //end_of_the_world vero (presente nello stream) è volutamente escluso dagli eventi necessari perchè il sottomodello sia competo.
  override val subtraceCompleteIf = Array(
    Array(new util.ArrayList[String](util.Arrays.asList("a1_start","a1_end","a2_start","a2_end"))),  //0
    Array(new util.ArrayList[String](util.Arrays.asList("a2_start","a2_end","a3_start","a3_end")), new util.ArrayList[String](util.Arrays.asList("a2_start","a2_end","a5_start","a5_end"))),  //1
    Array(new util.ArrayList[String](util.Arrays.asList("a3_start","a3_end","a4_start","a4_end"))),  //2
    Array(new util.ArrayList[String](util.Arrays.asList("a5_start","a5_end","a6_start","a6_end"))),  //3
    Array(new util.ArrayList[String](util.Arrays.asList("a6_start","a6_end","a7_start","a7_end")), new util.ArrayList[String](util.Arrays.asList("a6_start","a6_end","a8_start","a8_end"))),//4
    Array(new util.ArrayList[String](util.Arrays.asList("a8_start","a8_end","a9_start","a9_end","a10_start","a10_end"))),//5
    Array(new util.ArrayList[String](util.Arrays.asList("a9_start","a9_end","a10_start","a10_end","a11_start","a11_end"))),//6
    Array(new util.ArrayList[String](util.Arrays.asList("a11_start","a11_end","a12_start","a12_end"))),//7
    Array(new util.ArrayList[String](util.Arrays.asList("a4_start","a4_end","a13_start","a13_end")),new util.ArrayList[String](util.Arrays.asList("a7_start","a7_end","a13_start","a13_end")),new util.ArrayList[String](util.Arrays.asList("a12_start","a12_end","a13_start","a13_end"))),//8
    Array(new util.ArrayList[String](util.Arrays.asList("a13_start","a13_end","a14_start","a14_end"))),//9
    Array(new util.ArrayList[String](util.Arrays.asList("a1_start","a1_end"))),//10
    Array(new util.ArrayList[String](util.Arrays.asList("a2_start","a2_end"))),//11
    Array(new util.ArrayList[String](util.Arrays.asList("a3_start","a3_end"))),//12
    Array(new util.ArrayList[String](util.Arrays.asList("a4_start","a4_end"))),//13
    Array(new util.ArrayList[String](util.Arrays.asList("a5_start","a5_end"))),//14
    Array(new util.ArrayList[String](util.Arrays.asList("a6_start","a6_end"))),//15
    Array(new util.ArrayList[String](util.Arrays.asList("a7_start","a7_end"))),//16
    Array(new util.ArrayList[String](util.Arrays.asList("a8_start","a8_end"))),//17
    Array(new util.ArrayList[String](util.Arrays.asList("a9_start","a9_end"))),//18
    Array(new util.ArrayList[String](util.Arrays.asList("a10_start","a10_end"))),//19
    Array(new util.ArrayList[String](util.Arrays.asList("a11_start","a11_end"))),//20
    Array(new util.ArrayList[String](util.Arrays.asList("a12_start","a12_end"))),//21
    Array(new util.ArrayList[String](util.Arrays.asList("a13_start","a13_end"))),//22
    Array(new util.ArrayList[String](util.Arrays.asList("a14_start","a14_end"))),//23
    Array(new util.ArrayList[String](util.Arrays.asList("a1_start","a2_end"))),//24
    Array(new util.ArrayList[String](util.Arrays.asList("a8_start","a11_start"))),//25
    Array(new util.ArrayList[String](util.Arrays.asList("a10_end","a11_start"))),//26
    Array(new util.ArrayList[String](util.Arrays.asList("a10_start","a11_end"))),//27



    Array(new util.ArrayList[String](util.Arrays.asList("a1_start","a1_end","a2_start","a2_end","a5_start","a5_end")),
      new util.ArrayList[String](util.Arrays.asList("a1_start","a1_end","a2_start","a2_end","a3_start","a3_end","a4_start","a4_end"))),
    Array(new util.ArrayList[String](util.Arrays.asList("a5_start","a5_end","a6_start","a6_end","a8_start","a8_end")),
      new util.ArrayList[String](util.Arrays.asList("a5_start","a5_end","a6_start","a6_end","a7_start","a7_end"))),
    Array(new util.ArrayList[String](util.Arrays.asList("a8_start","a8_end","a9_start","a9_end","a10_start","a10_end","a11_start","a11_end","a12_start","a12_end"))),
    Array(new util.ArrayList[String](util.Arrays.asList("a4_start","a4_end","a13_start","a13_end","a14_start","a14_end")),
      new util.ArrayList[String](util.Arrays.asList("a7_start","a7_end","a13_start","a13_end","a14_start","a14_end")),
      new util.ArrayList[String](util.Arrays.asList("a12_start","a12_end","a13_start","a13_end","a14_start","a14_end")))
  )


  override val modelsPerEvent : Map[String,String] = Map(
    lastEvent->"0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27",
    "a1"->"0,10",
    "a2"->"0,1,11",
    "a3"->"1,2,12",
    "a4"->"2,8,13",
    "a5"->"1,3,14",
    "a6"->"3,4,15",
    "a7"->"4,8,16",
    "a8"->"4,5,17",
    "a9"->"5,6,18",
    "a10"->"5,6,19",
    "a11"->"6,7,20",
    "a12"->"7,8,21",
    "a13"->"8,9,22",
    "a14"->"9,23"
  )


  override val submodelWithLastEvent = 27
}
