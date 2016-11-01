package com.dgsoft.house.sale.action;

import com.dgsoft.common.system.PersonHelper;
import com.dgsoft.common.system.RunParam;
import com.dgsoft.developersale.DeveloperLogonInfo;
import com.dgsoft.developersale.DeveloperSaleService;
import com.dgsoft.developersale.LogonInfo;
import com.dgsoft.house.OwnerShareCalcType;
import com.dgsoft.house.PoolType;
import com.dgsoft.house.sale.DeveloperSaleServiceImpl;
import com.dgsoft.house.sale.NumberPool;
import com.dgsoft.house.sale.PowerPersonHelper;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by cooper on 9/15/15.
 */
@Name("houseContractHome")
public class HouseContractHome extends EntityHome<HouseContract> {

    public BusinessPool.LegalType[] getLegalTypes() {
        return BusinessPool.LegalType.values();
    }


    @In
    private FacesMessages facesMessages;

    private List<PowerPersonHelper> housePoolList;

    private ContractContextMap contractContextMap;

    private PersonHelper<SaleProxyPerson> proxyPersonHelper;

    private OwnerShareCalcType ownerShareCalcType;

    private int poolOwnerCount = 1;

    private String contractNumber;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public PersonHelper<SaleProxyPerson> getProxyPersonHelper() {
        if (proxyPersonHelper == null) {
            getInstance();
        }
        return proxyPersonHelper;
    }

    public OwnerShareCalcType getOwnerShareCalcType() {
        switch (RunParam.instance().getIntParamValue("OWNER_SHARE_CALC_TYPE")) {
            case 1:
                return OwnerShareCalcType.SCALE;
            case 2:
                return OwnerShareCalcType.AREA;
            default:
                return ownerShareCalcType;
        }

    }

    public void setOwnerShareCalcType(OwnerShareCalcType ownerShareCalcType) {
        this.ownerShareCalcType = ownerShareCalcType;
    }

    @Override
    protected HouseContract createInstance() {

        LogonInfo logonInfo = (LogonInfo) Component.getInstance("logonInfo", ScopeType.SESSION);

        HouseContract houseContract = new HouseContract(NumberPool.instance().genContractNumber(),
                logonInfo.getGroupCode(), new Date(),
                HouseContract.ContractStatus.PREPARE, logonInfo.getUserId(), logonInfo.getEmployeeName(), PoolType.SINGLE_OWNER);

        NewHouseContract newHouseContract = new NewHouseContract(houseContract);
        houseContract.setNewHouseContract(newHouseContract);

        SaleProxyPerson saleProxyPerson = new SaleProxyPerson(houseContract);
        houseContract.setSaleProxyPerson(saleProxyPerson);

        return houseContract;
    }

    private List<ContractTemplate> contractTemplateList;

    public List<ContractTemplate> getContractTemplateList() {
        if (contractTemplateList == null) {
            LogonInfo logonInfo = (LogonInfo) Component.getInstance("logonInfo", ScopeType.SESSION);
            contractTemplateList = getEntityManager().createQuery("select contract from ContractTemplate contract where contract.type = :contractType and contract.projectCode = :groupCode", ContractTemplate.class)
                    .setParameter("groupCode", logonInfo.getGroupCode()).setParameter("contractType", getInstance().getType()).getResultList();
        }
        return contractTemplateList;
    }

//    private void fillContextMap() throws JSONException {
//        if (getInstance().getContext() == null || getInstance().getContext().trim().equals("")){
//            contractContextMap = new ContractContextMap();
//        }else{
//            contractContextMap = new ContractContextMap(new JSONObject(getInstance().getContext()));
//        }
//    }

    @Override
    protected void initInstance() {
        super.initInstance();

        proxyPersonHelper = new PersonHelper<SaleProxyPerson>(getInstance().getSaleProxyPerson());
        contractContextMap = null;

        housePoolList = new ArrayList<PowerPersonHelper>(getInstance().getBusinessPools().size());
        for (BusinessPool pool : getInstance().getBusinessPoolList()) {
            housePoolList.add(new PowerPersonHelper(pool, getInstance().getHouseArea()));
        }

    }

    public List<PowerPersonHelper> getContractPoolOwners() {
        if (housePoolList == null) {
            getInstance();
        }
        return housePoolList;
    }

