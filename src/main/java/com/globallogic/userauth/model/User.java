package com.globallogic.userauth.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;

/**
 * Entity representing a User
 *
 * @since 1.0.0
 */
@Entity(name = "users")
public final class User {

    @Id
    @GeneratedValue
    /* change binary column to String */
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    private String name;

    @Column(unique = true)
    @NotBlank(message = "Name should not be null or blank")
    @Email
    private String email;

    @NotBlank(message = "Password should not be null or blank")
    private String password;

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Phone> phones;

    @Column(columnDefinition = "TIMESTAMP", updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime lastLogin;

    @NotNull(message = "isActive should not be null")
    private boolean isActive;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        phones.forEach(p -> p.setUser(this));
        this.phones = phones;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
