package br.com.sabino.labs.domain.repository

import br.com.sabino.labs.domain.entity.Beer
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BeerRepository : MongoRepository<Beer, String>
