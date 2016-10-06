package com.dgsoft.house.sale.model;

import com.dgsoft.common.system.OwnerPersonEntity;
import com.dgsoft.common.system.PersonEntity;
import com.dgsoft.common.system.Sex;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by cooper on 9/15/15.
 */

@Entity
@Table(name = "BUSINESS_POOL", catalog = "CONTRACT")
public class BusinessPool implements OwnerPersonEntity, java.io.Serializable {

    public enum LegalType{
        LEGAL_OWNER,LEGAL_MANAGER
    }

    public enum ContractPersonType{
        BUYER,SELLER
    }


    private String id;
    private String personName;
    private PersonEntity.CredentialsType credentialsType;
    private String credentialsNumber;
    private String relation;
    private BigDecimal poolArea;
    private BigDecimal poolPerc;

    private String legalPerson;
    private HouseContract houseContract;
    private LegalType legalType;

    private String phone;
    private String rootAddress;
    private String address;

    private Date birthday;
    private Sex sex;
    private String postCode;

    private String fingerprint;

    private String paperCopyA;
    private String paperCopyB;
    private String paperCopyInfo;

    private int pri;
    private ContractPersonType contractPersonType;

    private PowerProxyPerson powerProxyPerson;

    public BusinessPool() {
    }

    public BusinessPool(HouseContract houseContract, ContractPersonType contractPersonType, int pri) {
        this.houseContract = houseContract;
        this.contractPersonType = contractPersonType;
        this.pri = pri;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Column(name = "RELATION", length = 32)
    @Size(max = 32)
    public String getRelation() {
        return this.relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Column(name = "POOL_AREA", scale = 4)
    public BigDecimal getPoolArea() {
        return this.poolArea;
    }

    public void setPoolArea(BigDecimal poolArea) {
        this.poolArea = poolArea;
    }

    @Column(name = "PERC", scale = 4)
    public BigDecimal getPoolPerc() {
        return this.poolPerc;
    }

    public void setPoolPerc(BigDecimal perc) {
        this.poolPerc = perc;
    }

    @Column(name = "PHONE", nullable = true, length = 15)
    @Size(max = 15)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    @Column(name = "LEGAL_PERSON", nullable = true, length = 50)
    @Size(max = 50)
    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONTRACT", nullable = false)
    @NotNull
    public HouseContract getHouseContract() {
        return houseContract;
    }

    public void setHouseContract(HouseContract houseContract) {
        this.houseContract = houseContract;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "LEGAL_TYPE",length = 20)
    public LegalType getLegalType() {
        return legalType;
    }

    public void setLegalType(LegalType legalType) {
        this.legalType = legalType;
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

    @Column(name = "PRI", nullable = false)
    public int getPri() {
        return pri;
    }

    public void setPri(int pri) {
        this.pri = pri;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE",nullable = false,length = 16)
    @NotNull
    public ContractPersonType getContractPersonType() {
        return contractPersonType;
    }

    public void setContractPersonType(ContractPersonType contractPersonType) {
        this.contractPersonType = contractPersonType;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    public PowerProxyPerson getPowerProxyPerson() {
        return powerProxyPerson;
    }

    public void setPowerProxyPerson(PowerProxyPerson powerProxyPerson) {
        this.powerProxyPerson = powerProxyPerson;
    }


    @Column(name = "FINGERPRINT",length = 1024)
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
