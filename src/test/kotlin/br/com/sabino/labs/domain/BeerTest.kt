package br.com.sabino.labs.domain

import br.com.sabino.labs.domain.entity.Beer
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import br.com.sabino.labs.api.equalsVerifier

class BeerTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Beer::class)
        val beer1 = Beer()
        beer1.id = "id1"
        val beer2 = Beer()
        beer2.id = beer1.id
        assertThat(beer1).isEqualTo(beer2)
        beer2.id = "id2"
        assertThat(beer1).isNotEqualTo(beer2)
        beer1.id = null
        assertThat(beer1).isNotEqualTo(beer2)
    }
}
