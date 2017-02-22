package org.dcs.data

import com.typesafe.config.{Config, ConfigFactory}
import org.flywaydb.core.Flyway

/**
  * Created by cmathew on 19.01.17.
  */
object DbMigration {

  def migratePostgres(): Unit = {
    // Create the Flyway instance
    val flyway: Flyway = new Flyway()

    // Point it to the database
    val conf: Config = ConfigFactory.load()
    val postgres: Config = conf.getConfig("postgres")
    val url = postgres.getString("url")
    val user = postgres.getString("user")
    val password = postgres.getString("password")
    flyway.setDataSource(url, user, password)

    flyway.setLocations("db/migration/postgres")

    // Start the migration
    flyway.migrate()
  }
}
