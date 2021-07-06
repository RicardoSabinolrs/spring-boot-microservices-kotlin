package br.com.sabino.labs.api

import br.com.sabino.labs.domain.repository.BeerRepository
import br.com.sabino.labs.domain.service.BeerService
import br.com.sabino.labs.api.errors.BadRequestAlertException
import br.com.sabino.labs.domain.service.dto.BeerDTO

import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.PaginationUtil
import tech.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

private const val ENTITY_NAME = "sabinoLabsBeer"
/**
 * REST controller for managing [br.com.sabino.labs.domain.Beer].
 */
@RestController
@RequestMapping("/api")
class BeerResource(
    private val beerService: BeerService,
    private val beerRepository: BeerRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "sabinoLabsBeer"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /beers` : Create a new beer.
     *
     * @param beerDTO the beerDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new beerDTO, or with status `400 (Bad Request)` if the beer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beers")
    fun createBeer(@RequestBody beerDTO: BeerDTO): ResponseEntity<BeerDTO> {
        log.debug("REST request to save Beer : $beerDTO")
        if (beerDTO.id != null) {
            throw BadRequestAlertException(
                "A new beer cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = beerService.save(beerDTO)
        return ResponseEntity.created(URI("/api/beers/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id))
            .body(result)
    }

    /**
     * {@code PUT  /beers/:id} : Updates an existing beer.
     *
     * @param id the id of the beerDTO to save.
     * @param beerDTO the beerDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated beerDTO,
     * or with status `400 (Bad Request)` if the beerDTO is not valid,
     * or with status `500 (Internal Server Error)` if the beerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beers/{id}")
    fun updateBeer(
        @PathVariable(value = "id", required = false) id: String,
        @RequestBody beerDTO: BeerDTO
    ): ResponseEntity<BeerDTO> {
        log.debug("REST request to update Beer : {}, {}", id, beerDTO)
        if (beerDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, beerDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }


        if (!beerRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = beerService.save(beerDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, false, ENTITY_NAME,
                     beerDTO.id
                )
            )
            .body(result)
    }

    /**
    * {@code PATCH  /beers/:id} : Partial updates given fields of an existing beer, field will ignore if it is null
    *
    * @param id the id of the beerDTO to save.
    * @param beerDTO the beerDTO to update.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerDTO,
    * or with status {@code 400 (Bad Request)} if the beerDTO is not valid,
    * or with status {@code 404 (Not Found)} if the beerDTO is not found,
    * or with status {@code 500 (Internal Server Error)} if the beerDTO couldn't be updated.
    * @throws URISyntaxException if the Location URI syntax is incorrect.
    */
    @PatchMapping(value = ["/beers/{id}"], consumes = ["application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateBeer(
        @PathVariable(value = "id", required = false) id: String,
        @RequestBody beerDTO:BeerDTO
    ): ResponseEntity<BeerDTO> {
        log.debug("REST request to partial update Beer partially : {}, {}", id, beerDTO)
        if (beerDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, beerDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!beerRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }



            val result = beerService.partialUpdate(beerDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, beerDTO.id)
        )
    }

    /**
     * `GET  /beers` : get all the beers.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of beers in body.
     */
    @GetMapping("/beers")
    fun getAllBeers(pageable: Pageable): ResponseEntity<List<BeerDTO>> {
        log.debug("REST request to get a page of Beers")
        val page = beerService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /beers/:id` : get the "id" beer.
     *
     * @param id the id of the beerDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the beerDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/beers/{id}")
    fun getBeer(@PathVariable id: String): ResponseEntity<BeerDTO> {
        log.debug("REST request to get Beer : $id")
        val beerDTO = beerService.findOne(id)
        return ResponseUtil.wrapOrNotFound(beerDTO)
    }
    /**
     *  `DELETE  /beers/:id` : delete the "id" beer.
     *
     * @param id the id of the beerDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/beers/{id}")
    fun deleteBeer(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Beer : $id")

        beerService.delete(id)
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }
}
