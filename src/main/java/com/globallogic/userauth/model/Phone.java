package com.globallogic.userauth.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Entity that represents a phone number
 *
 * @since 1.0.0
 */
@Entity
public class Phone {

    @Id
    private long id;

    @NotNull(message = "user id should not be null")
    private UUID userId;

    @NotNull(message = "Phone number should not be null")
    private long number;

    @NotNull(message = "Phone number city code should not be null")
    private int cityCode;

    @NotNull(message = "Phone number country code should not be null")
    private String countryCode;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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
}
