package org.dcs.data

/**
  * Created by cmathew on 17.02.17.
  */
class DbMigrationSpec extends DbMigrationBehaviour {

  "Database migration" should " update database according to schema changes" in {
    DbMigration.migratePostgres()
  }

}

trait DbMigrationBehaviour extends DataUnitSpec {

}
