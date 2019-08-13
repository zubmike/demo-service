package ru.zubmike.service.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zubmike.service.demo.types.Starship;

import java.util.Optional;

@Repository
public interface StarshipRepository extends JpaRepository<Starship, Long> {

	Optional<Starship> findByNumber(String number);

}
