package com.example.workflow.Entities.User;

import com.example.workflow.Entities.BaseEntity;
import com.example.workflow.Entities.Business;
import com.example.workflow.Entities.Division;
import com.example.workflow.Entities.Pole;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name="_user")
public class User  extends BaseEntity implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String email;
    private String password;
    // Other user-related attributes

    @ManyToOne
    @JsonIgnore
    @JsonIdentityReference(alwaysAsId = true)
    private User manager; // Self-referential relationship to represent the manager of a user

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @JsonIgnoreProperties({"businesses",})
    @ManyToOne
    private Division division; // The division to which the user belongs

    @JsonIgnoreProperties({"divisions"})
    @ManyToOne
    private Pole pole; // The division to which the user belongs

    @Column(columnDefinition = "boolean default false")
    private boolean confirmed;

    @Column(columnDefinition = "boolean default false")
    private boolean mailconfirmed;
    private String confirmationToken;

//    notifications


    @Column(columnDefinition = "boolean default true")
    private boolean isActive;

    //    Notifications relatives à la durée
    @Column(columnDefinition = "boolean default false")
    private boolean nearDeadLine;
    @Column(columnDefinition = "boolean default false")
    private boolean deadLines;

    //    Notifications relatives à l'hiérarchie
    @Column(columnDefinition = "boolean default false")
    private boolean notifySysuperior;


    //   Notifications générales
    @Column(columnDefinition = "boolean default false")
    private boolean facturePaidDone;

    @Column(columnDefinition = "boolean default false")
    private boolean newFacutresTovalidate;

//    notifications



//    @JsonIgnoreProperties({"userList","sousTraitances"})
//    @ManyToMany(cascade = CascadeType.MERGE,mappedBy = "userList")
//    private List<Business> myBuinesses;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role != null) {
            return List.of(new SimpleGrantedAuthority(role.name()));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
