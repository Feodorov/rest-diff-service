package com.github.feodorov.model

/**
  * @author kfeodorov 
  * @since 18.05.16
  */

object DiffOutcome extends Enumeration {
  type DiffOutcome = Value
  val EQUAL, UNEQUAL_SIZE, UNEQUAL, ERROR, OK = Value
}
