package br.com.sabino.lab.api.api.repository

import br.com.sabino.lab.api.api.document.Employee
import org.springframework.data.mongodb.repository.MongoRepository

interface EmployeeRepository : MongoRepository<Employee, String> {
    fun findByEmail(email: String): Employee
    fun findByCpf(cpf: String): Employee
}
