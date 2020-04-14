package br.com.sabino.lab.restful.api.document

import br.com.sabino.lab.restful.api.domain.enum.TypeLaunch
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Launch(
        val date: Date,
        val typeLaunch: TypeLaunch,
        val employeeId: String,
        val description: String? = "",
        val location: String? = "",
        @Id val id: String? = null
)
