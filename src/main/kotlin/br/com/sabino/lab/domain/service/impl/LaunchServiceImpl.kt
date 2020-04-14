package br.com.sabino.lab.domain.service.impl

import br.com.sabino.lab.restful.api.document.Launch
import br.com.sabino.lab.restful.api.domain.service.LaunchService
import br.com.sabino.lab.restful.api.repository.LaunchRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class LaunchServiceImpl(val launchRepository: LaunchRepository) : LaunchService {

    override fun findEmployeeById(employeeId: String, pageRequest: PageRequest): Page<Launch> =
            launchRepository.findByEmployeeId(employeeId, pageRequest)

    override fun findById(id: String): Launch? = launchRepository.findOne(id)
    override fun persist(launch: Launch): Launch = launchRepository.save(launch)
    override fun remove(id: String) = launchRepository.delete(id)

}
