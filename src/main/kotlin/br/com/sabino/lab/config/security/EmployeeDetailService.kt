package br.com.sabino.lab.api.api.security

import br.com.sabino.lab.api.api.domain.service.EmployeeService
import br.com.sabino.lab.api.api.document.Employee
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class EmployeeDetailService(val employeeService: EmployeeService) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username != null) {
            val employee: Employee? = employeeService.findByEmail(username)
            if (employee != null) {
                return ChiefEmployee(employee)
            }
        }
        throw UsernameNotFoundException(username)
    }

}
