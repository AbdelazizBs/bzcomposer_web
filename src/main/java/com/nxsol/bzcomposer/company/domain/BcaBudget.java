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
@Table(name= "bca_budget")
public class BcaBudget {

    @Id
    @Column(name = "BudgetID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer budgetId;

    @Column(name = "EYear")
    private Integer eyear;

    @Column(name = "CompanyBudget")
    private Integer companyBudget;

    @Column(name = "BudgetName", length = 50)
    private String budgetName;

    @Column(name = "Active")
    private Integer active;

    @Column(name = "isDefault")
    private Integer isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID")
    private BcaCompany company;

    @OneToMany(mappedBy = "budget")
    private Set<BcaBudgetdetail> budgetBcaBudgetdetails;

    public Integer getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(final Integer budgetId) {
        this.budgetId = budgetId;
    }

    public Integer getEyear() {
        return eyear;
    }

    public void setEyear(final Integer eyear) {
        this.eyear = eyear;
    }

    public Integer getCompanyBudget() {
        return companyBudget;
    }

    public void setCompanyBudget(final Integer companyBudget) {
        this.companyBudget = companyBudget;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(final String budgetName) {
        this.budgetName = budgetName;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(final Integer active) {
        this.active = active;
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

    public Set<BcaBudgetdetail> getBudgetBcaBudgetdetails() {
        return budgetBcaBudgetdetails;
    }

    public void setBudgetBcaBudgetdetails(final Set<BcaBudgetdetail> budgetBcaBudgetdetails) {
        this.budgetBcaBudgetdetails = budgetBcaBudgetdetails;
    }

}
