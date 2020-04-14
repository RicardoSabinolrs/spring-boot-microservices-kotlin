package br.com.sabino.lab.api.api.security

import br.com.sabino.lab.api.api.document.Employee
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class ChiefEmployee(val employee: Employee) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities: MutableCollection<GrantedAuthority> = mutableListOf()
        authorities.add(SimpleGrantedAuthority(employee.profile.toString()))
        return authorities
    }

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = employee.email

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = employee.password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

}
