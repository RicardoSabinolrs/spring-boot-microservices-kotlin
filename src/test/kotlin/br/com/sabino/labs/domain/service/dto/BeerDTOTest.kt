package br.com.sabino.labs.domain.service.dto

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import br.com.sabino.labs.api.equalsVerifier


class BeerDTOTest {

    @Test
    fun dtoEqualsVerifier(){
        equalsVerifier(BeerDTO::class)
        val beerDTO1 = BeerDTO()
        beerDTO1.id = "id1"
        val beerDTO2 = BeerDTO()
        assertThat(beerDTO1).isNotEqualTo(beerDTO2)
        beerDTO2.id = beerDTO1.id
        assertThat(beerDTO1).isEqualTo(beerDTO2)
        beerDTO2.id = "id2"
        assertThat(beerDTO1).isNotEqualTo(beerDTO2)
        beerDTO1.id = null
        assertThat(beerDTO1).isNotEqualTo(beerDTO2)
    }
}
