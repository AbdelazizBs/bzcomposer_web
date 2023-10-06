package com.nxsol.bzcomposer.company.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.Set;
import javax.persistence.Table;

@Entity
@Table(name="bca_pricelevel")
public class BcaPricelevel {

    @Id
    @Column(name="PriceLevelID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer priceLevelId;

    @Column(name="Name", length = 50)
    private String name;

    @Column(name="IsActive")
    private Boolean isActive;

    @Column(name="DateAdded")
    private OffsetDateTime dateAdded;

    @Column(name="PriceLevelType", length = 50)
    private String priceLevelType;

    @Column(name="FixedPercentage")
    private Double fixedPercentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID") 
    private BcaCompany company;

    @OneToMany(mappedBy = "priceLevel")
    private Set<StorageClientvendor> priceLevelStorageClientvendors;

    public Integer getPriceLevelId() {
        return priceLevelId;
    }

    public void setPriceLevelId(final Integer priceLevelId) {
        this.priceLevelId = priceLevelId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

    public OffsetDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(final OffsetDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getPriceLevelType() {
        return priceLevelType;
    }

    public void setPriceLevelType(final String priceLevelType) {
        this.priceLevelType = priceLevelType;
    }

    public Double getFixedPercentage() {
        return fixedPercentage;
    }

    public void setFixedPercentage(final Double fixedPercentage) {
        this.fixedPercentage = fixedPercentage;
    }

    public BcaCompany getCompany() {
        return company;
    }

    public void setCompany(final BcaCompany company) {
        this.company = company;
    }

    public Set<StorageClientvendor> getPriceLevelStorageClientvendors() {
        return priceLevelStorageClientvendors;
    }

    public void setPriceLevelStorageClientvendors(
            final Set<StorageClientvendor> priceLevelStorageClientvendors) {
        this.priceLevelStorageClientvendors = priceLevelStorageClientvendors;
    }

}
