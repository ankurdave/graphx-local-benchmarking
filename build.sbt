name := "graphx-local-benchmarking"

version := "0.1-SNAPSHOT"

organization := "edu.berkeley.cs.amplab"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.5.0-SNAPSHOT"
libraryDependencies += "org.apache.spark" %% "spark-graphx" % "1.5.0-SNAPSHOT"

// Run apps with more memory
javaOptions += "-Xmx12G"

fork := true
