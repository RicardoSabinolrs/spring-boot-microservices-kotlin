package br.com.sabino.lab.domain.service

import br.com.sabino.lab.restful.api.document.Launch
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface LaunchService {

    fun findEmployeeById(employeeId: String, pageRequest: PageRequest): Page<Launch>

    fun findById(id: String): Launch?

    fun persist(launch: Launch): Launch

    fun remove(id: String)
}
