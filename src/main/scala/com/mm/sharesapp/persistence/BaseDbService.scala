package com.mm.sharesapp.persistence

import sorm._
trait BaseDbService {
  type DomainObject<:AnyRef
  
  val db:Instance
  
  def insert(item:DomainObject):Boolean
  
  def findAll:Seq[DomainObject]
  
  def findByExample(example:DomainObject):Seq[DomainObject]
  
}