    private void savePoolOwner() {

        getInstance().getBusinessPools().clear();
        for (PersonHelper<BusinessPool> pool : getContractPoolOwners()) {
            getInstance().getBusinessPools().add(pool.getPersonEntity());
        }
    }

    private boolean saveOrUpdateContract() {
        savePoolOwner();
        try {
            JSONArray poja = new JSONArray();
            for (BusinessPool bp : getInstance().getBusinessPoolList()) {
                JSONObject jo = new JSONObject();

                String type = bp.getContractPersonType().name();

                jo.put(type + "_name", bp.getPersonName());
                jo.put(type + "_cer_number",bp.getCredentialsNumber());
                jo.put(type + "_legal_person",bp.getLegalPerson());
                if (bp.getPowerProxyPerson() != null){
                    jo.put(type + "_proxy_name",bp.getPowerProxyPerson().getPersonName());
                    jo.put(type + "_proxy_cer_number", bp.getPowerProxyPerson().getCredentialsNumber());
                }
                poja.put(jo);

            }

            for(ContractNumber co: getInstance().getContractNumbers()){
                JSONObject jo = new JSONObject();
                jo.put("contract_number",co.getContractNumber());
                poja.put(jo);
            }

            getInstance().setContractIndex(poja.toString());

            if (contractContextMap != null) {
                getInstance().setContext(contractContextMap.toJson().toString());
            }
        } catch (JSONException e) {
            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "ContractContextFail");
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public String persist() {
        if (saveOrUpdateContract()) {
            return super.persist();
        } else {
            return null;
        }
    }


    @Override
    @Transactional
    public String update() {
        if (saveOrUpdateContract()) {
            return super.update();
        } else {
            return null;
        }
    }

    public void poolTypeChange() {
        if (PoolType.SINGLE_OWNER.equals(getPoolType())) {
            poolOwnerCount = 1;
        }
    }

    public int getPoolOwnerCount() {
        return poolOwnerCount;
    }

    public void setPoolOwnerCount(int poolOwnerCount) {
        this.poolOwnerCount = poolOwnerCount;
    }

    public PoolType getPoolType() {
        return getInstance().getPoolType();
    }

    public void setPoolType(PoolType poolType) {
        if (PoolType.SINGLE_OWNER.equals(poolType)) {
            setPoolOwnerCount(1);
        }
        getInstance().setPoolType(poolType);
    }


    public void setContext(String context) throws JSONException {
        getInstance().setContext(context);
        contractContextMap = null;
    }

    public void clearContext() {
        getInstance().setContext(null);
        contractContextMap = new ContractContextMap();
    }

    public void genPoolOwner() {

        while (getContractPoolOwners().size() != poolOwnerCount) {
            if (getContractPoolOwners().size() > getPoolOwnerCount()) {
                getInstance().getBusinessPools().remove(getContractPoolOwners().remove(0).getPersonEntity());
            } else {
                BusinessPool pool = new BusinessPool(getInstance(), BusinessPool.ContractPersonType.BUYER, getContractPoolOwners().size() + 1);
                getContractPoolOwners().add(new PowerPersonHelper(pool, getInstance().getHouseArea()));
                getInstance().getBusinessPools().add(pool);
            }
        }
    }

    public ContractContextMap getContractContextMap() {
        if (contractContextMap == null) {
            Logging.getLog(getClass()).debug("load context");
            if (getInstance().getContext() == null || getInstance().getContext().trim().equals("")) {
                contractContextMap = new ContractContextMap();

            } else {
                try {
                    contractContextMap = new ContractContextMap(new JSONObject(getInstance().getContext()));
                } catch (JSONException e) {
                    throw new IllegalArgumentException("context fail");
                }
            }
        }
        return contractContextMap;
    }

    public String printSingleContract(){
        return "view-contract-" + RunParam.instance().getParamValue("CONTRACT_LOCATION") + getInstance().getType().getPatchByVersion(getInstance().getContractVersion());

    }

    public String viewSingleContract() {
        if (RunParam.instance().getBooleanParamValue("USE_FINGERPRINT")) {
            FingerCollection.instance().clear();
            return "vaild-contract-finger";
        }
        return printSingleContract();

    }

    public String editContract() {
        return "edit-contract-" + RunParam.instance().getParamValue("CONTRACT_LOCATION") + getInstance().getType().getPatchByVersion(getInstance().getContractVersion());
    }

    private String searchPassword;

    public String getSearchPassword() {
        return searchPassword;
    }

    public void setSearchPassword(String searchPassword) {
        this.searchPassword = searchPassword;
    }

    public String commit() {
        DeveloperSaleService.CommitResult result = DeveloperSaleServiceImpl.instance().commitContract((DeveloperLogonInfo) Component.getInstance("logonInfo", true, true), toJson().toString());
        if (DeveloperSaleService.CommitResult.COMMIT_OK.equals(result)) {
            for (ContractNumber cn : getInstance().getContractNumbers()) {
                cn.setPassword(searchPassword);
            }
            getInstance().setStatus(HouseContract.ContractStatus.SUBMIT);
            return super.update();
        }
        facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "commitError_" + result.name());
        return null;
    }

