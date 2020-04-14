package br.com.sabino.lab.api.api.repository

import br.com.sabino.lab.api.api.document.Launch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface LaunchRepository : MongoRepository<Launch, String> {
    fun findByEmployeeId(employeeId: String, pageable: Pageable): Page<Launch>
}
