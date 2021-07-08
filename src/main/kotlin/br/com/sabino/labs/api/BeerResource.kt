package br.com.sabino.labs.api

import br.com.sabino.labs.domain.service.BeerService
import br.com.sabino.labs.domain.service.dto.BeerDTO
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.PaginationUtil
import tech.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException

@RestController
@RequestMapping("/api")
class BeerResource(private val beerService: BeerService) {

    private val log = LoggerFactory.getLogger(javaClass)
    private var applicationName: String = "sabinoLabsBeer"

    companion object {
        const val ENTITY_NAME = "sabinoLabsBeer"
    }

    @PostMapping("/beers")
    fun createBeer(@RequestBody beerDTO: BeerDTO): ResponseEntity<BeerDTO> {
        log.debug("REST request to save Beer : $beerDTO")

        val result = beerService.save(beerDTO)
        return ResponseEntity.created(URI("/api/beers/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id))
            .body(result)
    }

    @PutMapping("/beers/{id}")
    fun updateBeer(@PathVariable(value = "id", required = false) id: String, @RequestBody beerDTO: BeerDTO): ResponseEntity<BeerDTO> {
        log.debug("REST request to update Beer : {}, {}", id, beerDTO)

        val result = beerService.save(beerDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, false, ENTITY_NAME,
                    beerDTO.id
                )
            ).body(result)
    }

    @PatchMapping(value = ["/beers/{id}"], consumes = ["application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateBeer(@PathVariable(value = "id", required = false) id: String, @RequestBody beerDTO: BeerDTO): ResponseEntity<BeerDTO> {
        log.debug("REST request to partial update Beer partially : {}, {}", id, beerDTO)

        val result = beerService.partialUpdate(beerDTO)
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, beerDTO.id)
        )
    }

    @GetMapping("/beers")
    fun getAllBeers(pageable: Pageable): ResponseEntity<List<BeerDTO>> {
        log.debug("REST request to get a page of Beers")

        val page = beerService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @GetMapping("/beers/{id}")
    fun getBeer(@PathVariable id: String): ResponseEntity<BeerDTO> {
        log.debug("REST request to get Beer : $id")
        val beerDTO = beerService.findOne(id)
        return ResponseUtil.wrapOrNotFound(beerDTO)
    }

    @DeleteMapping("/beers/{id}")
    fun deleteBeer(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Beer : $id")

        beerService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }
}
