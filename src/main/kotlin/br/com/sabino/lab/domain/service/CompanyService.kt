package br.com.sabino.lab.domain.service

import br.com.sabino.lab.restful.api.document.Company

interface CompanyService {
    fun findByCnpj(cnpj: String): Company?
    fun persist(company: Company): Company
}
