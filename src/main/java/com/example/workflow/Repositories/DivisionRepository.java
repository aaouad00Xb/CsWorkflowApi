package com.example.workflow.Repositories;

import com.example.workflow.Entities.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {

    @Query(value = "SELECT * FROM division s WHERE s.pole_poleid = :pole_id ",nativeQuery = true)
    List<Division> findByPoleId(@Param("pole_id") Long pole_id);

}