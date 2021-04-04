package it.lorenzobugiani.springboottestcontainerkotlin

import org.springframework.data.mongodb.repository.MongoRepository

interface EntityRepository : MongoRepository<MongoSampleEntity, String>