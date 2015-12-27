package com.github.typed

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.{Await, Future}

class TypedTestSpec extends TestKit(ActorSystem("LergReader"))
with DefaultTimeout with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    shutdown()
  }

  "Typed" should {
    "should reply" in {
      import com.github.typed.reply._

      import scala.concurrent.duration._

      val sampleActor = system.actorOf(SampleActor.props)

      val eventualResponse: Future[SampleResponse] = sampleActor ? SampleRequest

      val result: SampleResponse = Await.result(eventualResponse, 3.seconds)

      result.value shouldBe "Ok"

      sampleActor ! "Trololo"

      val untyped: String = expectMsgClass(classOf[String])
      untyped shouldBe "Not found"


      val intTyped: Int = Await.result(sampleActor ? SampleRequest2, 3.seconds)
      intTyped shouldBe 100500

    }
  }

}
