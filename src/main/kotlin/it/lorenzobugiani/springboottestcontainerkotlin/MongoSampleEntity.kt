package it.lorenzobugiani.springboottestcontainerkotlin

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "sample")
data class MongoSampleEntity(
    @Id val id: String = ObjectId.get().toHexString(),
    val text: String
)