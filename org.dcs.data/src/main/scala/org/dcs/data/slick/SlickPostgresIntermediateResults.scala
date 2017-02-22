package org.dcs.data.slick

import slick.driver.PostgresDriver

/**
  * Created by cmathew on 19.02.17.
  */
object SlickPostgresIntermediateResults extends SlickIntermediateResults(PostgresDriver, "postgres")
