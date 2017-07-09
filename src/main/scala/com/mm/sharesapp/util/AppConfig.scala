package com.mm.sharesapp.util
import com.typesafe.config.ConfigFactory

object AppConfig {
  
  private val config =  ConfigFactory.load()

  private lazy val root = config.getConfig("my_app")

  object DbConfig {
    private val httpConfig = config.getConfig("db")

    lazy val url = httpConfig.getString("host")
    lazy val username = httpConfig.getInt("username")
    lazy val password = httpConfig.getInt("password")
  }
}