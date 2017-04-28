package com.mm.sharesapp.persistence

import com.mm.sharesapp.entities.SharePrice

abstract class ShareDbService extends BaseDbService {
  this:DbComponent =>
    type DomainObject = SharePrice
    
    def insert(item:SharePrice):Boolean = {
      db.save(item)
      true
    }
  
    def findAll:Seq[SharePrice] = db.query[SharePrice].fetch()
  
    def findByExample(example:SharePrice):Seq[SharePrice] = null
  
  
}