package org.mcq.spring6restmvc.mapper;

import org.mapstruct.Mapper;
import org.mcq.spring6restmvc.entities.Beer;
import org.mcq.spring6restmvc.model.BeerDTO;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);
}
