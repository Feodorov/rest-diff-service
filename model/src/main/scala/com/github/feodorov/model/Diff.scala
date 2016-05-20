package com.github.feodorov.model

import com.github.feodorov.model.DiffOutcome.DiffOutcome

/**
  * @author kfeodorov 
  * @since 18.05.16
  */

sealed trait Offset
sealed trait Length
case class Segment(offset: Int, length: Int)
case class Diff(outcome: DiffOutcome, diffs: Option[Seq[Segment]] = None, message: Option[String] = None)
