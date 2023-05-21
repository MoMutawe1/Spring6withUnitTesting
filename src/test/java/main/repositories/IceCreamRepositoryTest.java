package main.repositories;

import main.entities.IceCream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class IceCreamRepositoryTest {

    @Autowired
    IceCreamRepository iceCreamRepository;

    @Test
    void testSaveIceCream(){
        IceCream savedIceCream = iceCreamRepository.save(IceCream.builder()
                .iceCreamName("2023 IceCream")
                .build());

        assertThat(savedIceCream).isNotNull();
        assertThat(savedIceCream.getId()).isNotNull();
    }
}