    //提交合同信息
    private JSONObject toJson() {
        JSONObject contractJson = new JSONObject();
        try {
            contractJson.put("id", getInstance().getId());
            contractJson.put("projectId", getInstance().getNewHouseContract().getProjectCode());
            contractJson.put("houseCode", getInstance().getHouseCode());
            contractJson.put("type", getInstance().getType().name());
            contractJson.put("createTime", getInstance().getCreateTime().getTime());
            contractJson.put("attachEmpId", getInstance().getAttachEmpId());
            contractJson.put("attachEmpName", getInstance().getAttachEmpName());
            contractJson.put("contractPrice", getInstance().getPrice().doubleValue());
            contractJson.put("contract", new JSONObject(getInstance().getContext()));
            contractJson.put("contractVersion", getInstance().getContractVersion());
            contractJson.put("poolType", getInstance().getPoolType().name());
            contractJson.put("salePayType", getInstance().getNewHouseContract().getSalePayType().name());

            contractJson.put("projectCerNumber", getInstance().getNewHouseContract().getProjectCerNumber());

            contractJson.put("proxyPerson", getInstance().getSaleProxyPerson().getPersonName());
            contractJson.put("proxyCredentialsType", getInstance().getSaleProxyPerson().getCredentialsType().name());
            contractJson.put("proxyCredentialsNumber", getInstance().getSaleProxyPerson().getCredentialsNumber());
            contractJson.put("proxyTel", getInstance().getSaleProxyPerson().getTel());

//            contractJson.put("name", getInstance().getContractOwner().getPersonName());
//            contractJson.put("credentialsType", getInstance().getContractOwner().getCredentialsType().name());
//            contractJson.put("credentialsNumber", getInstance().getContractOwner().getCredentialsNumber());
//            contractJson.put("tel", getInstance().getContractOwner().getPhone());
//            contractJson.put("rootAddress", getInstance().getContractOwner().getRootAddress());
//            contractJson.put("legalPerson", getInstance().getContractOwner().getLegalPerson());
//            contractJson.put("address", getInstance().getContractOwner().getAddress());

            JSONArray numberJsonArray = new JSONArray();
            for (ContractNumber contractNumber : getInstance().getContractNumbers()) {
                numberJsonArray.put(contractNumber.getContractNumber());
            }
            contractJson.put("contractNumber", numberJsonArray);


                JSONArray poolArray = new JSONArray();
                for (BusinessPool businessPool : getInstance().getBusinessPoolList()) {
                    JSONObject poolObj = new JSONObject();
                    poolObj.put("name", businessPool.getPersonName());
                    poolObj.put("credentialsType", businessPool.getCredentialsType().name());
                    poolObj.put("credentialsNumber", businessPool.getCredentialsNumber());
                    poolObj.put("rootAddress",businessPool.getRootAddress());
                    poolObj.put("address",businessPool.getAddress());
                    if (businessPool.getPoolArea() != null) {
                        poolObj.put("poolArea", businessPool.getPoolArea().doubleValue());
                    } else {
                        poolObj.put("poolArea", 0.0);
                    }
                    if (businessPool.getRelation() != null) {
                        poolObj.put("relation", businessPool.getRelation());
                    } else {
                        poolObj.put("relation", "0");
                    }
                    poolObj.put("perc", businessPool.getPoolPerc());
                    poolObj.put("tel", businessPool.getPhone());
                    poolObj.put("legalPerson", businessPool.getLegalPerson());
                    if (businessPool.getLegalType() != null)
                        poolObj.put("legalType", businessPool.getLegalType().name());
                    poolArray.put(poolObj);
                }
                contractJson.put("pool", poolArray);


            return contractJson;
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
