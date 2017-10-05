package com.teamtreehouse.todotoday.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class User implements UserDetails{ //So spring security recognizes this class and integrates it in

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) //username must be unique
    @Size(min = 8, max = 20) //can be between 8 and 20 characters in length
    private String username;

    @Size(max = 100)
    private String password;

    @Column(nullable = false) //"enabled" cannot be null in the database
    private boolean enabled;

    @OneToOne //One user has one role
    @JoinColumn(name = "role_id") //Stores the role's id to denote what role the user is
    private Role role;


    @Override //Returns a list of "GrantedAuthority" objects, authority can be a detailed action a user can perform
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }
}
