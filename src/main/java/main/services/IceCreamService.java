package main.services;

import main.models.IceCreamDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IceCreamService {
    Optional<IceCreamDTO> getIceCreamById(UUID id);
    List<IceCreamDTO> getAllIceCreams();
    IceCreamDTO saveNewIceCream(IceCreamDTO iceCream);
    Optional<IceCreamDTO> updateIceCreamById(UUID id, IceCreamDTO iceCream);
    Optional<IceCreamDTO> patchIceCreamById(UUID iceCreamId, IceCreamDTO iceCream);
    Boolean deleteIceCreamById(UUID id);

}
