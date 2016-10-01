package com.dgsoft.house.sale.model;

import com.dgsoft.common.system.PersonEntity;
import com.dgsoft.common.system.Sex;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by cooper on 9/27/16.
 */
@Entity
@Table(name = "POWER_PROXY_PERSON", catalog = "CONTRACT")
public class PowerProxyPerson implements PersonEntity, java.io.Serializable {

    public enum ProxyType{
        ENTRUSTED,LEGAL
    }

    private String id;
    private ProxyType proxyType;

    private String personName;
    private PersonEntity.CredentialsType credentialsType;
    private String credentialsNumber;

    private String phone;
    private String rootAddress;
    private String address;

    private BusinessPool businessPool;

    private Date birthday;
    private Sex sex;
    private String postCode;

    private String fingerprint;

    private String paperCopyA;
    private String paperCopyB;
    private String paperCopyInfo;

    public PowerProxyPerson() {
    }

    public PowerProxyPerson(BusinessPool businessPool) {
        this.businessPool = businessPool;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    @GenericGenerator(name = "pkGenerator",
            strategy = "foreign",
            parameters = { @org.hibernate.annotations.Parameter(name = "property", value = "businessPool") })
    @GeneratedValue(generator = "pkGenerator")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public BusinessPool getBusinessPool() {
        return businessPool;
    }

    public void setBusinessPool(BusinessPool businessPool) {
        this.businessPool = businessPool;
    }

    @Column(name = "NAME", nullable = false, length = 50)
    @NotNull
    @Size(max = 50)
    @Override
    public String getPersonName() {
        return this.personName;
    }

    @Override
    public void setPersonName(String name) {
        this.personName = name;
    }

    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "ID_TYPE", nullable = false, length = 32)
    @NotNull
    public PersonEntity.CredentialsType getCredentialsType() {
        return credentialsType;
    }

    @Override
    public void setCredentialsType(PersonEntity.CredentialsType credentialsType) {
        this.credentialsType = credentialsType;
    }

    @Override
    @Column(name = "ID_NO", nullable = false, length = 100)
    @NotNull
    @Size(max = 100)
    public String getCredentialsNumber() {
        return credentialsNumber;
    }

    @Override
    public void setCredentialsNumber(String cerdentialsNumber) {
        this.credentialsNumber = cerdentialsNumber;
    }


    @Column(name = "PHONE", nullable = true, length = 15)
    @Size(max = 15)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "ROOT_ADDRESS", length = 50)
    @Size(max = 50)
    public String getRootAddress() {
        return rootAddress;
    }

    public void setRootAddress(String rootAddress) {
        this.rootAddress = rootAddress;
    }

    @Column(name = "ADDRESS",length = 200)
    @Size(max = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE",nullable = false,length = 16)
    @NotNull
    public ProxyType getProxyType() {
        return proxyType;
    }

    public void setProxyType(ProxyType proxyType) {
        this.proxyType = proxyType;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTHDAY")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "SEX",length = 10)
    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Column(name = "POST_CODE",length = 9)
    @Size(max = 9)
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Column(name = "FINGERPRINT",length = 512)
    @Size(max = 512)
    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    @Column(name = "PAPER_COPY_A", length = 32)
    @Size(max = 32)
    public String getPaperCopyA() {
        return paperCopyA;
    }

    public void setPaperCopyA(String paperCopyA) {
        this.paperCopyA = paperCopyA;
    }

    @Column(name = "PAPER_COPY_B", length = 32)
    @Size(max = 32)
    public String getPaperCopyB() {
        return paperCopyB;
    }

    public void setPaperCopyB(String paperCopyB) {
        this.paperCopyB = paperCopyB;
    }

    @Column(name = "PAPER_COPY_INFO", length = 1024)
    public String getPaperCopyInfo() {
        return paperCopyInfo;
    }

    public void setPaperCopyInfo(String paperCopyInfo) {
        this.paperCopyInfo = paperCopyInfo;
    }
}
