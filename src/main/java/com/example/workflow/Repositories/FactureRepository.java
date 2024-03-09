package com.example.workflow.Repositories;

import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.Facture;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Entities.User.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureRepository  extends JpaRepository<Facture, Long> {

//    @Query("SELECT f FROM ContratSousTraitance f WHERE f.business.businessID = :businessID AND f.sousTraitance.id = :soustraitantId")
//    List<ContratSousTraitance> findByBusinessIDAndSousTraitantId(@Param("businessID") Long businessID, @Param("soustraitantId") Long soustraitantId);



//    @Query("SELECT f FROM ContratSousTraitance f WHERE f.sousTraitance.id = :soustraitantId")
//    List<ContratSousTraitance> findBySousTraitanceId(@Param("soustraitantId") Long soustraitantId);



    List<Facture> findFacturesByValidFalseAndContratSousTraitance_DivisionAndCurrentStep_UserRole(Division d , UserRole r);


    List<Facture> findFacturesByValidFalseAndContratSousTraitance_DivisionAndCurrentStep_StepID(Division d ,Long id);


    List<Facture> findFacturesByAndContratSousTraitance_Division(Division d );

    List<Facture> findFacturesByContratSousTraitance_Division(Division d);


    List<Facture> findFacturesByValidFalseAndContratSousTraitance_ProjectManagerAndCurrentStep_UserRole(User r, UserRole ole);




    List<Facture> findFacturesByValidIsFalseAndCurrentStep_UserRoleIsNot(UserRole r);

    @Query("SELECT f FROM Facture f WHERE f.contratSousTraitance.division.pole.poleID = :poleID AND f.valid = false AND f.currentStep.userRole != 'DP' ")
    List<Facture> findInvalidContratByPoleAilleur(@Param("poleID") Long poleID);

    @Query("SELECT f FROM Facture f WHERE f.contratSousTraitance.division.pole.poleID = :poleID AND f.valid = true")
    List<Facture> findInvalidContratByPoleAilleurValid(@Param("poleID") Long poleID);




    @Query("SELECT f FROM Facture f WHERE f.valid = false ")
    List<Facture> findInvalidFacture();




    @Query("SELECT f FROM Facture f WHERE f.contratSousTraitance.division.pole.poleID = :poleID AND f.valid = false AND f.currentStep.userRole = 'DP' ")
    List<Facture> findInvalidContratByPole(@Param("poleID") Long poleID);




    List<Facture> findFactureByValidIsTrueAndContratSousTraitance_Division(Division d);
    List<Facture> findFacturesByValidIsFalseAndCurrentStep_UserRole(UserRole r);
    List<Facture> findFacturesByValidIsFalse();
    List<Facture> findFacturesByValidIsTrue();
    List<Facture> findFacturesByValidIsFalseAndContratSousTraitance_DivisionAndCurrentStep_UserRoleIsNot(Division d, UserRole r);



}
