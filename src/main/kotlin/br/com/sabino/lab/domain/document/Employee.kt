package br.com.sabino.lab.api.api.document

import br.com.sabino.lab.api.api.domain.enum.Profile
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Employee(
        val name: String,
        val email: String,
        val password: String,
        val cpf: String,
        val profile: Profile,
        val companyId: String,
        val hourValue: Double? = 0.0,
        val amountOfHoursWorkedPerDay: Float? = 0.0f,
        val quantitiesOfLunchHours: Float? = 0.0f,
        @Id val id: String? = null
)
