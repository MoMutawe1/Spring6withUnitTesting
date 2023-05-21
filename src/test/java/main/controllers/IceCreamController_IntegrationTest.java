package main.controllers;

import main.entities.IceCream;
import main.mappers.IceCreamMapper;
import main.models.IceCreamDTO;
import main.repositories.IceCreamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class IceCreamController_IntegrationTest {

    @Autowired
    IceCreamController iceCreamController;

    @Autowired
    IceCreamRepository iceCreamRepository;

    @Autowired
    IceCreamMapper iceCreamMapper;


    @Test
    void testDeleteByIDNotFound() {
        assertThrows(NotFoundException.class, () -> {
            iceCreamController.deleteIceCreamById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
        IceCream beer = iceCreamRepository.findAll().get(0);
        ResponseEntity responseEntity = iceCreamController.deleteIceCreamById(beer.getId());
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Assertions.assertThat(iceCreamRepository.findById(beer.getId()).isEmpty());
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            iceCreamController.updateIceCreamById(UUID.randomUUID(), IceCreamDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingIceCream() {
        IceCream iceCream = iceCreamRepository.findAll().get(0);
        IceCreamDTO iceCreamDTO = iceCreamMapper.iceCreamToIceCreamDto(iceCream);
        iceCreamDTO.setId(null);
        iceCreamDTO.setVersion(null);
        final String iceCreamName = "UPDATED";
        iceCreamDTO.setIceCreamName(iceCreamName);

        ResponseEntity responseEntity = iceCreamController.updateIceCreamById(iceCream.getId(), iceCreamDTO);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        IceCream updatedBeer = iceCreamRepository.findById(iceCream.getId()).get();
        Assertions.assertThat(updatedBeer.getIceCreamName()).isEqualTo(iceCreamName);
    }

    @Rollback
    @Transactional
    @Test
    void saveNewIceCreamTest() {
        IceCreamDTO iceCreamDTO = IceCreamDTO.builder()
                .iceCreamName("New IceCream")
                .build();

        ResponseEntity responseEntity = iceCreamController.addNewIceCream(iceCreamDTO);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        Assertions.assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        IceCream iceCream = iceCreamRepository.findById(savedUUID).get();
        Assertions.assertThat(iceCream).isNotNull();
    }

    @Test
    void testGetById() {
        IceCream iceCream = iceCreamRepository.findAll().get(0);
        IceCreamDTO dto = iceCreamController.getIceCreamById(iceCream.getId());
        Assertions.assertThat(dto).isNotNull();
    }

    @Test
    void testGetAllIceCreams(){
        List<IceCreamDTO> iceCreamDTOList = iceCreamController.getAllIceCreams();
        Assertions.assertThat(iceCreamDTOList.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList(){
        iceCreamRepository.deleteAll();
        List<IceCreamDTO> iceCreamDTOList = iceCreamController.getAllIceCreams();
        Assertions.assertThat(iceCreamDTOList.size()).isEqualTo(0);
    }
}