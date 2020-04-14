package br.com.sabino.lab.restful.api.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Company(
        val socialName: String,
        val cnpj: String,
        @Id val id: String? = null
)
