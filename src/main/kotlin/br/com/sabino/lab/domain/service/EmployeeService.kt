package br.com.sabino.lab.domain.service

import br.com.sabino.lab.api.api.document.Employee

interface EmployeeService {
    fun persist(employee: Employee): Employee
    fun findByCpf(cpf: String): Employee?
    fun findByEmail(email: String): Employee?
    fun findById(id: String): Employee?
}
