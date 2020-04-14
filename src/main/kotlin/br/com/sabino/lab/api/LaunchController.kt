package com.kazale.pontointeligente.controllers

import br.com.sabino.lab.api.api.document.Employee
import br.com.sabino.lab.api.api.document.Launch
import br.com.sabino.lab.api.api.domain.enum.TypeLaunch
import br.com.sabino.lab.api.api.domain.model.LaunchModel
import br.com.sabino.lab.api.api.domain.service.EmployeeService
import br.com.sabino.lab.api.api.domain.service.LaunchService
import br.com.sabino.lab.api.api.response.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import javax.validation.Valid

@RestController
@RequestMapping("/api/releases")
class LaunchController(val launchService: LaunchService, val employeeService: EmployeeService) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Value("\${paginacao.qtd_por_pagina}")
    val qtdPorPagina: Int = 15

    @PostMapping
    fun add(@Valid @RequestBody launchModel: LaunchModel, result: BindingResult): ResponseEntity<Response<LaunchModel>> {
        val response: Response<LaunchModel> = Response()
        validateEmployee(launchModel, result)

        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage)
            return ResponseEntity.badRequest().body(response)
        }

        val launch: Launch = converterDtoParaLancamento(launchModel, result)
        launchService.persist(launch)
        response.data = converterLaunchModel(launch)
        return ResponseEntity.ok(response)
    }

    @GetMapping(value = "/{id}")
    fun listarPorId(@PathVariable("id") id: String): ResponseEntity<Response<LaunchModel>> {
        val response: Response<LaunchModel> = Response()
        val launch: Launch? = launchService.findById(id)

        if (launch == null) {
            response.erros.add("Release not found for id $id")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterLaunchModel(launch)
        return ResponseEntity.ok(response)
    }

    @GetMapping(value = "/employee/{employeeId}")
    fun listEmployeeById(@PathVariable("employeeId") employeeId: String,
                         @RequestParam(value = "pag", defaultValue = "0") pag: Int,
                         @RequestParam(value = "ord", defaultValue = "id") ord: String,
                         @RequestParam(value = "dir", defaultValue = "DESC") dir: String):
            ResponseEntity<Response<Page<LaunchModel>>> {

        val response: Response<Page<LaunchModel>> = Response()
        val pageRequest = PageRequest(pag, qtdPorPagina, Sort.Direction.valueOf(dir), ord)
        val launch: Page<Launch> = launchService.findEmployeeById(employeeId, pageRequest)
        val launchModel: Page<LaunchModel> = launch.map { launch -> converterLaunchModel(launch) }

        response.data = launchModel
        return ResponseEntity.ok(response)
    }

    @PutMapping(value = "/{id}")
    fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody LaunchModel: LaunchModel,
                  result: BindingResult): ResponseEntity<Response<LaunchModel>> {

        val response: Response<LaunchModel> = Response()
        validateEmployee(LaunchModel, result)
        LaunchModel.id = id

        val launch: Launch = converterDtoParaLancamento(LaunchModel, result)
        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage)
            return ResponseEntity.badRequest().body(response)
        }

        launchService.persist(launch)
        response.data = converterLaunchModel(launch)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun remover(@PathVariable("id") id: String): ResponseEntity<Response<String>> {

        val response: Response<String> = Response()
        val lancamento: Launch? = launchService.findById(id)

        if (lancamento == null) {
            response.erros.add("Erro ao remover lançamento. Registro não encontrado para o id $id")
            return ResponseEntity.badRequest().body(response)
        }

        launchService.remove(id)
        return ResponseEntity.ok(Response())
    }

    private fun validateEmployee(LaunchModel: LaunchModel, result: BindingResult) {
        if (LaunchModel.employeeId == null) {
            result.addError(ObjectError("Employee", "Funcionário não informado."))
            return
        }

        val Employee: Employee? = employeeService.findById(LaunchModel.employeeId)
        if (Employee == null) {
            result.addError(ObjectError("Employee", "Funcionário não encontrado. ID inexistente."));
        }
    }

    private fun converterLaunchModel(launch: Launch): LaunchModel =
            LaunchModel(dateFormat.format(launch.date), launch.typeLaunch.toString(),
                    launch.description, launch.location,
                    launch.employeeId, launch.id)

    private fun converterDtoParaLancamento(LaunchModel: LaunchModel,
                                           result: BindingResult): Launch {
        if (LaunchModel.id != null) {
            val lanc: Launch? = launchService.findById(LaunchModel.id!!)
            if (lanc == null) result.addError(ObjectError("lancamento",
                    "Lançamento não encontrado."))
        }

        return Launch(dateFormat.parse(LaunchModel.data), TypeLaunch.valueOf(LaunchModel.type!!),
                LaunchModel.employeeId!!, LaunchModel.description,
                LaunchModel.location, LaunchModel.id)
    }
}
