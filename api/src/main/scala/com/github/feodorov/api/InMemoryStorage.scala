package com.github.feodorov.api

import java.util.concurrent.ConcurrentHashMap

import scala.concurrent.Future

/**
  * @author kfeodorov 
  * @since 19.05.16
  */
trait InMemoryStorage extends Storage[String, String] {
  val s = new ConcurrentHashMap[String, String]()

  override def get(k: String) = Option(s.get(k))

  override def put(k: String, v: String) = s.put(k, v)
}
