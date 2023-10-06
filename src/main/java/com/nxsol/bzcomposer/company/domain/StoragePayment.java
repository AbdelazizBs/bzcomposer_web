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
@Table(name= "storage_payment")
public class StoragePayment {

    @Id
    @Column(name= "PaymentID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @Column(name= "Amount")
    private Double amount;

    @Column(name= "PayerID")
    private Integer payerId;

    @Column(name= "PayeeID")
    private Integer payeeId;

    @Column(name= "AccountID")
    private Integer accountId;

    @Column(name= "InvoiceID")
    private Integer invoiceId;

    @Column(name= "CategoryID")
    private Integer categoryId;

    @Column(name= "CompanyID")
    private Integer companyId;

    @Column(name= "PayFromBalance")
    private Double payFromBalance;

    @Column(name= "PayToBalance")
    private Double payToBalance;

    @Column(name= "DateAdded", nullable = false)
    private OffsetDateTime dateAdded;

    @Column(name= "IsToBePrinted")
    private Boolean isToBePrinted;

    @Column(name= "isNeedtoDeposit")
    private Boolean isNeedtoDeposit;

    @Column(name= "CheckNumber", length = 50)
    private String checkNumber;

    @Column(name= "Deleted")
    private Boolean deleted;

    @Column(name= "PayableID")
    private Integer payableId;

    @Column(name= "RmaNo")
    private Integer rmaNo;

    @Column(name= "RmaItemID")
    private Integer rmaItemId;

    @Column(name= "TransactionID", length = 50)
    private String transactionId;

    @Column(name= "BillNum")
    private Integer billNum;

    @Column(name= "Priority")
    private Integer priority;

    @Column(name= "TransactionType")
    private Integer transactionType;

    @Column(name= "AccountCategoryID")
    private Integer accountCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PaymentTypeID")
    private BcaPaymenttype paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClientVendorID")
    private StorageClientvendor clientVendor;

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(final Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    public Integer getPayerId() {
        return payerId;
    }

    public void setPayerId(final Integer payerId) {
        this.payerId = payerId;
    }

    public Integer getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(final Integer payeeId) {
        this.payeeId = payeeId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(final Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(final Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(final Integer companyId) {
        this.companyId = companyId;
    }

    public Double getPayFromBalance() {
        return payFromBalance;
    }

    public void setPayFromBalance(final Double payFromBalance) {
        this.payFromBalance = payFromBalance;
    }

    public Double getPayToBalance() {
        return payToBalance;
    }

    public void setPayToBalance(final Double payToBalance) {
        this.payToBalance = payToBalance;
    }

    public OffsetDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(final OffsetDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Boolean getIsToBePrinted() {
        return isToBePrinted;
    }

    public void setIsToBePrinted(final Boolean isToBePrinted) {
        this.isToBePrinted = isToBePrinted;
    }

    public Boolean getIsNeedtoDeposit() {
        return isNeedtoDeposit;
    }

    public void setIsNeedtoDeposit(final Boolean isNeedtoDeposit) {
        this.isNeedtoDeposit = isNeedtoDeposit;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(final String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getPayableId() {
        return payableId;
    }

    public void setPayableId(final Integer payableId) {
        this.payableId = payableId;
    }

    public Integer getRmaNo() {
        return rmaNo;
    }

    public void setRmaNo(final Integer rmaNo) {
        this.rmaNo = rmaNo;
    }

    public Integer getRmaItemId() {
        return rmaItemId;
    }

    public void setRmaItemId(final Integer rmaItemId) {
        this.rmaItemId = rmaItemId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getBillNum() {
        return billNum;
    }

    public void setBillNum(final Integer billNum) {
        this.billNum = billNum;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(final Integer priority) {
        this.priority = priority;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(final Integer transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getAccountCategoryId() {
        return accountCategoryId;
    }

    public void setAccountCategoryId(final Integer accountCategoryId) {
        this.accountCategoryId = accountCategoryId;
    }

    public BcaPaymenttype getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(final BcaPaymenttype paymentType) {
        this.paymentType = paymentType;
    }

    public StorageClientvendor getClientVendor() {
        return clientVendor;
    }

    public void setClientVendor(final StorageClientvendor clientVendor) {
        this.clientVendor = clientVendor;
    }

}
