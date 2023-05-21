package main.repositories;

import main.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
