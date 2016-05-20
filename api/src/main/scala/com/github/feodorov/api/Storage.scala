package com.github.feodorov.api

/**
  * @author kfeodorov 
  * @since 19.05.16
  */
trait Storage[A, B] {
  def get(k: A) : Option[B]

  def put(k: A, v: B): Unit
}
