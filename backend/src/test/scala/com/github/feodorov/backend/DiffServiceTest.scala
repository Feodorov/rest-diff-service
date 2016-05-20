package com.github.feodorov.backend

import com.github.feodorov.api.APIv1
import org.scalatest.{FlatSpec, Matchers}
import spray.http.StatusCodes
import spray.testkit.ScalatestRouteTest
import spray.json._
import scodec.bits._

import scala.concurrent.ExecutionContext

/**
  * @author kfeodorov 
  * @since 20.05.16
  */
class DiffServiceTest extends FlatSpec with Matchers with ScalatestRouteTest with DiffService with APIv1 {
  override implicit val ec = ExecutionContext.global

  def actorRefFactory = system

  "DiffService" should "show error if diff requested for empty ID" in {
    Get("/diff/1") ~> route ~> check {
      status should be (StatusCodes.OK)
      responseAs[String].parseJson should be ("""{"outcome":"ERROR","message":"Not enough data: No left instance found, No right instance found"}""".parseJson)
    }
  }

  it should "let user to upload left and right data and get diff" in {
    Post("/diff/1/left", hex"010203".toBase64) ~> route ~> check {
      status should be (StatusCodes.OK)
      responseAs[String].parseJson should be ("""{"outcome":"OK","message":"Saved"}""".parseJson)
    }
    Post("/diff/1/right", hex"010204".toBase64) ~> route ~> check {
      status should be (StatusCodes.OK)
      responseAs[String].parseJson should be ("""{"outcome":"OK","message":"Saved"}""".parseJson)
    }
    Get("/diff/1") ~> route ~> check {
      status should be (StatusCodes.OK)
      responseAs[String].parseJson should be ("""{
                        |  "outcome": "UNEQUAL",
                        |  "diffs": [{
                        |    "offset": 2,
                        |    "length": 1
                        |  }]
                        |}""".stripMargin.parseJson)
    }
  }
}
