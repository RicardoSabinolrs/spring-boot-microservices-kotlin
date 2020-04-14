package br.com.sabino.lab.domain.service.impl

import br.com.sabino.lab.api.api.document.Employee
import br.com.sabino.lab.api.api.repository.EmployeeRepository
import br.com.sabino.lab.api.api.domain.service.EmployeeService
import org.springframework.stereotype.Service

@Service
class EmployeeServiceImpl(val employeeRepository: EmployeeRepository) : EmployeeService {
    override fun persist(employee: Employee): Employee = employeeRepository.save(employee)
    override fun findByCpf(cpf: String): Employee? = employeeRepository.findByCpf(cpf)
    override fun findByEmail(email: String): Employee? = employeeRepository.findByEmail(email)
    override fun findById(id: String): Employee? = employeeRepository.findOne(id)

}
