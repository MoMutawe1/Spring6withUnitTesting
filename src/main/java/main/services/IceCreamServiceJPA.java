package main.services;

import lombok.RequiredArgsConstructor;
import main.mappers.IceCreamMapper;
import main.models.IceCreamDTO;
import main.repositories.IceCreamRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class IceCreamServiceJPA implements IceCreamService{
    private final IceCreamRepository iceCreamRepository;
    private final IceCreamMapper iceCreamMapper;

    @Override
    public Optional<IceCreamDTO> getIceCreamById(UUID id) {
        return Optional.ofNullable(iceCreamMapper.iceCreamToIceCreamDto(iceCreamRepository.findById(id).orElse(null)));
    }

    @Override
    public List<IceCreamDTO> getAllIceCreams() {
        return iceCreamRepository.findAll()
                .stream()
                .map(iceCreamMapper::iceCreamToIceCreamDto)
                .collect(Collectors.toList());
    }

    @Override
    public IceCreamDTO saveNewIceCream(IceCreamDTO iceCream) {
        return iceCreamMapper.iceCreamToIceCreamDto(iceCreamRepository.save(iceCreamMapper.iceCreamDtoToIceCream(iceCream)));
    }

    @Override
    public Optional<IceCreamDTO> updateIceCreamById(UUID iceCreamId, IceCreamDTO iceCream) {
        AtomicReference<Optional<IceCreamDTO>> atomicReference = new AtomicReference<>();

        iceCreamRepository.findById(iceCreamId).ifPresentOrElse(foundBeer -> {
            foundBeer.setIceCreamName(iceCream.getIceCreamName());
            foundBeer.setIceCreamStyle(iceCream.getIceCreamStyle());
            foundBeer.setUpc(iceCream.getUpc());
            foundBeer.setPrice(iceCream.getPrice());
            atomicReference.set(Optional.of(iceCreamMapper
                    .iceCreamToIceCreamDto(iceCreamRepository.save(foundBeer))));
            }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Optional<IceCreamDTO> patchIceCreamById(UUID iceCreamId, IceCreamDTO iceCream) {
        AtomicReference<Optional<IceCreamDTO>> atomicReference = new AtomicReference<>();

        iceCreamRepository.findById(iceCreamId).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(iceCream.getIceCreamName())){
                foundBeer.setIceCreamName(iceCream.getIceCreamName());
            }
            if (iceCream.getIceCreamStyle() != null){
                foundBeer.setIceCreamStyle(iceCream.getIceCreamStyle());
            }
            if (StringUtils.hasText(iceCream.getUpc())){
                foundBeer.setUpc(iceCream.getUpc());
            }
            if (iceCream.getPrice() != null){
                foundBeer.setPrice(iceCream.getPrice());
            }
            if (iceCream.getQuantityOnHand() != null){
                foundBeer.setQuantityOnHand(iceCream.getQuantityOnHand());
            }
            atomicReference.set(Optional.of(iceCreamMapper
                    .iceCreamToIceCreamDto(iceCreamRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteIceCreamById(UUID iceCreamId) {
        if (iceCreamRepository.existsById(iceCreamId)) {
            iceCreamRepository.deleteById(iceCreamId);
            return true;
        }
        return false;
    }
}
