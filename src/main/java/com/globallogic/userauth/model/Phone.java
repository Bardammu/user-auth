package com.globallogic.userauth.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

/**
 * Entity that represents a phone number
 *
 * @since 1.0.0
 */
@Entity
public class Phone {

    @Id
    @GeneratedValue
    /* change binary column to String */
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @NotNull(message = "Phone number should not be null")
    private long number;

    @NotNull(message = "Phone number city code should not be null")
    private int cityCode;

    @NotNull(message = "Phone number country code should not be null")
    private String countryCode;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
