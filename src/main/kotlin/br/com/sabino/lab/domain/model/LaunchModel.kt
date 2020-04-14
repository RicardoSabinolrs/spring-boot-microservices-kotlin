package br.com.sabino.lab.domain.model

import org.hibernate.validator.constraints.NotEmpty

data class LaunchModel(

        @get:NotEmpty(message = "Data não pode ser vazia.")
        val data: String? = null,

        @get:NotEmpty(message = "Tipo não pode ser vazio.")
        val type: String? = null,

        val description: String? = null,
        val location: String? = null,
        val employeeId: String? = null,
        var id: String? = null
)
