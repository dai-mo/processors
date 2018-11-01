/*
 * Copyright (c) 2017-2018 brewlabs SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
