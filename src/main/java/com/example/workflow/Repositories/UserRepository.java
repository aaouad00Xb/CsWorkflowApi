package com.example.workflow.Repositories;

import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.Pole;
import com.example.workflow.Entities.StepField_r;
import com.example.workflow.Entities.User.User;
import com.example.workflow.Entities.User.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByRoleAndDivision(UserRole role, Division division);

    List<User> findByRoleAndDivisionDivisionId(UserRole role, Long divisionId);


    List<User> findByRoleAndDivision_DivisionId(UserRole role, Long divisionId);
    List<User> findByRoleAndPole_PoleID(UserRole role, Long poleId);


    List<User> findByRoleAndPole(UserRole role, Pole p );

    List<User> findByRole(UserRole role);




    Optional<User> findByIdOrderByCreatedAtDesc(Long aLong);


    Optional<List<User>> findByDivision(Division d);


    Optional<User> findByEmail(String email);
    Optional<User> findUserByUsername(String username);

    // Add your custom query to check if a user with a specific username exists
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = ?1")
    boolean existsByUsername(String username);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = ?1")
    boolean existsByEmail(String email);

    User findByConfirmationToken(String confirmationToken);


    List<User> findByConfirmedFalseAndMailconfirmedTrue();
    List<User> findByConfirmedTrueAndMailconfirmedTrue();


}
