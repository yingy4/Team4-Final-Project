package Markov

import util.Random

trait Rand {
  def nextInt(n: Int): Int
}

trait Gen[T] {

  def get(seed: Long): T
}

object Gen {

  def apply[T](block: Rand => T): Gen[T] = new Gen[T] {
    def get(seed: Long): T = block(new Rand {
      val rnd = new Random(seed)

      def nextInt(n: Int): Int = rnd.nextInt(n)
    })
  }

}
