package com.github.feodorov.utils

import com.github.feodorov.model.DiffOutcome._
import com.github.feodorov.model.{Diff, Segment}
import org.scalatest.{FlatSpec, Matchers}
import scodec.bits._

import scalaz._

/**
  * @author kfeodorov 
  * @since 18.05.16
  */
class DiffUtilsTest extends FlatSpec with Matchers {

  "DiffUtils" should "return error for bad base64 strings" in {
    val good = "YQ=="
    val bad1 = "???"
    val bad2 = ":("
    DiffUtils.diff(bad1, bad2) shouldBe Failure(NonEmptyList(s"Cannot decode left instance", s"Cannot decode right instance"))
    DiffUtils.diff(good, bad2) shouldBe Failure(NonEmptyList(s"Cannot decode right instance"))
    DiffUtils.diff(bad2, good) shouldBe Failure(NonEmptyList(s"Cannot decode left instance"))
  }

  it should "work detect equal data" in {
    DiffUtils.diff("AAA", "AAA") shouldBe Success(Diff(EQUAL))
  }

  it should "work detect unequal sizes" in {
    DiffUtils.diff("AAA", "AAA" * 3) shouldBe Success(Diff(UNEQUAL_SIZE))
  }

  it should "return a list of mismatches" in {
    DiffUtils.diff(
      hex"010203".toBase64,
      hex"01FF03".toBase64
    ) shouldBe Success(Diff(UNEQUAL, Some(Segment(1, 1) :: Nil)))

    DiffUtils.diff(
      hex"010203".toBase64,
      hex"FF02FF".toBase64
    ) shouldBe Success(Diff(UNEQUAL, Some(Segment(0, 1) :: Segment(2, 1) :: Nil)))
  }
}
