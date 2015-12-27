package com.github.typed

import akka.actor.Actor
import akka.actor.Status.Failure
import scala.concurrent.Future
import scala.language.higherKinds
import scala.util.Success

trait ReplyingActor extends Actor {
  implicit val ec = context.dispatcher

  final def receive(): Receive = {
    case m: Replyable[_] if receiveReplyable.isDefinedAt(m) =>
      val replyTo = sender()
      implicit val ec = context.dispatcher
      try {
        receiveReplyable(m).onComplete {
          case Success(x) => replyTo ! x
          case util.Failure(e) => replyTo ! Failure(e)
        }

      } catch {
        case e: Exception => replyTo ! Failure(e)
      }
    case x => receiveOther(x)
  }

  def receiveReplyable[T]: PartialFunction[Replyable[T], Future[T]]

  def receiveOther: PartialFunction[Any, Unit]
}
