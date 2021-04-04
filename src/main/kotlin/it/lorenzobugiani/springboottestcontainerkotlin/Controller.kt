package it.lorenzobugiani.springboottestcontainerkotlin

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class Controller(private val repository: EntityRepository) {

    @GetMapping
    @RequestMapping("/{id}")
    fun getEntity(@PathVariable("id") id: String) = repository.findById(id)

    @PostMapping(consumes = [MediaType.TEXT_PLAIN_VALUE])
    fun saveEntity(@RequestBody text: String) = repository.save(MongoSampleEntity(text = text))
}