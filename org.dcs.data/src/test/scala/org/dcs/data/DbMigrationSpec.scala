package org.dcs.data

import org.scalatest.Ignore

/**
  * Created by cmathew on 17.02.17.
  */
@Ignore
class DbMigrationSpec extends DbMigrationBehaviour {

  "Database migration" should " update database according to schema changes" in {
    DbMigration.migratePostgres()
  }

}

trait DbMigrationBehaviour extends DataUnitSpec {

}
