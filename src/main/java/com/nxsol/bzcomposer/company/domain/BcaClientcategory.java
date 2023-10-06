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
import java.util.Set;
import javax.persistence.Table;

@Entity
@Table(name= "bca_clientcategory")
public class BcaClientcategory {

    @Id
    @Column(name = "CVCategoryID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cvcategoryId;

    @Column(name = "Name", length = 50)
    private String name;

    @Column(name = "Active")
    private Integer active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID")
    private BcaCompany company;

    @OneToMany(mappedBy = "cvcategory")
    private Set<StorageClientvendor> cvcategoryStorageClientvendors;

    public Integer getCvcategoryId() {
        return cvcategoryId;
    }

    public void setCvcategoryId(final Integer cvcategoryId) {
        this.cvcategoryId = cvcategoryId;
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

    public BcaCompany getCompany() {
        return company;
    }

    public void setCompany(final BcaCompany company) {
        this.company = company;
    }

    public Set<StorageClientvendor> getCvcategoryStorageClientvendors() {
        return cvcategoryStorageClientvendors;
    }

    public void setCvcategoryStorageClientvendors(
            final Set<StorageClientvendor> cvcategoryStorageClientvendors) {
        this.cvcategoryStorageClientvendors = cvcategoryStorageClientvendors;
    }

}
