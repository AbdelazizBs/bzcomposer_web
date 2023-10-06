package com.nxsol.bzcomposer.company.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "city_state_zip")
public class CityStateZip {

    @Id
    @Column(name= "ZIP_CODE", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer zipCode;

    @Column(name= "CITY_NAME", length = 50)
    private String cityName;

    @Column(name= "STATE_NAME", length = 50)
    private String stateName;

    @Column(name= "STATE_CODE", length = 12)
    private String stateCode;

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(final Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(final String stateName) {
        this.stateName = stateName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(final String stateCode) {
        this.stateCode = stateCode;
    }

}
