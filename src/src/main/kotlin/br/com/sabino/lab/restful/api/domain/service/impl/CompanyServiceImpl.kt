package br.com.sabino.lab.restful.api.domain.service.impl

import br.com.sabino.lab.restful.api.document.Company
import br.com.sabino.lab.restful.api.repository.CompanyRepository
import br.com.sabino.lab.restful.api.domain.service.CompanyService
import org.springframework.stereotype.Service

@Service
class CompanyServiceImpl(val companyRepository: CompanyRepository) : CompanyService {
    override fun findByCnpj(cnpj: String): Company? = companyRepository.findByCnpj(cnpj)
    override fun persist(company: Company): Company = companyRepository.save(company)
}
