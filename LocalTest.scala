package org.apache.spark.graphx

import org.apache.spark.graphx._
import org.apache.spark.graphx.impl._

object LocalTest {

  def main(args: Array[String]) {
    val num_edges = args(0).toInt

    println("Loading...")

    val builder = new EdgePartitionBuilder[Double, Double]
    for (i <- 1 to num_edges) {
      val fields = readLine().split("\t")
      val srcId = fields(0).toLong
      val dstId = fields(1).toLong
      builder.add(srcId, dstId, 1.0)
      if (i % 10000 == 0) println("Loaded %d edges".format(i))
    }

    val ep = builder.toEdgePartition

    println("Scanning...")

    var sum = 0.0
    val iters = 10
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
    println("Scanned %d edges in %f seconds".format(num_edges * iters, (end_time - start_time) / 1.0e9))

    println("Sum: " + sum)
  }

}
