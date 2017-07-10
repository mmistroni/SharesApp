package com.mm.sharesapp.util
import com.typesafe.config.ConfigFactory

object AppConfig {
  
  private val config =  ConfigFactory.load()

  private lazy val root = config.getConfig("my_app")

  object DbConfig {
    private val dbConfig = config.getConfig("db")

    lazy val url = dbConfig.getString("host")
    lazy val username = dbConfig.getString("username")
    lazy val password = dbConfig.getString("password")
  }
}