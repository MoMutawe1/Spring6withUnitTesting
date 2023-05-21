package main.services;

import lombok.extern.slf4j.Slf4j;
import main.models.IceCreamDTO;
import main.models.IceCreamStyle;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class IceCreamServiceImpl implements IceCreamService{

        private Map<UUID, IceCreamDTO> iceCreamMap;

        public IceCreamServiceImpl() {
            log.debug("IceCreamServiceImpl got construct.");
            iceCreamMap = new HashMap<>();

            IceCreamDTO iceCream1 =
                    IceCreamDTO.builder()
                            .id(UUID.randomUUID())
                            .version(1)
                            .iceCreamName("Soft Serve")
                            .iceCreamStyle(IceCreamStyle.SoftServeIceCream)
                            .upc("123456")
                            .quantityOnHand(150)
                            .price(new BigDecimal("2.99"))
                            .createdDate(LocalDateTime.now())
                            .updateDate(LocalDateTime.now())
                            .build();

            IceCreamDTO iceCream2 =
                    IceCreamDTO.builder()
                            .id(UUID.randomUUID())
                            .version(1)
                            .iceCreamName("Philadelphia Style")
                            .iceCreamStyle(IceCreamStyle.PhiladelphiaStyle)
                            .upc("223344")
                            .quantityOnHand(120)
                            .price(new BigDecimal("1.99"))
                            .createdDate(LocalDateTime.now())
                            .updateDate(LocalDateTime.now())
                            .build();

            IceCreamDTO iceCream3 =
                    IceCreamDTO.builder()
                            .id(UUID.randomUUID())
                            .version(1)
                            .iceCreamName("Custard IceCream")
                            .iceCreamStyle(IceCreamStyle.CustardIceCream)
                            .upc("567890")
                            .quantityOnHand(180)
                            .price(new BigDecimal("1.49"))
                            .createdDate(LocalDateTime.now())
                            .updateDate(LocalDateTime.now())
                            .build();

            iceCreamMap.put(iceCream1.getId(), iceCream1);
            iceCreamMap.put(iceCream2.getId(), iceCream2);
            iceCreamMap.put(iceCream3.getId(), iceCream3);
        }

    @Override
    public Optional<IceCreamDTO> getIceCreamById(UUID id) {
        log.debug("Get IceCreamById in IceCreamServiceImpl was called.");
        return Optional.of(iceCreamMap.get(id));
    }

    @Override
    public List<IceCreamDTO> getAllIceCreams(){
        log.debug("Get All Ice Creams in IceCreamServiceImpl was called.");
        return new ArrayList<>(iceCreamMap.values());
    }

    @Override
    public IceCreamDTO saveNewIceCream(IceCreamDTO iceCream) {
            IceCreamDTO newIceCream = IceCreamDTO.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .iceCreamName(iceCream.getIceCreamName())
                    .iceCreamStyle(iceCream.getIceCreamStyle())
                    .upc(iceCream.getUpc())
                    .quantityOnHand(iceCream.getQuantityOnHand())
                    .price(iceCream.getPrice())
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();
        iceCreamMap.put(newIceCream.getId(), newIceCream);
        return newIceCream;
    }

    @Override
    public Optional<IceCreamDTO> updateIceCreamById(UUID id, IceCreamDTO iceCream) {
            IceCreamDTO updateIceCream = iceCreamMap.get(id);
            updateIceCream.setIceCreamName(iceCream.getIceCreamName());
            updateIceCream.setUpc(iceCream.getUpc());
            updateIceCream.setQuantityOnHand(iceCream.getQuantityOnHand());
            updateIceCream.setPrice(iceCream.getPrice());

            return Optional.of(updateIceCream);
    }

    @Override
    public Optional<IceCreamDTO> patchIceCreamById(UUID iceCreamId, IceCreamDTO iceCream) {
        IceCreamDTO patchIceCream = iceCreamMap.get(iceCreamId);

        if(StringUtils.hasText(iceCream.getIceCreamName()))
            patchIceCream.setIceCreamName(iceCream.getIceCreamName());

        if(iceCream.getIceCreamStyle() != null)
            patchIceCream.setIceCreamStyle(iceCream.getIceCreamStyle());

        if(StringUtils.hasText(iceCream.getUpc()))
            patchIceCream.setUpc(iceCream.getUpc());

        if(iceCream.getPrice() != null)
            patchIceCream.setPrice(iceCream.getPrice());

        if(iceCream.getQuantityOnHand() != null)
            patchIceCream.setQuantityOnHand(iceCream.getQuantityOnHand());

        return Optional.of(patchIceCream);
    }

    @Override
    public Boolean deleteIceCreamById(UUID id) {
        iceCreamMap.remove(id);
        return true;
    }
}
