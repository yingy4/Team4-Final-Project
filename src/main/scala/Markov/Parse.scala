import scala.io.Source
import java.io._


import scala.collection.mutable

object Parse extends App {

  val lyFile = Source.fromFile("003-midi.ly").getLines().flatMap(_.split(" ")).toList
  val a = Source.fromFile("test.ly").getLines().flatMap(_.split(" ")).toList
  //获得头部公共部分
  def parseLyFile(words: List[String]): List[List[String]] = {
    //除去不要的部分（voiceTwo、voiceThree）
    val trueWords = words.filterNot(_.startsWith("\\voice")).drop(13)
    //1
    val splitCommonPart1 = trueWords.takeWhile(_ != "trackAchannelB").size
    //commonPart1: 开头到trackAchannelB = \relative c {
    val (commonPart1, rest1) = trueWords.splitAt(splitCommonPart1 +6)
    //tAcB: trackAchannelB内容，不包括｛ ｝
    val (tAcB, rest2) = rest1.span(_ != "}")
    //2
    val splitCommonPart2 = rest2.takeWhile(_ != "{").size
    val (commonPart2, rest3) = rest2.splitAt(splitCommonPart2 + 1)
    //    commonPart2 = commonPart2+:token
    val (tAcBvB, rest4) = rest3.span(_ != "}")
    //3
    val splitCommonPart3 = rest4.takeWhile(_ != "{").size
    val (commonPart3, rest5) = rest4.splitAt(splitCommonPart3 + 1)
    val (tAcBvC, rest6) = rest5.span(_ != "}")
    //4
    val splitCommonPart4 = rest6.takeWhile(_ != "{").size
    val (commonPart4, rest7) = rest6.splitAt(splitCommonPart4 + 1)
    val (tAcBvD, rest8) = rest7.span(_ != "}")
    //    //5
    //    val splitCommonPart5 = rest8.takeWhile(_ != "{").size
    //    val (commonPart5, rest9) = rest8.splitAt(splitCommonPart5 + 1)
    //    val (tAcBvE, rest10) = rest9.span(_ != "}")
    //    //    6
    //    val splitCommonPart6 = rest9.takeWhile(_ != "{").size
    //    val (commonPart6, rest11) = rest10.splitAt(splitCommonPart3 + 1)
    //    val (tAcBvF, rest12) = rest11.span(_ != "}")
    //    //7
    //    val splitCommonPart7 = rest12.takeWhile(_ != "{").size
    //    val (commonPart7, rest13) = rest12.splitAt(splitCommonPart3 + 1)
    //    val (tAcBvG, rest14) = rest13.span(_ != "}")
    //    //8
    val commonPart8 = List("}\n\ntrackA = <<\n  \\context Voice = voiceA \\trackAchannelA\n  \\context Voice = voiceB \\trackAchannelB\n  \\context Voice = voiceC \\trackAchannelBvoiceB\n  \\context Voice = voiceD \\trackAchannelBvoiceC\n  \\context Voice = voiceE \\trackAchannelBvoiceD\n  \\context Voice = voiceF \\trackAchannelBvoiceE\n  \\context Voice = voiceG \\trackAchannelBvoiceF\n  \\context Voice = voiceH \\trackAchannelBvoiceG\n  \\context Voice = voiceI \\trackAchannelBvoiceH\n>>\n\n\n\\score {\n  <<\n    \\context Staff=trackA \\trackA\n  >>\n  \\layout {}\n  \\midi {}\n}")
    //    println(tAcBvG)

    List(commonPart1, tAcB,
      commonPart2, tAcBvB,
      commonPart3, tAcBvC,
      commonPart4, tAcBvD,
      commonPart8)
    //      commonPart5, tAcBvE,
    //      commonPart6, tAcBvF,
    //      commonPart7, tAcBvG,
    //      commonPart8)

  }
  parseLyFile(lyFile)

}
