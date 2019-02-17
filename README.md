## Dynamic programming by the example of the Fibonacci sequence and measured with JMH

## JMH Benchmarks

Running benchmarks:
* `./run-jmh-benchmarks.sh`
* `./run-jmh-jfr-benchmarks.sh` - additionally records Flight Recorder file from JMH run 

#### [FibonacciJmhBenchmark](benchmark/src/main/scala/com/kduda/dynamic/fibonacci/FibonacciJmhBenchmark.scala)
```scala
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
```

### Fibonacci algorithms

#### [FibonacciNaive](fibonacci/src/main/scala/com/kduda/dynamic/fibonacci/FibonacciNaive.scala)
```scala
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
```

#### [FibonacciNaiveMemoization](fibonacci/src/main/scala/com/kduda/dynamic/fibonacci/FibonacciNaiveMemoization.scala)
```scala
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
```

#### [FibonacciTailrec](fibonacci/src/main/scala/com/kduda/dynamic/fibonacci/FibonacciTailrec.scala)
```scala
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
```

#### [FibonacciStream](fibonacci/src/main/scala/com/kduda/dynamic/fibonacci/FibonacciStream.scala)
```scala
object FibonacciStream {
  def apply(n: Int): BigInt = {

    def _fibonacci(nMinusTwo: BigInt, nMinusOne: BigInt): Stream[BigInt] =
      nMinusTwo #:: _fibonacci(nMinusOne, nMinusTwo + nMinusOne)

    _fibonacci(0, 1).apply(n)
  }
}
```

#### [FibonacciBottomUp](fibonacci/src/main/scala/com/kduda/dynamic/fibonacci/FibonacciBottomUp.scala)
```scala
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
 ```

## Benchmarks results

#### JDK 1.8 HotSpot
```
openjdk version "1.8.0_202"
OpenJDK Runtime Environment (AdoptOpenJDK)(build 1.8.0_202-b08)
OpenJDK 64-Bit Server VM (AdoptOpenJDK)(build 25.202-b08, mixed mode)
```

```
Benchmark                                         Mode  Cnt        Score       Error  Units
FibonacciJmhBenchmark.fibonacciTailrec           thrpt  200  2001547.362 ±  6868.334  ops/s
FibonacciJmhBenchmark.fibonacciBottomUp          thrpt  200  1805695.925 ± 16442.757  ops/s
FibonacciJmhBenchmark.fibonacciStream            thrpt  200  1117719.733 ±  4825.625  ops/s
FibonacciJmhBenchmark.fibonacciNaiveMemoization  thrpt  200   563011.785 ± 14488.654  ops/s
FibonacciJmhBenchmark.fibonacciNaive             thrpt  200      621.838 ±     0.509  ops/s
```

#### JDK 1.8 OpenJ9
```
openjdk version "1.8.0_202"
OpenJDK Runtime Environment (build 1.8.0_202-b08)
Eclipse OpenJ9 VM (build openj9-0.12.1, JRE 1.8.0 Mac OS X amd64-64-Bit Compressed References 20190205_147 (JIT enabled, AOT enabled)
OpenJ9   - 90dd8cb40
OMR      - d2f4534b
JCL      - d002501a90 based on jdk8u202-b08)
```

```
Benchmark                                         Mode  Cnt        Score       Error  Units
FibonacciJmhBenchmark.fibonacciTailrec           thrpt  200  1854265.775 ± 24061.462  ops/s
FibonacciJmhBenchmark.fibonacciBottomUp          thrpt  200  1362137.742 ± 21082.305  ops/s
FibonacciJmhBenchmark.fibonacciStream            thrpt  200   682050.482 ±  3478.769  ops/s
FibonacciJmhBenchmark.fibonacciNaiveMemoization  thrpt  200   533618.996 ±  9791.518  ops/s
FibonacciJmhBenchmark.fibonacciNaive             thrpt  200      435.027 ±     4.988  ops/s
```

#### JDK 1.8 GraalVM
```
java version "1.8.0_192"
Java(TM) SE Runtime Environment (build 1.8.0_192-b12)
GraalVM 1.0.0-rc12 (build 25.192-b12-jvmci-0.54, mixed mode)
```

