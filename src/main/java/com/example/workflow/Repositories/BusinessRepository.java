package com.example.workflow.Repositories;

import com.example.workflow.Entities.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    // Alternatively, you can use a custom query method with a JPQL query
//    @Query("SELECT b FROM Business b JOIN b.userList u WHERE u.id = :userId")
//    List<Business> findBusinessesByUserId(@Param("userId") Long userId);
}
