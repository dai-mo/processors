package org.dcs.data.avro

import java.io.File

import org.apache.avro.Schema
import org.apache.avro.data.Json
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.commons.io.IOUtils
import org.dcs.data.model.{User, UserWithAge}
import org.dcs.data._

import scala.collection.mutable

/**
  * Created by cmathew on 24.10.16.
  */
class TestAvroSerDeSpec extends DataUnitSpec {

  val firstName = "Obi"
  val middleName = "Wan"
  val lastName = "Kenobi"
  val age = 1024
  val defaultAge = -1


  "Specific Avro Ser De" should "be valid for same reader writer schema when writing to file" in {

    val asfsd: AvroSpecificFileSerDe[User, User] = new AvroSpecificFileSerDe[User, User]

    val user: User = new User()
    user.setFirstName(firstName)
    user.setMiddleName(middleName)
    user.setLastName(lastName)


    val file = File.createTempFile("user", ".avro")

    asfsd.ser(Some(user.getSchema), List(user), file)

    val data = asfsd.deser(None, file)

    assert(user.getFirstName.toString == firstName)
    assert(user.getMiddleName.toString == middleName)
    assert(user.getLastName.toString == lastName)


  }

  "Specific Avro Ser De" should "be valid for same reader writer schema when writing to bytes" in {

    val asbasd: AvroSpecificByteArraySerDe[User, User] = new AvroSpecificByteArraySerDe[User , User]

    val user: User = new User()
    user.setFirstName(firstName)
    user.setMiddleName(middleName)
    user.setLastName(lastName)


    val bytes = asbasd.ser(Some(user.getSchema), user)

    val str = new String(bytes)
    val schema = Some(user.getSchema)
    val userDeSer = asbasd.deser(schema, schema, bytes)

    assert(userDeSer.getFirstName.toString == firstName)
    assert(userDeSer.getMiddleName.toString == middleName)
    assert(userDeSer.getLastName.toString == lastName)


  }

  "Specific Avro Ser De" should "be valid for missing reader schema field" in {

    // verifies schema resolution for the scenario that ....
    // if the writer's record contains a field with a name not present
    // in the reader's record, the writer's value for that field is ignored.

    val asbasd = new AvroSpecificByteArraySerDe[UserWithAge, UserWithAge]

    val user = new UserWithAge()
    user.setFirstName(firstName)
    user.setMiddleName(middleName)
    user.setLastName(lastName)
    user.setAge(age)

    val bytes = asbasd.ser(Some(user.getSchema), user)

    val asbasdwa = new AvroSpecificByteArraySerDe[User, User]

    val schema = Some(User.getClassSchema)
    val userWithoutAge = asbasdwa.deser(Some(user.getSchema), schema, bytes)

    assert(userWithoutAge.getFirstName.toString == firstName)
    assert(userWithoutAge.getMiddleName.toString == middleName)
    assert(userWithoutAge.getLastName.toString == lastName)

  }

  "Generic Avro Ser De" should "be valid for new field in reader schema" in {

    // verifies schema resolution for the scenario that ....
    // if the reader's record schema has a field that contains a default value,
    // and writer's schema does not have a field with the same name,
    // then the reader should use the default value from its field.

    def assertUser(data: GenericRecord): Unit = {

      assert(data.get("first_name").toString == firstName)
      assert(data.get("middle_name").toString == middleName)
      assert(data.get("last_name").toString == lastName)
      assert(data.get("age").asInstanceOf[Int] == defaultAge)
    }


    val agbasd:  AvroGenericByteArraySerDe = new AvroGenericByteArraySerDe
    val schemaForUser: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro/user.avsc"))

    val user = new GenericData.Record(schemaForUser)
    user.put("first_name", firstName)
    user.put("middle_name", middleName)
    user.put("last_name", lastName)

    val bytes = agbasd.ser(Some(schemaForUser), user)

    val schemaForUserWithAge: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro/user-with-age.avsc"))

    val data = agbasd.deser(Some(schemaForUser), Some(schemaForUserWithAge), bytes)

    assertUser(data)
  }

  "Generic Avro Ser De" should "not work for new field in reader schema without default value" in {

    // verifies schema resolution for the scenario that ....
    // if the reader's record schema has a field with no default value,
    // and writer's schema does not have a field with the same name, an error is signalled.

    val agbasd:  AvroGenericByteArraySerDe = new AvroGenericByteArraySerDe
    val schemaForUser: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro/user.avsc"))

    val user = new GenericData.Record(schemaForUser)
    user.put("first_name", firstName)
    user.put("middle_name", middleName)
    user.put("last_name", lastName)

    val bytes = agbasd.ser(Some(schemaForUser), user)

    val schemaForUserWithAge: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro/user-with-age.avsc"))

    val data = agbasd.deser(Some(schemaForUser), Some(schemaForUserWithAge), bytes)

  }

  "Generic Avro Ser De" should "be valid for missing filed in reader schema" in {

    // verifies schema resolution for the scenario that ....
    // if the writer's record contains a field with a name not present
    // in the reader's record, the writer's value for that field is ignored.

    def assertUser(data: GenericRecord): Unit = {

      assert(data.get("first_name").toString == firstName)
      assert(data.get("middle_name").toString == middleName)
      assert(data.get("last_name").toString == lastName)
      assert(data.get("age") == null)
    }


    val agbasd:  AvroGenericByteArraySerDe = new AvroGenericByteArraySerDe
    val schemaForUserWithAge: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro/user-with-age.avsc"))


    val user = new GenericData.Record(schemaForUserWithAge)
    user.put("first_name", firstName)
    user.put("middle_name", middleName)
    user.put("last_name", lastName)
    user.put("age", age)

    val bytes = agbasd.ser(Some(schemaForUserWithAge), user)

    val schemaForUser: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro/user.avsc"))

    val data = agbasd.deser(Some(schemaForUserWithAge), Some(schemaForUser), bytes)

    assertUser(data)
  }

  "Json Avro Ser De" should "be valid for aribtrary json" in {

    def assertUser(data: mutable.Map[String, AnyRef]): Unit = {
      assert(data.size == 4)
      assert(data("first_name") == firstName)
      assert(data("middle_name") == middleName)
      assert(data("last_name") == lastName)
      assert(data("age").asInstanceOf[Long] == age)
    }


    val agjbasd:  AvroGenericJsonObjectByteArraySerDe = new AvroGenericJsonObjectByteArraySerDe
    val json = IOUtils.toString(this.getClass.getResourceAsStream("/user.json"), "UTF-8")

    val jsonObj = Json.parseJson(json)
    val bytes = agjbasd.ser(jsonObj)

    val data = agjbasd.deser(bytes)

    assertUser(data)
  }

}