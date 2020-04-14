package com.kazale.pontointeligente.utils

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class PasswordUtil {
    fun bcryptGenerate(password: String): String = BCryptPasswordEncoder().encode(password)
}
