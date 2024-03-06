package com.appswave.io.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

/**
 * Class represents User domain model, as JPA Entity.
 *
 * @author Omar Ismail
 * @version 1.0
 */
@Entity
@Table(name = "users",
        catalog = "NEWSDB",
        schema = "PUBLIC",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"USER_UUID"}),
                @UniqueConstraint(columnNames = {"USERNAME"}),
                @UniqueConstraint(columnNames = {"REFRESH_TOKEN"})
        })
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@ToString
public class User implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 5666668516577592568L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "USER_UUID", columnDefinition = "UUID", nullable = false, updatable = false)
    @ToString.Exclude
    private UUID userUuid;

    @NonNull
    @Basic(optional = false)
    @Column(name = "FIRST_NAME", nullable = false, length = 100)
    private String firstName;

    @NonNull
    @Basic(optional = false)
    @Column(name = "LAST_NAME", nullable = false, length = 100)
    private String lastName;

    @NonNull
    @Basic(optional = false)
    @Column(nullable = false)
    @ToString.Exclude
    private String username;

    @NonNull
    @Basic(optional = false)
    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    @Basic(optional = false)
    @Column(name = "REFRESH_TOKEN", columnDefinition = "UUID")
    private UUID refreshToken;

    @Basic(optional = false)
    @Column(name = "TOKEN_EXPIRY_DATE")
    private Instant tokenExpiryDate;


    @OneToMany(cascade = ALL, fetch = EAGER)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID",
            nullable = false, insertable = false, updatable = false)
    private Set<Role> authorities = new HashSet<>();

    @ToString.Exclude
    private boolean enabled = true;

    public User(Integer id) {
        this.id = id;
    }

    public void setAuthorities(Set<Role> roles) {
        for (Role role : roles) {
            role.setRolePK(new RolePK(this.getId(), role.getAuthority()));
            this.authorities.add(role);
        }
    }

    public void setAuthorities(String... authorities) {
        for (String authority : authorities)
            this.authorities.add(new Role(new RolePK(this.getId(), authority)));
    }

    public String getFullName() {
        return getFirstName()
                .concat(" ")
                .concat(getLastName());
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        User user = (User) o;
        return Objects.equals(this.id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.id, this.userUuid, this.firstName, this.lastName, this.username,
                this.password, this.authorities, this.enabled, this.refreshToken);
    }
}
