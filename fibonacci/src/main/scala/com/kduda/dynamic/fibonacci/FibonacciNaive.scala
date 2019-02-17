package com.kduda.dynamic.fibonacci

object FibonacciNaive {
  def apply(n: Int): BigInt = {
    n match {
      case 0 => BigInt(0)
      case 1 => BigInt(1)
      case 2 => BigInt(1)
      case _ => FibonacciNaive(n - 2) + FibonacciNaive(n - 1)
    }
  }
}
