package com.github.feodorov.utils

import com.github.feodorov.model._
import DiffOutcome._
import scodec.bits._

import scalaz._
import Scalaz._

/**
  * @author kfeodorov 
  * @since 18.05.16
  */

object DiffUtils {

  /**
    * Calculate diff on two base64 encoded byte arrays
    * @return Status (EQUAL/UNEQUAL_SIZE/UNEQUAL) with an optional list of mismatches
    */
  def diff(s1: String, s2: String): ValidationNel[String, Diff] = {(
      ByteVector.fromBase64(s1).toSuccessNel(s"Cannot decode left instance") |@|
      ByteVector.fromBase64(s2).toSuccessNel(s"Cannot decode right instance")
    ) {_.wrapNel append _.wrapNel}
  } map {
    case NonEmptyList(v1, ICons(v2, INil())) if v1.length != v2.length => Diff(UNEQUAL_SIZE)
    case NonEmptyList(v1, ICons(v2, INil())) if v1 === v2 => Diff(EQUAL)
    case NonEmptyList(v1, ICons(v2, INil())) =>
      val diffWithIndex = v1.zipWith(v2)(_.compareTo(_).toByte).toIndexedSeq.zipWithIndex
      val mismatches = diffWithIndex.foldLeft(Seq.empty[Segment]) { case (acc, (b, i)) =>
        acc match {
          case init :+ last if b != 0 && last.offset + last.length === i =>
            init :+ Segment(last.offset, last.length + 1)
          case list if b != 0 => list :+ Segment(offset = i, length = 1)
          case list => list
        }
      }
      Diff(UNEQUAL, mismatches.some)
  }
}
