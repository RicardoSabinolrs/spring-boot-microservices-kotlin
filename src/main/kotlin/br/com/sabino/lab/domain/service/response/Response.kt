package br.com.sabino.lab.api.api.response

data class Response<T>(
        val erros: ArrayList<String> = arrayListOf(),
        var data: T? = null
)
