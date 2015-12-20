package acceptance.support

import cucumber.api.scala.ScalaDsl

trait Env extends ScalaDsl {

  val db = Mongo("authorization-acceptance-tests")
  val authCollection = db.createCollection("authorization")

  val host = "http://localhost:9000"

  Before {
    s =>
      db.removeCollection(authCollection)
      println("data reset")
      Context.world.empty
  }

  After {
    s =>
      db.removeCollection(authCollection)
      println("data reset")
  }

}

object Context {
  var world: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map.empty[String, Any]
}