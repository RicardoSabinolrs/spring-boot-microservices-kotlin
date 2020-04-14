package br.com.sabino.lab.restful.api.response

data class Response<T>(
        val erros: ArrayList<String> = arrayListOf(),
        var data: T? = null
)
