package main.bootstrap;

import lombok.RequiredArgsConstructor;
import main.entities.Customer;
import main.entities.IceCream;
import main.models.IceCreamStyle;
import main.repositories.CustomerRepository;
import main.repositories.IceCreamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final IceCreamRepository iceCreamRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadIceCreamData();
        loadCustomerData();
    }

    private void loadIceCreamData() {
        if (iceCreamRepository.count() == 0){
            IceCream iceCream1 = IceCream.builder()
                    .iceCreamName("Arabic IceCream")
                    .iceCreamStyle(IceCreamStyle.CustardIceCream)
                    .upc("12356")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(122)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            IceCream iceCream2 = IceCream.builder()
                    .iceCreamName("Jordan IceCream")
                    .iceCreamStyle(IceCreamStyle.PhiladelphiaStyle)
                    .upc("12356222")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(392)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            IceCream iceCream3 = IceCream.builder()
                    .iceCreamName("Sunshine City")
                    .iceCreamStyle(IceCreamStyle.SoftServeIceCream)
                    .upc("12356")
                    .price(new BigDecimal("13.99"))
                    .quantityOnHand(144)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            iceCreamRepository.save(iceCream1);
            iceCreamRepository.save(iceCream2);
            iceCreamRepository.save(iceCream3);
        }

    }

    private void loadCustomerData() {

        if (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name("Customer 1")
                    .version(1)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Customer customer2 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name("Customer 2")
                    .version(1)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Customer customer3 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name("Customer 3")
                    .version(1)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
        }
    }
}