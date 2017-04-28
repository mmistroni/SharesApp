package com.mm.sharesapp.persistence

trait DbConfigComponent {
  
  val config:Config
  
  trait Config {
    val username:String
    val password:String
    val dbName:String
    val dbUrl:String
  }
}