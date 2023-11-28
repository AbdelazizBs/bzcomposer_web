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
@Table(name= "bca_message")
public class BcaMessage {

    @Id
    @Column(name= "MessageID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

//    @Column(name= "CompanyID")
//    private Integer companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID")
    private BcaCompany company;
    
    
    @Column(name= "Name")
    private String name;

    @Column(name= "Active")
    private Integer active;
    
    @Column(name = "IsDefault")
    private Boolean isDefault;

    @OneToMany(mappedBy = "message")
    private Set<BcaInvoice> messageBcaInvoices;

    @OneToMany(mappedBy = "message")
    private Set<BcaInvoicememorized> messageBcaInvoicememorizeds;

    @OneToMany(mappedBy = "message")
    private Set<StorageInvoice> messageStorageInvoices;

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(final Integer messageId) {
        this.messageId = messageId;
    }

    public BcaCompany getCompany() {
		return company;
	}

	public void setCompany(BcaCompany company) {
		this.company = company;
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

    public Set<BcaInvoice> getMessageBcaInvoices() {
        return messageBcaInvoices;
    }

    public void setMessageBcaInvoices(final Set<BcaInvoice> messageBcaInvoices) {
        this.messageBcaInvoices = messageBcaInvoices;
    }

    public Set<BcaInvoicememorized> getMessageBcaInvoicememorizeds() {
        return messageBcaInvoicememorizeds;
    }

    public void setMessageBcaInvoicememorizeds(
            final Set<BcaInvoicememorized> messageBcaInvoicememorizeds) {
        this.messageBcaInvoicememorizeds = messageBcaInvoicememorizeds;
    }

    public Set<StorageInvoice> getMessageStorageInvoices() {
        return messageStorageInvoices;
    }

    public void setMessageStorageInvoices(final Set<StorageInvoice> messageStorageInvoices) {
        this.messageStorageInvoices = messageStorageInvoices;
    }

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

}
