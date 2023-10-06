package com.nxsol.bzcomposer.company.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name= "bca_lineofcreditterm")
public class BcaLineofcreditterm {

    @Id
    @Column(name= "CreditTermId", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer creditTermId;

    @Column(name= "Name")
    private String name;

    @Column(name= "Active")
    private Integer active;

    @Column(name= "Days")
    private Integer days;

    @Column(name= "isDefault")
    private Integer isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID")
    private BcaCompany company;

    public Integer getCreditTermId() {
        return creditTermId;
    }

    public void setCreditTermId(final Integer creditTermId) {
        this.creditTermId = creditTermId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(final Integer active) {
        this.active = active;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(final Integer days) {
        this.days = days;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(final Integer isDefault) {
        this.isDefault = isDefault;
    }

    public BcaCompany getCompany() {
        return company;
    }

    public void setCompany(final BcaCompany company) {
        this.company = company;
    }

}
