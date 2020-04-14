package br.com.sabino.lab.api

import br.com.sabino.lab.api.api.document.Employee
import br.com.sabino.lab.api.api.document.Launch
import br.com.sabino.lab.domain.enum.Profile
import br.com.sabino.lab.domain.enum.TypeLaunch
import br.com.sabino.lab.domain.model.LaunchModel
import br.com.sabino.lab.domain.service.EmployeeService
import br.com.sabino.lab.domain.service.LaunchService
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.kazale.pontointeligente.utils.PasswordUtil

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.text.SimpleDateFormat
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class LaunchControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @MockBean
    private val launchService: LaunchService? = null

    @MockBean
    private val employeeService: EmployeeService? = null

    private val urlBase: String = "/api/releases/"
    private val employeeId: String = "1"
    private val launchId: String = "1"
    private val type: String = TypeLaunch.START_OF_WORK.name
    private val date: Date = Date()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Test
    @Throws(Exception::class)
    @WithMockUser
    fun shouldRegisterRelease() {
        val launch: Launch = shouldObtainTheDataFromAPosting()

        BDDMockito.given<Employee>(employeeService?.findById(employeeId))
                .willReturn(shouldEncryptAnEmployeePassword())
        BDDMockito.given(launchService?.persist(shouldObtainTheDataFromAPosting()))
                .willReturn(launch)

        mvc!!.perform(MockMvcRequestBuilders.post(urlBase)
                .content(shouldGetJsonFromPostRequest())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date.type").value(type))
                .andExpect(jsonPath("$.date.date").value(dateFormat.format(date)))
                .andExpect(jsonPath("$.date.employeeId").value(employeeId))
                .andExpect(jsonPath("$.errors").isEmpty())
    }

    @Test
    @Throws(Exception::class)
    @WithMockUser
    fun shouldThrowAnExceptionForAUserRegistryWithInvalidIdentifier() {
        BDDMockito.given<Employee>(employeeService?.findById(employeeId)).willReturn(null)
        mvc!!.perform(MockMvcRequestBuilders.post(urlBase)
                .content(shouldGetJsonFromPostRequest())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors")
                        .value("Funcionário não encontrado. ID inexistente."))
                .andExpect(jsonPath("$.date").isEmpty())
    }

    @Test
    @Throws(Exception::class)
    @WithMockUser(username = "admin@admin.com", roles = arrayOf("ADMIN"))
    fun shouldRemoveALaunch() {
        BDDMockito.given<Launch>(launchService?.findById(launchId))
                .willReturn(shouldObtainTheDataFromAPosting())
        mvc!!.perform(MockMvcRequestBuilders.delete(urlBase + launchId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
    }

    @Throws(JsonProcessingException::class)
    private fun shouldGetJsonFromPostRequest(): String {
        val launchModel = LaunchModel(dateFormat.format(date), type, "Descrição", "1.234,4.234", employeeId)
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(launchModel)
    }

    private fun shouldObtainTheDataFromAPosting(): Launch =
            Launch(date, TypeLaunch.valueOf(type), employeeId, "Descrição", "1.243,4.345", launchId)

    private fun shouldEncryptAnEmployeePassword(): Employee = Employee("Nome", "email@email.com",
            PasswordUtil().bcryptGenerate("123456"), "23145699876", Profile.ROLE_USER, employeeId)
}
