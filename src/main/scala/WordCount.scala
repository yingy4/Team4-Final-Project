import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhouliang6 on 2017/6/20.
  */
object WordCount {
  def main(args: Array[String]) {
    val logFile ="111.txt"

    val conf = new SparkConf().setAppName("WordCount").setMaster("local")

    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2)
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
    print("---------------")
  }
}

