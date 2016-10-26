package org.dcs.data.cassandra

import java.util.{Date, UUID}

import io.getquill._
import org.dcs.data.DataUnitSpec
import org.scalatest.Ignore

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by cmathew on 26.10.16.
  */
@Ignore // FIXME: Remove ignore when integration setup for cassandra is complete
class CassandraConnectionSpec extends DataUnitSpec {


  "New processor data" should "be inserted / deleted correctly" in {

    case class ProcessorData(id: UUID, timestamp: Date, processorId: UUID, data: Array[Byte]) {
      override def equals(that: Any): Boolean = that match {
        case ProcessorData(thatId, thatTimestamp, thatProcessorId, thatData) =>
          thatId == this.id &&
          thatTimestamp == this.timestamp &&
          thatProcessorId == this.processorId &&
          thatData.deep == this.data.deep
        case _ => false
      }
    }

    lazy val ctx = new CassandraAsyncContext[SnakeCase]("ctx")

    import ctx._

    val data = "Test".getBytes
    val id = UUID.randomUUID()
    val pd = ProcessorData(id, new Date(), UUID.randomUUID(), data)

    val insertDataQ = quote(query[ProcessorData].insert(lift(pd)))
    ctx.run(insertDataQ)

    val dataQuery = quote(query[ProcessorData].filter(p => p.id == lift(id)))
    var queryResult = ctx.run(dataQuery)
    whenReady(queryResult) { result =>
      assert(result.head == pd)
    }

    val deleteQuery = quote(query[ProcessorData].filter(p => p.id == lift(id)).delete)
    ctx.run(deleteQuery)
    queryResult = ctx.run(dataQuery)

    whenReady(queryResult) { result =>
      assert(result == Nil)
    }
  }

}
