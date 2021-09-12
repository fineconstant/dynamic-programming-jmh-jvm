package com.kduda.dynamic.fibonacci

object FibonacciStream {
  def apply(n: Int): BigInt = {

    def _fibonacci(nMinusTwo: BigInt, nMinusOne: BigInt): LazyList[BigInt] =
      nMinusTwo #:: _fibonacci(nMinusOne, nMinusTwo + nMinusOne)

    _fibonacci(0, 1).apply(n)
  }
}
