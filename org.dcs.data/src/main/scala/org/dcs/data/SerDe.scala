package org.dcs.data

import java.io._

import org.apache.avro.Schema
import org.apache.avro.data.Json
import org.apache.avro.file.{DataFileReader, DataFileWriter}
import org.apache.avro.generic.{GenericDatumReader, GenericDatumWriter, GenericRecord}
import org.apache.avro.io._
import org.apache.avro.specific.{SpecificData, SpecificDatumReader, SpecificDatumWriter}

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * Created by cmathew on 24.10.16.
  */

trait SerDe[S, SerT, InDeSerT, OutDeSerT] {

  def ser(schema: Option[S], deSerObj: InDeSerT): SerT

  def deser(wSchema: Option[S], rSchema: Option[S], serObj: SerT): OutDeSerT
}

trait FileSerDe[S, SerT, InDeSerT, OutDeSerT] {

  def ser(schema: Option[S], deSerObj: List[InDeSerT], file: File)

  def deser(schema: Option[S], serObj: SerT): List[OutDeSerT]

  def genData(dfr: DataFileReader[OutDeSerT]): List[OutDeSerT] = dfr.hasNext match {
    case true => dfr.next :: genData(dfr)
    case false => Nil
  }
}

trait JsonSerDe[SerT, InDeSerT, OutDeSerT] {

  def ser(deSerObj: InDeSerT): SerT

  def deser(serObj: SerT): OutDeSerT
}



class AvroSpecificFileSerDe[InDeSerT, OutDeSerT] extends FileSerDe[Schema, File, InDeSerT, OutDeSerT]  {

  override def ser(schema: Option[Schema], deSerObjs: List[InDeSerT], file: File) = {
    val userDatumWriter: DatumWriter[InDeSerT] = new SpecificDatumWriter[InDeSerT]
    val  dataFileWriter: DataFileWriter[InDeSerT] = new DataFileWriter[InDeSerT](userDatumWriter)

    dataFileWriter.create(schema.get, file)
    deSerObjs.foreach(dso => dataFileWriter.append(dso))
    dataFileWriter.close()
  }

  override def deser(schema: Option[Schema], file: File): List[OutDeSerT] = {
    val rSchema = if(schema.isDefined) schema.get else null
    val  datumReader: DatumReader[OutDeSerT] = new SpecificDatumReader[OutDeSerT](null, rSchema)
    val  dataFileReader: DataFileReader[OutDeSerT] = new DataFileReader[OutDeSerT](file, datumReader);
    genData(dataFileReader)
  }
}

class AvroSpecificByteArraySerDe[InDeSerT, OutDeSerT] extends SerDe[Schema, Array[Byte], InDeSerT, OutDeSerT] {

  override def ser(schema: Option[Schema], deSerObj: InDeSerT): Array[Byte] = {
    val out: ByteArrayOutputStream = new ByteArrayOutputStream()
    val encoder: BinaryEncoder= EncoderFactory.get().binaryEncoder(out, null)
    val writer: DatumWriter[InDeSerT] = new SpecificDatumWriter[InDeSerT](schema.get)

    writer.write(deSerObj, encoder)
    encoder.flush()
    out.close()
    out.toByteArray
  }

  override def deser(wSchema: Option[Schema], rSchema: Option[Schema], bytes: Array[Byte]): OutDeSerT = {

    val reader: SpecificDatumReader[OutDeSerT] = new SpecificDatumReader[OutDeSerT](wSchema.get, rSchema.get)
    val decoder: Decoder = DecoderFactory.get().binaryDecoder(bytes, null)
    reader.read(null.asInstanceOf[OutDeSerT], decoder)
  }
}

class AvroGenericFileSerDe extends FileSerDe[Schema, File, GenericRecord, GenericRecord]  {

  override def ser(schema: Option[Schema], deSerObjs: List[GenericRecord], file: File) = {
    val datumWriter: DatumWriter[GenericRecord] = new GenericDatumWriter[GenericRecord](schema.get)
    val dataFileWriter: DataFileWriter[GenericRecord] = new DataFileWriter[GenericRecord](datumWriter)
    dataFileWriter.create(schema.get, file)
    deSerObjs.foreach(dso => dataFileWriter.append(dso))
    dataFileWriter.close()
  }

  override def deser(schema: Option[Schema], file: File): List[GenericRecord] = {
    val datumReader: DatumReader[GenericRecord] = new GenericDatumReader[GenericRecord](schema.get);
    val dataFileReader: DataFileReader[GenericRecord] = new DataFileReader[GenericRecord](file, datumReader);
    genData(dataFileReader)
  }
}

class AvroGenericByteArraySerDe extends SerDe[Schema, Array[Byte], GenericRecord, GenericRecord] {

  override def ser(schema: Option[Schema], deSerObj: GenericRecord): Array[Byte] = {
    val out: ByteArrayOutputStream = new ByteArrayOutputStream()
    val encoder: BinaryEncoder= EncoderFactory.get().binaryEncoder(out, null)
    val writer: DatumWriter[GenericRecord] = new GenericDatumWriter[GenericRecord](schema.get)

    writer.write(deSerObj, encoder)
    encoder.flush()
    out.close()
    out.toByteArray
  }

  override def deser(wSchema: Option[Schema], rSchema: Option[Schema], bytes: Array[Byte]): GenericRecord = {

    val reader = new GenericDatumReader[GenericRecord](wSchema.get, rSchema.get)
    val decoder: Decoder = DecoderFactory.get().binaryDecoder(bytes, null)
    reader.read(null.asInstanceOf[GenericRecord], decoder)
  }
}

class AvroGenericJsonObjectByteArraySerDe extends JsonSerDe[Array[Byte], AnyRef, mutable.Map[String, AnyRef]] {

  override def ser(jsonObj: AnyRef): Array[Byte] = {
    val out: ByteArrayOutputStream = new ByteArrayOutputStream()
    val encoder: BinaryEncoder= EncoderFactory.get().binaryEncoder(out, null)

    val writer: Json.ObjectWriter = new Json.ObjectWriter()
    writer.write(jsonObj, encoder)
    encoder.flush()
    out.close()
    out.toByteArray
  }

  override def deser(bytes: Array[Byte]): mutable.Map[String, AnyRef] = {

    val reader: Json.ObjectReader = new Json.ObjectReader
    val decoder: Decoder = DecoderFactory.get().binaryDecoder(bytes, null)
    reader.read(null.asInstanceOf[AnyRef], decoder).asInstanceOf[java.util.LinkedHashMap[String, AnyRef]].asScala
  }
}



