package br.com.sabino.lab.restful.api.repository

import br.com.sabino.lab.restful.api.document.Company
import org.springframework.data.mongodb.repository.MongoRepository

interface CompanyRepository : MongoRepository<Company, String> {
    fun findByCnpj(cnpj: String): Company
}
