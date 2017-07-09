package com.mm.sharesapp.persistence
import org.squeryl.PrimitiveTypeMode._

import org.squeryl._

trait RunTestsInsideTransaction extends DbTestBase {
  self: BaseDBConnector =>

  override protected def runTest(testName: String,args: org.scalatest.Args): org.scalatest.Status = {

    if(isIgnored(testName))
      super.runTest(testName, args)
    else {
      // each test occur from within a transaction, that way when the test completes _all_ changes
      // made during the test are reverted so each test gets a clean enviroment to test against
      transaction {
        println("runTest inside trans. runTest..." + testName)
        val res = super.runTest(testName, args)

        // we abort the transaction if we get to here, so changes get rolled back
        Session.currentSession.connection.rollback
        return res
      }
    }
  }

}

