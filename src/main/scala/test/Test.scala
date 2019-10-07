package test

import outwatch.dom._
import outwatch.dom.dsl._
import outwatch.reactive._

import cats.effect.SyncIO

object Test {
  def main(args: Array[String]): Unit = {

    val mainElement = h1("Hello World!")

    OutWatch
      .renderInto[SyncIO]("#app", mainElement)
      .unsafeRunSync()
  }
}
