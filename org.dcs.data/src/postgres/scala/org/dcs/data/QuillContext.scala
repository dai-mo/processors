package org.dcs.data

import io.getquill.{JdbcContext, PostgresDialect, SnakeCase}
import org.dcs.commons.config.DbConfig

/**
  * Created by cmathew on 02.02.17.
  */
object QuillContext extends  JdbcContext[PostgresDialect, SnakeCase](DbConfig.DbPostgresPrefix)
