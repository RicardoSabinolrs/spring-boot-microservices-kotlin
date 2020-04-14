package br.com.sabino.lab.restful.api.domain.model

data class CompanyModel(
        val socialName: String,
        val cnpj: String,
        val id: String? = null
)
