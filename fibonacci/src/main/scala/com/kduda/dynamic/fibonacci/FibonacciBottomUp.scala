package com.kduda.dynamic.fibonacci

object FibonacciBottomUp {
  def apply(n: Int): BigInt = {
    def _fibonacci(n: Int): BigInt = {
      var nMinusTwo = BigInt(0)
      var nMinusOne = BigInt(1)
      var currentN  = BigInt(0)

      for (_ <- 0 until n) {
        nMinusTwo = nMinusOne
        nMinusOne = currentN
        currentN  = nMinusTwo + nMinusOne
      }

      currentN
    }

    n match {
      case 0 => BigInt(0)
      case 1 => BigInt(1)
      case 2 => BigInt(1)
      case _ => _fibonacci(n)
    }
  }
}
