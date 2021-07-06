package br.com.sabino.labs.domain.service.dto

import java.io.Serializable
import java.util.*

/**
 * A DTO for the [br.com.sabino.labs.domain.Beer] entity.
 */
data class BeerDTO(
    var id: String? = null,
    var name: String? = null,
    var ibu: String? = null,
    var style: String? = null,
    var description: String? = null,
    var alcoholTenor: String? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BeerDTO) return false
        val beerDTO = other
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, beerDTO.id);
    }

    override fun hashCode() = Objects.hash(this.id)
}
