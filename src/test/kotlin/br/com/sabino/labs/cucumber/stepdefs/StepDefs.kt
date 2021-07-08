package br.com.sabino.labs.cucumber.stepdefs

import org.springframework.test.web.servlet.ResultActions

abstract class StepDefs {
    protected var actions: ResultActions? = null
}
