package com.mm.sharesapp.persistence

import com.mm.sharesapp.entities.Share

class ShareDbService extends DbService {
    type DomainObject = Share

    def insert(item: Share): Boolean = {
      Db.save(item)
      true
    }

    def findAll: Seq[Share] = Db.query[Share].fetch()

    def findByExample(example: Share): Seq[Share] = null

  }