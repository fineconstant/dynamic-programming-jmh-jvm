name := "dynamic-programming-fibonacci"

ThisBuild / organization := "com.kduda.dynamic.programming.fibonacci"
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.12.8"

lazy val root = (project in file("."))
  .aggregate(fibonacci, benchmark)

lazy val fibonacci = project

lazy val benchmark = project
  .dependsOn(fibonacci)
  .enablePlugins(JmhPlugin)
