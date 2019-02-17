package com.kduda.dynamic.fibonacci

import scala.collection.mutable

object FibonacciNaiveMemoization {
  def apply(n: Int): BigInt = {
    val cache = mutable.WeakHashMap.empty[Int, BigInt]

    def _fibonacci(n: Int): BigInt = {
      cache.getOrElse(n, n match {
        case 0 =>
          val y = BigInt(0)
          cache.put(n, y)
          y
        case 1 =>
          val y = BigInt(1)
          cache.put(n, y)
          y
        case _ =>
          val y = _fibonacci(n - 2) + _fibonacci(n - 1)
          cache.put(n, y)
          y
      })
    }
    _fibonacci(n)
  }
}
