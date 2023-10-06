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
@Table(name="bca_refundreason")
public class BcaRefundreason {

    @Id
    @Column(name="ReasonID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reasonId;

    @Column(name="RefundReason", length = 50)
    private String refundReason;

    @Column(name="Active")
    private Integer active;

    @Column(name="IsDefaultReason")
    private Boolean isDefaultReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID")
    private BcaCompany company;

    @OneToMany(mappedBy = "reason")
    private Set<BcaRmareason> reasonBcaRmareasons;

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(final Integer reasonId) {
        this.reasonId = reasonId;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(final String refundReason) {
        this.refundReason = refundReason;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(final Integer active) {
        this.active = active;
    }

    public Boolean getIsDefaultReason() {
        return isDefaultReason;
    }

    public void setIsDefaultReason(final Boolean isDefaultReason) {
        this.isDefaultReason = isDefaultReason;
    }

    public BcaCompany getCompany() {
        return company;
    }

    public void setCompany(final BcaCompany company) {
        this.company = company;
    }

    public Set<BcaRmareason> getReasonBcaRmareasons() {
        return reasonBcaRmareasons;
    }

    public void setReasonBcaRmareasons(final Set<BcaRmareason> reasonBcaRmareasons) {
        this.reasonBcaRmareasons = reasonBcaRmareasons;
    }

}
