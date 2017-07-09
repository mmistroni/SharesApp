package com.mm.sharesapp.persistence
import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import org.scalatest._

abstract class SchemaTester extends DbTestBase {
  self: BaseDBConnector =>

  def schema : Schema

  def prePopulate() = {}

  override def beforeAll() = {

    super.beforeAll()
    println("----------------- CREATING SCHEMA------------------")
    sessionCreator().foreach { _ =>
      transaction {
        schema.drop
        schema.create
        try{
          prePopulate
        }catch{
          case e : Exception =>
            println("EXCEPTION:" + e.getMessage)
            println(e.getStackTrace)
        }
      }
    }
  }

  /**
  override def afterAll() = {
    super.afterAll()

    sessionCreator().foreach { _ =>
      transaction {
        schema.drop
      }
    }
  } **/
}


abstract class DbTestBase extends FunSuite with BeforeAndAfterAll with BeforeAndAfterEach with Matchers {
  self: BaseDBConnector =>

  def isIgnored(testName: String) =
    sessionCreator().isEmpty || ignoredTests.exists(_ == testName)


  def ignoredTests : List[String] = Nil

  override def beforeAll() = {
    println("Base db ...beforeALL")
    val c = sessionCreator()
    if(c.isDefined) {
      SessionFactory.concreteFactory = c
    }
  }

  override protected def runTest(testName: String,args: org.scalatest.Args): org.scalatest.Status = {
    if(isIgnored(testName))
      org.scalatest.SucceededStatus
    else
      super.runTest(testName, args)
  }

}
