package com.github.feodorov.backend

import com.github.feodorov.model.DiffOutcome._
import com.github.feodorov.model.{Diff, DiffOutcome, Segment}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

/**
  * @author kfeodorov 
  * @since 20.05.16
  */
object JsonProtocol extends DefaultJsonProtocol {
  implicit object DiffOutcomeJsonFormat extends RootJsonFormat[DiffOutcome] {
    def write(obj: DiffOutcome.DiffOutcome): JsString = JsString(obj.toString)

    def read(json: JsValue): DiffOutcome.DiffOutcome = json match {
      case JsString(id) => DiffOutcome.withName(id)
      case _ => throw new DeserializationException("Enum string expected")
    }
  }
  implicit val segmentFormat = jsonFormat2(Segment.apply)
  implicit val diffFormat = jsonFormat3(Diff.apply)
}