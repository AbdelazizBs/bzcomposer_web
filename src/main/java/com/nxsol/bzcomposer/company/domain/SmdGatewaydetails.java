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
@Table(name= "smd_gatewaydetails")
public class SmdGatewaydetails {

    @Id
    @Column(name= "GatewayID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gatewayId;

    @Column(name= "GatewayType", length = 50)
    private String gatewayType;

    @Column(name= "Field1", length = 50)
    private String field1;

    @Column(name= "Field2", length = 150)
    private String field2;

    @Column(name= "Field3", length = 50)
    private String field3;

    @Column(name= "Field4", length = 75)
    private String field4;

    @Column(name= "Field5", length = 50)
    private String field5;

    @Column(name= "Field6", length = 50)
    private String field6;

    @Column(name= "Field7", length = 50)
    private String field7;

    @Column(name= "Field8", length = 50)
    private String field8;

    @Column(name= "Field9", length = 50)
    private String field9;

    @Column(name= "Field10", length = 50)
    private String field10;

    @Column(name= "isDefault")
    private Boolean isDefault;

    @Column(name= "TestEnv")
    private Boolean testEnv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID")
    private BcaCompany company;

    public Integer getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(final Integer gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(final String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(final String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(final String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(final String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(final String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(final String field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(final String field6) {
        this.field6 = field6;
    }

    public String getField7() {
        return field7;
    }

    public void setField7(final String field7) {
        this.field7 = field7;
    }

    public String getField8() {
        return field8;
    }

    public void setField8(final String field8) {
        this.field8 = field8;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(final String field9) {
        this.field9 = field9;
    }

    public String getField10() {
        return field10;
    }

    public void setField10(final String field10) {
        this.field10 = field10;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(final Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getTestEnv() {
        return testEnv;
    }

    public void setTestEnv(final Boolean testEnv) {
        this.testEnv = testEnv;
    }

    public BcaCompany getCompany() {
        return company;
    }

    public void setCompany(final BcaCompany company) {
        this.company = company;
    }

}
