package com.github.typed

import akka.actor.Props
import scala.concurrent.Future

case class SampleResponse(value: String)

case object SampleRequest extends Replyable[SampleResponse]

case object SampleRequest2 extends Replyable[Int]

class SampleActor extends ReplyingActor {

  override def receiveReplyable[T]: PartialFunction[Replyable[T], Future[T]] = {
    case SampleRequest =>
      Future(SampleResponse("Ok"))
    case SampleRequest2 =>
      Future(100500)
  }

  override def receiveOther: PartialFunction[Any, Unit] = {
    case x => sender() ! "Not found"
  }
}

object SampleActor {
  def props:Props = Props(classOf[SampleActor])
}
