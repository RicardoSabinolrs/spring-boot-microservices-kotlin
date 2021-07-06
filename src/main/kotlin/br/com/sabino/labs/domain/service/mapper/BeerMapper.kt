package br.com.sabino.labs.domain.service.mapper


import br.com.sabino.labs.domain.entity.Beer
import br.com.sabino.labs.domain.service.dto.BeerDTO
import org.mapstruct.Mapper

/**
 * Mapper for the entity [Beer] and its DTO [BeerDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface BeerMapper :
    EntityMapper<BeerDTO, Beer> {
}
