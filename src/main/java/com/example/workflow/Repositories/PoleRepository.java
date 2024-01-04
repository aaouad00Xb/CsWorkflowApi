package com.example.workflow.Repositories;

import com.example.workflow.Entities.Pole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PoleRepository extends JpaRepository<Pole, Long> {

    Optional<Pole> findByPoleName(String poleName);
}