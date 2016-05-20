package com.github.feodorov.api

/**
  * @author kfeodorov
  * @since 19.05.16.
  */
trait API extends BusinessLogic with Storage[String, String]
