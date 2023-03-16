package com.globallogic.userauth.dto;

import java.util.Objects;

/**
 * POJO that represents a phone on a registration request or response
 *
 * @since 1.0.0
 */
public class PhoneDto {

    private final long number;

    private final int cityCode;

    private final String countryCode;

    public PhoneDto(long number, int cityCode, String countrycode) {
        this.number = number;
        this.cityCode = cityCode;
        this.countryCode = countrycode;
    }

    public long getNumber() {
        return number;
    }

    public int getCityCode() {
        return cityCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhoneDto)) {
            return false;
        }
        PhoneDto that = (PhoneDto) o;
        return number == that.number &&
               cityCode == that.cityCode &&
               Objects.equals(countryCode, that.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, cityCode, countryCode);
    }
}
