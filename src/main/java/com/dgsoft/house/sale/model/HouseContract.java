package com.dgsoft.house.sale.model;

import com.dgsoft.house.PoolType;
import com.dgsoft.house.SalePayType;
import com.dgsoft.house.SaleType;
import com.dgsoft.house.sale.contract.ContractInfo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by cooper on 9/14/15.
 */
@Entity
@Table(name = "HOUSE_CONTRACT", catalog = "CONTRACT")
public class HouseContract implements java.io.Serializable, ContractInfo {

    public enum ContractStatus{
        PREPARE, SUBMIT, RECORD
    }

    private String id;
    private String projectCode;
    private String groupId;
    private String houseCode;
    private SaleType type;

    private Date createTime;
    private ContractStatus status;
    private String attachEmpId;
    private String attachEmpName;
    private BigDecimal price;
    private String context;
    private String houseDescription;
    private int contractVersion;
    private int version;

    private PoolType poolType;
    private SalePayType salePayType;

    private ContractOwner contractOwner;
    private Set<BusinessPool> businessPools = new HashSet<BusinessPool>(0);
    private Set<ContractNumber> contractNumbers = new HashSet<ContractNumber>(0);

    private SaleProxyPerson saleProxyPerson;

    public HouseContract() {
    }

    public HouseContract(String id,String groupId, Date createTime, ContractStatus status, String attachEmpId, String attachEmpName, PoolType poolType) {
        this.id = id;
        this.groupId = groupId;
       // this.projectCode = projectCode;
        this.createTime = createTime;
        this.status = status;
        this.attachEmpId = attachEmpId;
        this.attachEmpName = attachEmpName;
        this.poolType = poolType;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "GROUP_ID" , nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    @Column(name = "PROJECT_ID",nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getProjectCode() {
        return projectCode;
    }

    @Override
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Column(name = "HOUSE_CODE", nullable = false,length = 32, unique = true)
    @NotNull
    @Size(max = 32)
    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE",length = 20, nullable = false)
    @NotNull
    public SaleType getType() {
        return type;
    }

    @Override
    public void setType(SaleType type) {
        this.type = type;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME",nullable = false)
    @NotNull
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20, nullable = false)
    @NotNull
    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    @Column(name = "ATTACH_EMP_ID", nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getAttachEmpId() {
        return attachEmpId;
    }

    public void setAttachEmpId(String attachEmpId) {
        this.attachEmpId = attachEmpId;
    }

    @Column(name ="ATTACH_EMP_NAME", nullable = false, length = 50)
    public String getAttachEmpName() {
        return attachEmpName;
    }

    public void setAttachEmpName(String attachEmpName) {
        this.attachEmpName = attachEmpName;
    }

    @Column(name = "CONTRACT_PRICE", nullable = false, length = 21, scale = 3)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTRACT", columnDefinition = "LONGTEXT")
    public String getContext() {
        return context;
    }

    @Override
    public void setContext(String context) {
        this.context = context;
    }

    @Override
    @Column(name = "CONTRACT_VERSION", nullable = false)
    public int getContractVersion() {
        return contractVersion;
    }

    @Override
    public void setContractVersion(int contractVersion) {
        this.contractVersion = contractVersion;
    }

    @Version
    @Column(name = "VERSION",nullable = false)
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public ContractOwner getContractOwner() {
        return contractOwner;
    }

    public void setContractOwner(ContractOwner contractOwner) {
        this.contractOwner = contractOwner;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "POOL_TYPE", length = 32, nullable = false)
    @NotNull
    public PoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(PoolType poolType) {
        this.poolType = poolType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "SALE_PAY_TYPE", nullable = false, length = 32)
    @NotNull
    public SalePayType getSalePayType() {
        return salePayType;
    }

    public void setSalePayType(SalePayType salePayType) {
        this.salePayType = salePayType;
    }

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "houseContract")
    public Set<BusinessPool> getBusinessPools() {
        return businessPools;
    }

    public void setBusinessPools(Set<BusinessPool> businessPools) {
        this.businessPools = businessPools;
    }

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "houseContract")
    public Set<ContractNumber> getContractNumbers() {
        return contractNumbers;
    }

    public void setContractNumbers(Set<ContractNumber> contractNumbers) {
        this.contractNumbers = contractNumbers;
    }

    @Column(name = "HOUSE_DESCRIPTION", nullable = false, length = 52)
    @NotNull
    @Size(max = 52)
    public String getHouseDescription() {
        return houseDescription;
    }

    public void setHouseDescription(String houseDescription) {
        this.houseDescription = houseDescription;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public SaleProxyPerson getSaleProxyPerson() {
        return saleProxyPerson;
    }

    public void setSaleProxyPerson(SaleProxyPerson saleProxyPerson) {
        this.saleProxyPerson = saleProxyPerson;
    }

    @Transient
    public List<ContractNumber> getContractNumberList(){
        List<ContractNumber> result = new ArrayList<ContractNumber>(getContractNumbers());
        Collections.sort(result, new Comparator<ContractNumber>() {
            @Override
            public int compare(ContractNumber o1, ContractNumber o2) {
                return o1.getContractNumber().compareTo(o2.getContractNumber());
            }
        });
        return result;
    }
}
