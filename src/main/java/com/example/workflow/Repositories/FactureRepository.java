//package com.example.workflow.Repositories;
//
//import com.example.workflow.Entities.ContratSousTraitance;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface FactureRepository  extends JpaRepository<ContratSousTraitance, Long> {
//
////    @Query("SELECT f FROM ContratSousTraitance f WHERE f.business.businessID = :businessID AND f.sousTraitance.id = :soustraitantId")
////    List<ContratSousTraitance> findByBusinessIDAndSousTraitantId(@Param("businessID") Long businessID, @Param("soustraitantId") Long soustraitantId);
//
//
//
////    @Query("SELECT f FROM ContratSousTraitance f WHERE f.sousTraitance.id = :soustraitantId")
////    List<ContratSousTraitance> findBySousTraitanceId(@Param("soustraitantId") Long soustraitantId);
//
//
//}
