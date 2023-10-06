package com.nxsol.bzcomposer.company.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;
import javax.persistence.Table;

@Entity
@Table(name= "bca_apmemorizedingroup")
public class BcaApmemorizedingroup {

    @Id
    @Column(name = "MemorizeID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memorizeId;

    @Column(name = "BillingGroupID", nullable = false)
    private Integer billingGroupId;

    @Column(name = "DateAdded")
    private OffsetDateTime dateAdded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID")
    private BcaCompany company;

    public Integer getMemorizeId() {
        return memorizeId;
    }

    public void setMemorizeId(final Integer memorizeId) {
        this.memorizeId = memorizeId;
    }

    public Integer getBillingGroupId() {
        return billingGroupId;
    }

    public void setBillingGroupId(final Integer billingGroupId) {
        this.billingGroupId = billingGroupId;
    }

    public OffsetDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(final OffsetDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public BcaCompany getCompany() {
        return company;
    }

    public void setCompany(final BcaCompany company) {
        this.company = company;
    }

}
