package br.com.sabino.labs.api

import br.com.sabino.labs.IntegrationTest
import br.com.sabino.labs.domain.entity.Beer
import br.com.sabino.labs.domain.repository.BeerRepository
import br.com.sabino.labs.domain.service.mapper.BeerMapper
import br.com.sabino.labs.api.errors.ExceptionTranslator

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.Extensions
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.validation.Validator
import java.util.UUID

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


/**
 * Integration tests for the [BeerResource] REST controller.
 */
@IntegrationTest
@Extensions(
    ExtendWith(MockitoExtension::class)
)
@AutoConfigureMockMvc
@WithMockUser
class BeerResourceIT  {
    @Autowired
    private lateinit var beerRepository: BeerRepository

    @Autowired
    private lateinit var beerMapper: BeerMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var restBeerMockMvc: MockMvc

    private lateinit var beer: Beer

    @BeforeEach
    fun initTest() {
        beerRepository.deleteAll()
        beer = createEntity()
    }

    @Test
    @Throws(Exception::class)
    fun createBeer() {
        val databaseSizeBeforeCreate = beerRepository.findAll().size

        // Create the Beer
        val beerDTO = beerMapper.toDto(beer)
        restBeerMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(beerDTO))
        ).andExpect(status().isCreated)

        // Validate the Beer in the database
        val beerList = beerRepository.findAll()
        assertThat(beerList).hasSize(databaseSizeBeforeCreate + 1)
        val testBeer = beerList[beerList.size - 1]

        assertThat(testBeer.name).isEqualTo(DEFAULT_NAME)
        assertThat(testBeer.ibu).isEqualTo(DEFAULT_IBU)
        assertThat(testBeer.style).isEqualTo(DEFAULT_STYLE)
        assertThat(testBeer.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testBeer.alcoholTenor).isEqualTo(DEFAULT_ALCOHOL_TENOR)
    }

    @Test
    @Throws(Exception::class)
    fun createBeerWithExistingId() {
        // Create the Beer with an existing ID
        beer.id = "existing_id"
        val beerDTO = beerMapper.toDto(beer)

        val databaseSizeBeforeCreate = beerRepository.findAll().size

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeerMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(beerDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Beer in the database
        val beerList = beerRepository.findAll()
        assertThat(beerList).hasSize(databaseSizeBeforeCreate)
    }



    @Test
    @Throws(Exception::class)
    fun getAllBeers() {
        // Initialize the database
        beerRepository.save(beer)

        // Get all the beerList
        restBeerMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beer.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ibu").value(hasItem(DEFAULT_IBU)))
            .andExpect(jsonPath("$.[*].style").value(hasItem(DEFAULT_STYLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].alcoholTenor").value(hasItem(DEFAULT_ALCOHOL_TENOR)))    }

    @Test
    @Throws(Exception::class)
    fun getBeer() {
        // Initialize the database
        beerRepository.save(beer)

        val id = beer.id
        assertNotNull(id)

        // Get the beer
        restBeerMockMvc.perform(get(ENTITY_API_URL_ID, beer.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(beer.id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ibu").value(DEFAULT_IBU))
            .andExpect(jsonPath("$.style").value(DEFAULT_STYLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.alcoholTenor").value(DEFAULT_ALCOHOL_TENOR))    }
    @Test
    @Throws(Exception::class)
    fun getNonExistingBeer() {
        // Get the beer
        restBeerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    fun putNewBeer() {
        // Initialize the database
        beerRepository.save(beer)

        val databaseSizeBeforeUpdate = beerRepository.findAll().size

        // Update the beer
        val updatedBeer = beerRepository.findById(beer.id).get()
        updatedBeer.name = UPDATED_NAME
        updatedBeer.ibu = UPDATED_IBU
        updatedBeer.style = UPDATED_STYLE
        updatedBeer.description = UPDATED_DESCRIPTION
        updatedBeer.alcoholTenor = UPDATED_ALCOHOL_TENOR
        val beerDTO = beerMapper.toDto(updatedBeer)

        restBeerMockMvc.perform(
            put(ENTITY_API_URL_ID, beerDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(beerDTO))
        ).andExpect(status().isOk)

        // Validate the Beer in the database
        val beerList = beerRepository.findAll()
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate)
        val testBeer = beerList[beerList.size - 1]
        assertThat(testBeer.name).isEqualTo(UPDATED_NAME)
        assertThat(testBeer.ibu).isEqualTo(UPDATED_IBU)
        assertThat(testBeer.style).isEqualTo(UPDATED_STYLE)
        assertThat(testBeer.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testBeer.alcoholTenor).isEqualTo(UPDATED_ALCOHOL_TENOR)
    }

    @Test
    fun putNonExistingBeer() {
        val databaseSizeBeforeUpdate = beerRepository.findAll().size
        beer.id = UUID.randomUUID().toString()

        // Create the Beer
        val beerDTO = beerMapper.toDto(beer)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
            restBeerMockMvc.perform(put(ENTITY_API_URL_ID, beerDTO.id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isBadRequest)

        // Validate the Beer in the database
        val beerList = beerRepository.findAll()
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun putWithIdMismatchBeer() {
        val databaseSizeBeforeUpdate = beerRepository.findAll().size
        beer.id = UUID.randomUUID().toString()

        // Create the Beer
        val beerDTO = beerMapper.toDto(beer)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerMockMvc.perform(
            put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(beerDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Beer in the database
        val beerList = beerRepository.findAll()
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun putWithMissingIdPathParamBeer() {
        val databaseSizeBeforeUpdate = beerRepository.findAll().size
        beer.id = UUID.randomUUID().toString()

        // Create the Beer
        val beerDTO = beerMapper.toDto(beer)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerMockMvc.perform(put(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the Beer in the database
        val beerList = beerRepository.findAll()
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate)
    }


    @Test
    @Throws(Exception::class)
    fun partialUpdateBeerWithPatch() {

        // Initialize the database
beerRepository.save(beer)

val databaseSizeBeforeUpdate = beerRepository.findAll().size

// Update the beer using partial update
val partialUpdatedBeer = Beer().apply {
    id = beer.id


        style = UPDATED_STYLE
        description = UPDATED_DESCRIPTION
        alcoholTenor = UPDATED_ALCOHOL_TENOR
}


restBeerMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedBeer.id)
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedBeer)))
.andExpect(status().isOk)

// Validate the Beer in the database
val beerList = beerRepository.findAll()
assertThat(beerList).hasSize(databaseSizeBeforeUpdate)
val testBeer = beerList.last()
    assertThat(testBeer.name).isEqualTo(DEFAULT_NAME)
    assertThat(testBeer.ibu).isEqualTo(DEFAULT_IBU)
    assertThat(testBeer.style).isEqualTo(UPDATED_STYLE)
    assertThat(testBeer.description).isEqualTo(UPDATED_DESCRIPTION)
    assertThat(testBeer.alcoholTenor).isEqualTo(UPDATED_ALCOHOL_TENOR)
    }

    @Test
    @Throws(Exception::class)
    fun fullUpdateBeerWithPatch() {

        // Initialize the database
beerRepository.save(beer)

val databaseSizeBeforeUpdate = beerRepository.findAll().size

// Update the beer using partial update
val partialUpdatedBeer = Beer().apply {
    id = beer.id


        name = UPDATED_NAME
        ibu = UPDATED_IBU
        style = UPDATED_STYLE
        description = UPDATED_DESCRIPTION
        alcoholTenor = UPDATED_ALCOHOL_TENOR
}


restBeerMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedBeer.id)
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedBeer)))
.andExpect(status().isOk)

// Validate the Beer in the database
val beerList = beerRepository.findAll()
assertThat(beerList).hasSize(databaseSizeBeforeUpdate)
val testBeer = beerList.last()
    assertThat(testBeer.name).isEqualTo(UPDATED_NAME)
    assertThat(testBeer.ibu).isEqualTo(UPDATED_IBU)
    assertThat(testBeer.style).isEqualTo(UPDATED_STYLE)
    assertThat(testBeer.description).isEqualTo(UPDATED_DESCRIPTION)
    assertThat(testBeer.alcoholTenor).isEqualTo(UPDATED_ALCOHOL_TENOR)
    }

    @Throws(Exception::class)
    fun patchNonExistingBeer() {
        val databaseSizeBeforeUpdate = beerRepository.findAll().size
        beer.id = UUID.randomUUID().toString()

        // Create the Beer
        val beerDTO = beerMapper.toDto(beer)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
            restBeerMockMvc.perform(patch(ENTITY_API_URL_ID, beerDTO.id)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isBadRequest)

        // Validate the Beer in the database
        val beerList = beerRepository.findAll()
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun patchWithIdMismatchBeer() {
        val databaseSizeBeforeUpdate = beerRepository.findAll().size
        beer.id = UUID.randomUUID().toString()

        // Create the Beer
        val beerDTO = beerMapper.toDto(beer)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
            restBeerMockMvc.perform(patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isBadRequest)

        // Validate the Beer in the database
        val beerList = beerRepository.findAll()
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamBeer() {
        val databaseSizeBeforeUpdate = beerRepository.findAll().size
        beer.id = UUID.randomUUID().toString()

        // Create the Beer
        val beerDTO = beerMapper.toDto(beer)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
            restBeerMockMvc.perform(patch(ENTITY_API_URL)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the Beer in the database
        val beerList = beerRepository.findAll()
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun deleteBeer() {
        // Initialize the database
        beerRepository.save(beer)

        val databaseSizeBeforeDelete = beerRepository.findAll().size

        // Delete the beer
        restBeerMockMvc.perform(
            delete(ENTITY_API_URL_ID, beer.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val beerList = beerRepository.findAll()
        assertThat(beerList).hasSize(databaseSizeBeforeDelete - 1)
    }


    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_IBU = "AAAAAAAAAA"
        private const val UPDATED_IBU = "BBBBBBBBBB"

        private const val DEFAULT_STYLE = "AAAAAAAAAA"
        private const val UPDATED_STYLE = "BBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private const val DEFAULT_ALCOHOL_TENOR = "AAAAAAAAAA"
        private const val UPDATED_ALCOHOL_TENOR = "BBBBBBBBBB"


        private val ENTITY_API_URL: String = "/api/beers"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"




        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Beer {
            val beer = Beer(

                name = DEFAULT_NAME,

                ibu = DEFAULT_IBU,

                style = DEFAULT_STYLE,

                description = DEFAULT_DESCRIPTION,

                alcoholTenor = DEFAULT_ALCOHOL_TENOR

            )


            return beer
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Beer {
            val beer = Beer(

                name = UPDATED_NAME,

                ibu = UPDATED_IBU,

                style = UPDATED_STYLE,

                description = UPDATED_DESCRIPTION,

                alcoholTenor = UPDATED_ALCOHOL_TENOR

            )


            return beer
        }

    }
}
