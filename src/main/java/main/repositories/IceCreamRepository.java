package main.repositories;

import main.entities.IceCream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface IceCreamRepository extends JpaRepository<IceCream, UUID> {
}
