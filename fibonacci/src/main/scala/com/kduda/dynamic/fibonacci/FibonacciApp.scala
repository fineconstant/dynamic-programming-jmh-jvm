package com.kduda.dynamic.fibonacci

object FibonacciApp {
  def main(args: Array[String]): Unit = {
    for { x <- 0 to 40 } println(s"Naive(x) = $x ${FibonacciNaive(x)}")                        // takes to long when when n > 30
    for { x <- 0 to 100 } println(s"NaiveMemoization(x) = $x ${FibonacciNaiveMemoization(x)}") // stack overflow when n > 3000
    for { x <- 0 to 100 } println(s"Tailrec(x) = $x ${FibonacciTailrec(x)}")                   // no stack overflow, and works surprisingly fast
    for { x <- 0 to 100 } println(s"Stream(x) = $x ${FibonacciStream(x)}")                     // works great
    for { x <- 0 to 100 } println(s"BottomUp(x) = $x ${FibonacciBottomUp(x)}")                 // works great
  }
}
