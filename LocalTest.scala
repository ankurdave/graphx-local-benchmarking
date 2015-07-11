package org.apache.spark.graphx

import org.apache.spark.graphx._
import org.apache.spark.graphx.impl._

trait Accum[A] {
  def add(x: A)
  def print()
}

class DoubleAccum extends Accum[Double] {
  var sum = 0.0
  def add(x: Double) { sum += x }
  def print() { println(sum)}
}

object LocalTest {


  def main(args: Array[String]) {
    var i = 0.0; 

    var accum: Accum[Double] = new DoubleAccum()
    val start_time = System.nanoTime
    while (i < 1.0e10) {
      accum.add(i)
      i += 1.0
    }
    val end_time = System.nanoTime
    val dur = (end_time - start_time) / 1.0e9
    accum.print()
    println(s"Runtime ${dur}")
  }

  def edgeScan(args: Array[String]) {
    val file = args(0)
    val iters = args(1).toInt

    println("Loading...")

    var num_edges = 0
    val builder = new EdgePartitionBuilder[Double, Double]
    for (line <- scala.io.Source.fromFile(file).getLines) {
      num_edges += 1
      val fields = line.split("\t")
      val srcId = fields(0).toLong
      val dstId = fields(1).toLong
      builder.add(srcId, dstId, 1.0)
    }

    val ep = builder.toEdgePartition

    println("Scanning...")

    var sum = 0.0
    val start_time = System.nanoTime
    var i = 0

    while (i < iters) {
      val result = ep.aggregateMessagesEdgeScan[Double](
        ctx => ctx.sendToDst(ctx.srcAttr * ctx.attr),
        _ + _,
        TripletFields.Src,
        EdgeActiveness.Neither)

      // while (result.hasNext) {
      //   sum += result.next._2
      // }

      i += 1
    }


    val end_time = System.nanoTime
    val dur = (end_time - start_time) / 1.0e9
    println("Scanned %d edges in %f seconds".format(num_edges * iters, dur))
    println("%f edges/s".format(num_edges * iters / dur))

    println("Sum: " + sum)
  }

}
