package br.com.sabino.labs.domain.service

import br.com.sabino.labs.domain.entity.Beer
import br.com.sabino.labs.domain.repository.BeerRepository
import br.com.sabino.labs.domain.service.dto.BeerDTO
import br.com.sabino.labs.domain.service.mapper.BeerMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

/**
 * Service Implementation for managing [Beer].
 */
@Service
class BeerService(private val beerRepository: BeerRepository, private val beerMapper: BeerMapper) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a beer.
     *
     * @param beerDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(beerDTO: BeerDTO): BeerDTO {
        log.debug("Request to save Beer : $beerDTO")
        var beer = beerMapper.toEntity(beerDTO)
        beer = beerRepository.save(beer)
        return beerMapper.toDto(beer)
    }

    /**
     * Partially updates a beer.
     *
     * @param beerDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(beerDTO: BeerDTO): Optional<BeerDTO> {
        log.debug("Request to partially update Beer : {}", beerDTO)
        return beerRepository.findById(beerDTO.id)
            .map {
                beerMapper.partialUpdate(it, beerDTO)
                it
            }
            .map { beerRepository.save(it) }
            .map { beerMapper.toDto(it) }

    }

    /**
     * Get all the beers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<BeerDTO> {
        log.debug("Request to get all Beers")
        return beerRepository.findAll(pageable)
            .map(beerMapper::toDto)
    }


    /**
     * Get one beer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<BeerDTO> {
        log.debug("Request to get Beer : $id")
        return beerRepository.findById(id)
            .map(beerMapper::toDto)
    }

    /**
     * Delete the beer by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String): Unit {
        log.debug("Request to delete Beer : $id")
        beerRepository.deleteById(id)
    }
}
