package com.kduda.dynamic.fibonacci

import scala.annotation.tailrec

object FibonacciTailrec {
  def apply(n: Int): BigInt = {

    @tailrec
    def _fibonacci(n: Int, nMinusTwo: BigInt, nMinusOne: BigInt): BigInt = {
      n match {
        case 0 => nMinusTwo
        case 1 => nMinusOne
        case _ => _fibonacci(n - 1, nMinusOne, nMinusOne + nMinusTwo)
      }
    }

    _fibonacci(n, 0, 1)
  }
}
