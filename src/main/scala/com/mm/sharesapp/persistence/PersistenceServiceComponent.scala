package com.mm.sharesapp.persistence

trait PersistenceServiceComponent {
  
  def dbService(inst:AnyRef):BaseDbService
    
  
    
}