```
Benchmark                                         Mode  Cnt        Score       Error  Units
FibonacciJmhBenchmark.fibonacciTailrec           thrpt  200  5415798.980 ± 14753.933  ops/s
FibonacciJmhBenchmark.fibonacciBottomUp          thrpt  200  4420254.845 ±  4893.127  ops/s
FibonacciJmhBenchmark.fibonacciStream            thrpt  200  1365366.170 ±   933.818  ops/s
FibonacciJmhBenchmark.fibonacciNaiveMemoization  thrpt  200   901036.310 ±  1713.943  ops/s
FibonacciJmhBenchmark.fibonacciNaive             thrpt  200     1181.364 ±     6.406  ops/s
```

#### JDK 11.0.2 HotSpot
```
openjdk version "11.0.2" 2019-01-15
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.2+9)
OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.2+9, mixed mode)
```

```
Benchmark                                         Mode  Cnt        Score       Error  Units
FibonacciJmhBenchmark.fibonacciTailrec           thrpt  200  4497583.703 ± 42183.751  ops/s
FibonacciJmhBenchmark.fibonacciBottomUp          thrpt  200  2998348.356 ± 20048.948  ops/s
FibonacciJmhBenchmark.fibonacciStream            thrpt  200   944649.041 ±  6745.397  ops/s
FibonacciJmhBenchmark.fibonacciNaiveMemoization  thrpt  200   595821.665 ± 19302.382  ops/s
FibonacciJmhBenchmark.fibonacciNaive             thrpt  200     1046.870 ±     1.768  ops/s
```

#### JDK 11.0.1 OpenJ9
```
openjdk version "11.0.1" 2018-10-16
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.1+13)
Eclipse OpenJ9 VM AdoptOpenJDK (build openj9-0.11.0, JRE 11 Mac OS X amd64-64-Bit Compressed References 20181020_7 (JIT enabled, AOT enabled)
OpenJ9   - 090ff9dc
OMR      - ea548a66
JCL      - f62696f378 based on jdk-11.0.1+13)
```

```
Benchmark                                         Mode  Cnt        Score       Error  Units
FibonacciJmhBenchmark.fibonacciTailrec           thrpt  200  1748200.819 ± 32120.465  ops/s
FibonacciJmhBenchmark.fibonacciBottomUp          thrpt  200  1222295.070 ±  8661.716  ops/s
FibonacciJmhBenchmark.fibonacciStream            thrpt  200   634128.886 ±  5631.495  ops/s
FibonacciJmhBenchmark.fibonacciNaiveMemoization  thrpt  200   394875.229 ±  3638.218  ops/s
FibonacciJmhBenchmark.fibonacciNaive             thrpt  200      380.049 ±     1.844  ops/s
```

#### JDK 11.0.2 OpenJ9
```
openjdk version "11.0.2" 2019-01-15
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.2+9)
Eclipse OpenJ9 VM AdoptOpenJDK (build openj9-0.12.1, JRE 11 Mac OS X amd64-64-Bit Compressed References 20190204_123 (JIT enabled, AOT enabled)
OpenJ9   - 90dd8cb40
OMR      - d2f4534b
JCL      - 289c70b684 based on )
```

```
Benchmark                                         Mode  Cnt        Score       Error  Units
FibonacciJmhBenchmark.fibonacciTailrec           thrpt  200  1817007.843 ± 24424.427  ops/s
FibonacciJmhBenchmark.fibonacciBottomUp          thrpt  200  1193490.741 ±  8880.656  ops/s
FibonacciJmhBenchmark.fibonacciStream            thrpt  200   638851.149 ±  3091.760  ops/s
FibonacciJmhBenchmark.fibonacciNaiveMemoization  thrpt  200   450829.877 ±  9917.783  ops/s
FibonacciJmhBenchmark.fibonacciNaive             thrpt  200      388.061 ±     2.882  ops/s
```

## Comparison

![Algorithm types vs. JVM implementations [ops/s]](chart.png)


![Algorithm types vs. JVM implementations [ops/s]](chart-log.png)
