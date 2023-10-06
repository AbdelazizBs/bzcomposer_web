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
@Table(name= "bcp_deductionlist")
public class BcpDeductionlist {

    @Id
    @Column(name= "DeductionListID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deductionListId;

    @Column(name= "DeductionList", length = 50)
    private String deductionList;

    @Column(name= "DeductionAmount")
    private Integer deductionAmount;

    @Column(name= "DeductionRate")
    private Integer deductionRate;

    @Column(name= "UseRate")
    private Boolean useRate;

    @Column(name= "IsTaxExempt")
    private Boolean isTaxExempt;

    @Column(name= "Active")
    private Integer active;

    @Column(name= "DateAdded", nullable = false)
    private OffsetDateTime dateAdded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID")
    private BcaCompany company;

    public Integer getDeductionListId() {
        return deductionListId;
    }

    public void setDeductionListId(final Integer deductionListId) {
        this.deductionListId = deductionListId;
    }

    public String getDeductionList() {
        return deductionList;
    }

    public void setDeductionList(final String deductionList) {
        this.deductionList = deductionList;
    }

    public Integer getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(final Integer deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public Integer getDeductionRate() {
        return deductionRate;
    }

    public void setDeductionRate(final Integer deductionRate) {
        this.deductionRate = deductionRate;
    }

    public Boolean getUseRate() {
        return useRate;
    }

    public void setUseRate(final Boolean useRate) {
        this.useRate = useRate;
    }

    public Boolean getIsTaxExempt() {
        return isTaxExempt;
    }

    public void setIsTaxExempt(final Boolean isTaxExempt) {
        this.isTaxExempt = isTaxExempt;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(final Integer active) {
        this.active = active;
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
