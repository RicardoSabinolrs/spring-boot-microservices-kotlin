package br.com.sabino.lab.domain.service.impl

import br.com.sabino.lab.api.api.document.Company
import br.com.sabino.lab.api.api.repository.CompanyRepository
import br.com.sabino.lab.api.api.domain.service.CompanyService
import org.springframework.stereotype.Service

@Service
class CompanyServiceImpl(val companyRepository: CompanyRepository) : CompanyService {
    override fun findByCnpj(cnpj: String): Company? = companyRepository.findByCnpj(cnpj)
    override fun persist(company: Company): Company = companyRepository.save(company)
}
