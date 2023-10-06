package com.nxsol.bzcomposer.company.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.persistence.Table;

@Entity
@Table(name= "bca_invoicecredit")
public class BcaInvoicecredit {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "Credit", precision = 23, scale = 4)
    private BigDecimal credit;

    @Column(name= "Total_Credit", precision = 23, scale = 4)
    private BigDecimal totalCredit;

    @Column(name= "Memo")
    private String memo;

    @Column(name= "DateAdded", nullable = false)
    private OffsetDateTime dateAdded;

    @Column(name= "cvId")
    private Integer cvId;

    @Column(name= "Remaining_CreditAmt")
    private Double remainingCreditAmt;

    @Column(name= "Deleted")
    private Integer deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "InvoiceID")
    private BcaInvoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "InvoiceTypeID")
    private BcaInvoicetype invoiceType;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(final BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(final BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(final String memo) {
        this.memo = memo;
    }

    public OffsetDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(final OffsetDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Integer getCvId() {
        return cvId;
    }

    public void setCvId(final Integer cvId) {
        this.cvId = cvId;
    }

    public Double getRemainingCreditAmt() {
        return remainingCreditAmt;
    }

    public void setRemainingCreditAmt(final Double remainingCreditAmt) {
        this.remainingCreditAmt = remainingCreditAmt;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(final Integer deleted) {
        this.deleted = deleted;
    }

    public BcaInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(final BcaInvoice invoice) {
        this.invoice = invoice;
    }

    public BcaInvoicetype getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(final BcaInvoicetype invoiceType) {
        this.invoiceType = invoiceType;
    }

}
