package test

import outwatch._
import outwatch.dsl._

import cats.effect.SyncIO

import colibri._
import outwatch.reactive.handler._

sealed trait StoreEvent
object StoreEvent {
  case class AddTodo(todo: String) extends StoreEvent
  case class RemoveTodo(index: Int) extends StoreEvent
}

case class State(todos: List[String])
object State {
  def initial = State(todos = Nil)
}

object Test {
  def main(args: Array[String]): Unit = {

    def mainElement(handler: Subject[StoreEvent]) = {
      // val allEvents = handler.scan(List.empty[StoreEvent]) { (list, event) => event :: list }

      val state: Observable[State] = handler.scan(State.initial) { (state, event) =>
        event match {
          case StoreEvent.AddTodo(todo) => state.copy(todos = todo :: state.todos)
          case StoreEvent.RemoveTodo(idx) => state.copy(todos = state.todos.patch(idx, Nil, 1))
        }
      }

      div(
        h1("Hello World!"),

        input(
          tpe := "text",

          onChange.value.map(value => StoreEvent.AddTodo(value)) --> handler,
          value <-- handler.map(_ => "")
        ),

        ul(
          state.map(_.todos.zipWithIndex.map { case (todo, idx) =>
            li(todo, button("x", marginLeft := "5px", onClick.use(StoreEvent.RemoveTodo(idx)) --> handler))
          })
        )
      )
    }

    val app = for {
      handler <- Handler.create[StoreEvent]
      _ <- OutWatch.renderInto[SyncIO]("#app", mainElement(handler))
    } yield ()

    app.unsafeRunSync()
  }
}
