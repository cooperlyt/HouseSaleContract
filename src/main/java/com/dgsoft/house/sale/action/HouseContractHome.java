package com.dgsoft.house.sale.action;

import com.dgsoft.common.SetLinkList;
import com.dgsoft.common.system.PersonHelper;
import com.dgsoft.developersale.LogonInfo;
import com.dgsoft.house.PoolType;
import com.dgsoft.house.sale.ContractOwnerHelper;
import com.dgsoft.house.sale.NumberPool;
import com.dgsoft.house.sale.contract.ContractContextMap;
import com.dgsoft.house.sale.model.BusinessPool;
import com.dgsoft.house.sale.model.ContractOwner;
import com.dgsoft.house.sale.model.ContractTemplate;
import com.dgsoft.house.sale.model.HouseContract;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Credentials;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cooper on 9/15/15.
 */
@Name("houseContractHome")
public class HouseContractHome extends EntityHome<HouseContract> {

    @In
    private FacesMessages facesMessages;

    private List<PersonHelper<BusinessPool>> housePoolList;

    private ContractOwnerHelper contractOwner;

    private ContractContextMap contractContextMap;

    private int poolOwnerCount = 0;

    private String contractNumber;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    @Override
    protected HouseContract createInstance(){

        LogonInfo logonInfo = (LogonInfo) Component.getInstance("logonInfo", ScopeType.SESSION);
        return new HouseContract(NumberPool.instance().genContractNumber(),
                logonInfo.getGroupCode(),new Date(),
                HouseContract.ContractStatus.PREPARE,logonInfo.getUserId(),logonInfo.getEmployeeName(), PoolType.SINGLE_OWNER);
    }

    private List<ContractTemplate> contractTemplateList;

    public List<ContractTemplate> getContractTemplateList() {
        if (contractTemplateList == null){
            LogonInfo logonInfo = (LogonInfo) Component.getInstance("logonInfo", ScopeType.SESSION);
            contractTemplateList = getEntityManager().createQuery("select contract from ContractTemplate contract where contract.type = :contractType and contract.projectCode = :groupCode",ContractTemplate.class)
                    .setParameter("groupCode",logonInfo.getGroupCode()).setParameter("contractType", getInstance().getType()).getResultList();
        }
        return contractTemplateList;
    }

    private void fillContextMap() throws JSONException {
        if (getInstance().getContext() == null || getInstance().getContext().trim().equals("")){
            contractContextMap = new ContractContextMap();
        }else{
            contractContextMap = new ContractContextMap(new JSONObject(getInstance().getContext()));
        }
    }

    @Override
    protected void initInstance(){
        super.initInstance();
        ContractOwner owner = getInstance().getContractOwner();
        if (owner == null){
            owner = new ContractOwner(getInstance());
            getInstance().setContractOwner(owner);
        }
        contractOwner = new ContractOwnerHelper(owner);

        try {
            fillContextMap();
        } catch (JSONException e) {
            Logging.getLog(getClass()).error("load contract context error.", e);
            throw new IllegalArgumentException("load contract context error.");
        }

        housePoolList = new ArrayList<PersonHelper<BusinessPool>>(getInstance().getBusinessPools().size());
        for(BusinessPool pool: getInstance().getBusinessPools()){
            housePoolList.add(new PersonHelper<BusinessPool>(pool));
        }
    }

    public List<PersonHelper<BusinessPool>> getContractPoolOwners() {
        if (housePoolList == null){
            getInstance();
        }
        return housePoolList;
    }

    public ContractOwnerHelper getContractOwnerHelper(){
        if (contractOwner == null){
            getInstance();
        }
        return contractOwner;
    }

    private void savePoolOwner(){
        getInstance().getBusinessPools().clear();
        for(PersonHelper<BusinessPool> pool: housePoolList){
            getInstance().getBusinessPools().add(pool.getPersonEntity());
        }
    }

    @Override
    @Transactional
    public String persist(){
        savePoolOwner();
        if (contractContextMap != null){
            try {
                getInstance().setContext(contractContextMap.toJson().toString());
            } catch (JSONException e) {
                facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,"ContractContextFail");
                return null;
            }
        }

        return super.persist();
    }


    @Override
    @Transactional
    public String update(){
        savePoolOwner();
        if (contractContextMap != null){
            try {
                getInstance().setContext(contractContextMap.toJson().toString());
            } catch (JSONException e) {
                facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,"ContractContextFail");
                return null;
            }
        }

        return super.update();
    }

    public int getPoolOwnerCount() {
        return poolOwnerCount;
    }

    public void setPoolOwnerCount(int poolOwnerCount) {
        this.poolOwnerCount = poolOwnerCount;
    }

    public PoolType getPoolType(){
        return getInstance().getPoolType();
    }

    public void setPoolType(PoolType poolType){
        if (PoolType.SINGLE_OWNER.equals(poolType)){
            setPoolOwnerCount(0);
        }
        getInstance().setPoolType(poolType);
    }


    public void setContext(String context) throws JSONException {
        getInstance().setContext(context);
        fillContextMap();
    }

    public void clearContext(){
        getInstance().setContext(null);
        contractContextMap = new ContractContextMap();
    }

    public String genPoolOwner(){

        while (getContractPoolOwners().size() != poolOwnerCount){
            if (getContractPoolOwners().size() > getPoolOwnerCount()){
                getInstance().getBusinessPools().remove(getContractPoolOwners().remove(0).getPersonEntity());
            }else{
                BusinessPool pool = new BusinessPool(getInstance());
                getContractPoolOwners().add(new PersonHelper<BusinessPool>(pool));
                getInstance().getBusinessPools().add(pool);
            }
        }
        return "pool-owner-ok";
    }

    public ContractContextMap getContractContextMap() {
        if (contractContextMap == null){
            getInstance();
        }
        return contractContextMap;
    }

    public String viewSingleContract(){

        return "view-contract-" + getInstance().getType().getPatchByVersion(getInstance().getContractVersion());

    }
}
