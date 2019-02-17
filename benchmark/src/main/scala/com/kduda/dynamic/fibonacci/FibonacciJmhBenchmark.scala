package com.kduda.dynamic.fibonacci

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole

@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(10)
@Threads(1)
class FibonacciJmhBenchmark {

  private val fibonacciN = 25

  @Benchmark
  def fibonacciNaive(bh: Blackhole): Unit = {
    bh.consume(FibonacciNaive(fibonacciN))
  }

  @Benchmark
  def fibonacciNaiveMemoization(bh: Blackhole): Unit = {
    bh.consume(FibonacciNaiveMemoization(fibonacciN))
  }

  @Benchmark
  def fibonacciTailrec(bh: Blackhole): Unit = {
    bh.consume(FibonacciTailrec(fibonacciN))
  }

  @Benchmark
  def fibonacciStream(bh: Blackhole): Unit = {
    bh.consume(FibonacciStream(fibonacciN))
  }

  @Benchmark
  def fibonacciBottomUp(bh: Blackhole): Unit = {
    bh.consume(FibonacciBottomUp(fibonacciN))
  }
}
