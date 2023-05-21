package main.mappers;

import main.entities.IceCream;
import main.models.IceCreamDTO;
import org.mapstruct.Mapper;

@Mapper
public interface IceCreamMapper {

    IceCream iceCreamDtoToIceCream(IceCreamDTO iceCreamDTO);
    IceCreamDTO iceCreamToIceCreamDto(IceCream iceCream);
}
