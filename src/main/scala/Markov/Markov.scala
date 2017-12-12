package Markov

import shapeless._
import shapeless.ops.nat._
import shapeless.syntax.sized._

//N的类型上界是Nat
// ToInt[N]： type-level Nats to value level Ints.



class Markov[N <: Nat] private(implicit n: ToInt[N]) {

  //relate[T]: markov chain当前用作参考的是N个元素
  type Relate[T] = Markov.Relate[T, N]
  type Dict[T] = Markov.Dict[T, N]

  //order: 如markov.order2, 则order = 2
  val order = n()
  //  println(order+1)

  //将获得的word list变为dictionary
  def build[T](xs: List[T]): Dict[T] = {
    //    获得空Dict[T]
    val blankDict: Dict[T] =
      Map.empty.withDefaultValue(Vector.empty)


    def subDict(current: Dict[T], vector: Sized[Vector[T], Succ[N]]): Dict[T] = {
      //ensureSized[N]: 返回relate[T,N]
      //vector[T].ensureSized[N] --->new Sized[Vector[T],N]
      //.init: 返回除最后一个元素之外的所有元素
      val key = vector.init.ensureSized[N]
      //在key的位置更新，
      // :+ 尾部添加,相同的key下的集合组成该key的dictionary
      current.updated(key, current(key) :+ vector.last)
    }

    //Succ[N]: 求后续

    xs.sliding(order + 1)
      .map(_.toVector.ensureSized[Succ[N]]) //new Sized[Vector[T], Succ[N]]
      .foldLeft(blankDict)(subDict)
  }

  //从build(word list)中，得到需要的markov chain
  def gen[T](dict: Dict[T]): Gen[Stream[T]] = Gen { random =>
    //选出vector中的某一个元素
    def rand[A](vector: Vector[A]): Option[A] =
      if (vector.isEmpty) None
      else vector.lift(random.nextInt(vector.size))

    def slide(relate: Relate[T], next: T): Relate[T] =
      (relate.unsized.tail :+ next).ensureSized[N]

    //dict.get(relate).flatMap(rand):将dict中key为relate的vector取出，rand处理（随机选择其中一个类型为T的元素）
    def loop(relate: Relate[T]): Stream[T] =
      dict.get(relate).flatMap(rand).fold(Stream.empty[T]) { next =>
        next #:: loop(slide(relate, next))
      }

    //.keys： 获取所有的key
    val first = rand(dict.keys.toVector).get
    first.toStream append loop(first)
  }

  def run[T](xs: List[T]): Gen[Stream[T]] =
    gen(build(xs))

}
object Markov {

  //sized:用于收集具有静态指定长度的集合类型的包装器,
  //Nat:type层面的，自然数的,基础的trait
  type Relate[T, N <: Nat] = Sized[Vector[T], N]
  type Dict[T, N <: Nat] = Map[Relate[T, N], Vector[T]]

  def apply[N <: Nat](implicit n: ToInt[N]): Markov[N] = new Markov[N]


  lazy val Const2 = Markov[Nat._2]

  lazy val Const12 = Markov[Nat._12]


}


