import java.io._

import Markov.Markov
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source


object Main extends App {
  //  val config = new SparkConf().setAppName("Team4Final").setMaster("local")
  //  val sc = new SparkContext(config)


  var commonPart1 = List[String]()
  var temptAcB = List[String]()
  var commonPart2 = List[String]()
  var temptAcBvB = List[String]()
  var commonPart3 = List[String]()
  var temptAcBvC = List[String]()
  var commonPart4 = List[String]()
  var temptAcBvD = List[String]()
  //  var commonPart5 = List[String]()
  //  var temptAcBvE = List[String]()
  //  var commonPart6 = List[String]()
  //  var temptAcBvF = List[String]()
  //  var commonPart7 = List[String]()
  //  var temptAcBvG = List[String]()
  var commonPart8 = List[String]()

  //generate common parts
  val lyFile = Source.fromFile("music/101-midi.ly").getLines().flatMap(_.split(" ")).toList
  //  val lyFile = sc.textFile("music/101-midi.ly",2).flatMap(_.split(" ")).toLocalIterator.toList

  var moduleList = Parse.parseLyFile(lyFile)
  commonPart1 = moduleList(0)
  commonPart2 = moduleList(2)
  commonPart3 = moduleList(4)
  commonPart4 = moduleList(6)
  //    commonPart5 = moduleList(8)
  //    commonPart6 = moduleList(10)
  //    commonPart7 = moduleList(12)
  commonPart8 = moduleList(8)


  //generate music part
  for (i <- 101 to 215) {
    val lyFile = Source.fromFile("music/" + i + "-midi.ly").getLines().flatMap(_.split(" ")).toList
    //获得List[List[String]]
    var moduleList = Parse.parseLyFile(lyFile)

    //将获得的每个tAcB部分汇集成一个大的word list
    //      .filter(_ != "")
    temptAcB = temptAcB ++ moduleList(1)
    temptAcBvB = temptAcBvB ++ moduleList(3)
    temptAcBvC = temptAcBvC ++ moduleList(5)
    temptAcBvD = temptAcBvD ++ moduleList(7)
    //    temptAcBvE = temptAcBvE ++ moduleList(9).filter(_ != "")
    //    temptAcBvF = temptAcBvF ++ moduleList(11).filter(_ != "")
    //    temptAcBvG = temptAcBvG ++ moduleList(13).filter(_ != "")

  }


  val seed = System.currentTimeMillis()
  //  //Const2: 由前2个元素决定后一个元素
  val markov = Markov.Const2
  //
  val tAcB = markov.run(temptAcB.sliding(12, 12).toList).get(seed).flatten.take(1000)
  val tAcBvB = markov.run(temptAcBvB).get(seed).take(850)
  val tAcBvC = markov.run(temptAcBvC).get(seed).take(500)
  val tAcBvD = markov.run(temptAcBvD).get(seed).take(280)
  //  val tAcBvE = markov.run(temptAcBvE).get(seed).take(160)
  //  val tAcBvF = markov.run(temptAcBvF).get(seed).take(40)
  //  val tAcBvG = markov.run(temptAcBvG).get(seed).take(19)

  //
  //  //写入操作
  val writer = new PrintWriter(new File("test.ly"))

  writer.write(commonPart1.mkString(" "))
  writer.write(tAcB.mkString(" "))
  writer.write(commonPart2.mkString(" "))
  writer.write(tAcBvB.mkString(" "))
  writer.write(commonPart3.mkString(" "))
  writer.write(tAcBvC.mkString(" "))
  writer.write(commonPart4.mkString(" "))
  writer.write(tAcBvD.mkString(" "))
  writer.write(commonPart8.mkString(" "))

  writer.close()

}

