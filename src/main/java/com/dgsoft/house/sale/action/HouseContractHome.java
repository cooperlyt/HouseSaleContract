package com.dgsoft.house.sale.action;

import com.dgsoft.common.SetLinkList;
import com.dgsoft.common.system.PersonHelper;
import com.dgsoft.developersale.DeveloperLogonInfo;
import com.dgsoft.developersale.DeveloperSaleService;
import com.dgsoft.developersale.LogonInfo;
import com.dgsoft.house.PoolType;
import com.dgsoft.house.sale.ContractOwnerHelper;
import com.dgsoft.house.sale.DeveloperSaleServiceImpl;
import com.dgsoft.house.sale.NumberPool;
import com.dgsoft.house.sale.contract.ContractContextMap;
import com.dgsoft.house.sale.model.*;
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
import org.json.JSONArray;
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

    public ContractOwner.LegalType[] getLegalTypes(){
        return ContractOwner.LegalType.values();
    }

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

    public String commit(){
        DeveloperSaleService.CommitResult result = DeveloperSaleServiceImpl.instance().commitContract((DeveloperLogonInfo)Component.getInstance("logonInfo",true,true),toJson().toString());
        if (DeveloperSaleService.CommitResult.COMMIT_OK.equals(result)){
            return "SUBMITED";
        }
        facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,"commitError_" + result.name());
        return null;
    }

    private JSONObject toJson() {
        JSONObject contractJson = new JSONObject();
        try {
            contractJson.put("id", getInstance().getId());
            contractJson.put("projectId", getInstance().getProjectCode());
            contractJson.put("houseCode", getInstance().getHouseCode());
            contractJson.put("type", getInstance().getType().name());
            contractJson.put("createTime", getInstance().getCreateTime().getTime());
            contractJson.put("attachEmpId", getInstance().getAttachEmpId());
            contractJson.put("attachEmpName", getInstance().getAttachEmpName());
            contractJson.put("contractPrice", getInstance().getPrice());
            contractJson.put("contract", new JSONObject(getInstance().getContext()));
            contractJson.put("contractVersion", getInstance().getContractVersion());
            contractJson.put("poolType", getInstance().getPoolType().name());

            contractJson.put("name", getInstance().getContractOwner().getPersonName());
            contractJson.put("credentialsType", getInstance().getContractOwner().getCredentialsType().name());
            contractJson.put("credentialsNumber", getInstance().getContractOwner().getCredentialsNumber());
            contractJson.put("tel", getInstance().getContractOwner().getPhone());
            contractJson.put("rootAddress", getInstance().getContractOwner().getRootAddress());
            contractJson.put("legalPerson", getInstance().getContractOwner().getLegalPerson());
            contractJson.put("address", getInstance().getContractOwner().getAddress());

            JSONArray numberJsonArray = new JSONArray();
            for (ContractNumber contractNumber : getInstance().getContractNumbers()) {
                numberJsonArray.put(contractNumber.getContractNumber());
            }
            contractJson.put("contractNumber", numberJsonArray);

            if (getInstance().getContractOwner().getLegalType() != null)
                contractJson.put("legalType", getInstance().getContractOwner().getLegalType().name());


            if (!PoolType.SINGLE_OWNER.equals(getInstance().getPoolType())) {
                JSONArray poolArray = new JSONArray();
                for (BusinessPool businessPool : getInstance().getBusinessPools()) {
                    JSONObject poolObj = new JSONObject();
                    poolObj.put("name", businessPool.getPersonName());
                    poolObj.put("credentialsType", businessPool.getCredentialsType().name());
                    poolObj.put("credentialsNumber", businessPool.getCredentialsNumber());
                    poolObj.put("poolArea", businessPool.getPoolArea().doubleValue());
                    poolObj.put("relation", businessPool.getRelation());
                    poolObj.put("perc", businessPool.getPerc());
                    poolObj.put("tel", businessPool.getPhone());
                    poolObj.put("legalPerson", businessPool.getLegalPerson());
                    if (businessPool.getLegalType() != null)
                        poolObj.put("legalType", businessPool.getLegalType().name());
                    poolArray.put(poolObj);
                }
                contractJson.put("pool", poolArray);
            }

            return contractJson;
        }catch (JSONException e){
            throw new IllegalArgumentException(e);
        }
    }
}
