package br.com.sabino.lab.restful.api.domain.model

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.NotEmpty

data class EmployeeModel(

        @get:NotEmpty(message = "Nome não pode ser vazio.")
        @get:Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
        val name: String = "",

        @get:NotEmpty(message = "Email não pode ser vazio.")
        @get:Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
        @get:Email(message = "Email inválido.")
        val email: String = "",

        val password: String? = null,
        val hourValue: String? = null,
        val hoursWrked: String? = null,
        val quantitiesOfLunchHours: String? = null,
        val id: String? = null
)
