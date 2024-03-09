package com.example.workflow.Repositories;

import com.example.workflow.Entities.ContratSousTraitance;
import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Entities.User.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContratSoustraitanceRepo  extends JpaRepository<ContratSousTraitance, Long> {
    @Query("SELECT f FROM ContratSousTraitance f WHERE f.business.businessID = :businessID AND f.soustraitant.id = :soustraitantId")
    List<ContratSousTraitance> findByBusinessIDAndSousTraitantId(@Param("businessID") Long businessID, @Param("soustraitantId") Long soustraitantId);



    @Query("SELECT f FROM ContratSousTraitance f WHERE f.division.pole.poleID = :poleID AND f.valid = false AND f.currentStep.userRole = 'DP' ")
    List<ContratSousTraitance> findInvalidContratByPole(@Param("poleID") Long poleID);



    @Query("SELECT f FROM ContratSousTraitance f WHERE f.valid = false")
    List<ContratSousTraitance> findInvalidContrat();

    @Query("SELECT f FROM ContratSousTraitance f WHERE f.division.pole.poleID = :poleID  ")
    List<ContratSousTraitance> findContratByPole(@Param("poleID") Long poleID);

    @Query("SELECT f FROM ContratSousTraitance f WHERE f.division.pole.poleID = :poleID AND f.valid = false AND f.currentStep.userRole != 'DP' ")
    List<ContratSousTraitance> findInvalidContratByPoleAilleur(@Param("poleID") Long poleID);

    @Query("SELECT f FROM ContratSousTraitance f WHERE f.division.pole.poleID = :poleID AND f.valid = true")
    List<ContratSousTraitance> findInvalidContratByPoleAilleurValid(@Param("poleID") Long poleID);


    List<ContratSousTraitance> findContratSousTraitancesByValidIsFalseAndDivisionAndCurrentStep_UserRole(Division d , UserRole r);
    List<ContratSousTraitance> findContratSousTraitancesByValidIsFalseAndDivision(Division d );
    List<ContratSousTraitance> findContratSousTraitancesByValidIsFalseAndProjectManagerAndCurrentStep_UserRole(User r, UserRole ole);
    List<ContratSousTraitance> findContratSousTraitancesByValidIsFalseAndDivisionAndCurrentStep_UserRoleIsNot(Division d, UserRole r);
    List<ContratSousTraitance> findContratSousTraitancesByValidIsTrueAndDivision(Division d);
    List<ContratSousTraitance> findContratSousTraitancesByValidIsFalseAndCurrentStep_UserRole(UserRole r);
    List<ContratSousTraitance> findContratSousTraitancesByValidIsTrue();
    int countByValid(boolean b);
    int countByCurrentStepStepID(Long id);
    int countByValidFalseAndCurrentStepStepIDNot(Long id);

    @Query("SELECT SUM(c.totalAmount) FROM ContratSousTraitance c")
    Optional<Double> sumTotalAmount();



//    List<ContratSousTraitance> findContratSousTraitancesByValidIsFalseAndNotCurrentStep_UserRole(UserRole r);
    List<ContratSousTraitance> findContratSousTraitancesByValidIsFalseAndCurrentStep_UserRoleIsNot(UserRole r);
}

