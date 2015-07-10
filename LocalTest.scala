package org.apache.spark.graphx

import org.apache.spark.graphx._
import org.apache.spark.graphx.impl._

object LocalTest {

  def main(args: Array[String]) {
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

    for (i <- 1 to iters) {
      val result = ep.aggregateMessagesEdgeScan[Double](
        ctx => ctx.sendToDst(ctx.srcAttr * ctx.attr),
        _ + _,
        TripletFields.Src,
        EdgeActiveness.Neither)

      while (result.hasNext) {
        sum += result.next._2
      }
    }

    val end_time = System.nanoTime
    val dur = (end_time - start_time) / 1.0e9
    println("Scanned %d edges in %f seconds".format(num_edges * iters, dur))
    println("%f edges/s".format(num_edges * iters / dur))

    println("Sum: " + sum)
  }

}
