package com.dgsoft.house.sale.action;

import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.house.sale.ContractService;
import cc.coopersoft.house.sale.data.*;
import com.dgsoft.common.system.PersonHelper;
import com.dgsoft.common.system.RunParam;
import com.dgsoft.developersale.DeveloperLogonInfo;
import com.dgsoft.developersale.LogonInfo;
import com.dgsoft.house.OwnerShareCalcType;
import com.dgsoft.house.PoolType;
import com.dgsoft.house.sale.ContractPowerPersonHelper;
import com.dgsoft.house.sale.NumberPool;
import com.dgsoft.house.sale.contract.ContractContextMap;
import com.dgsoft.house.sale.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.*;

/**
 * Created by cooper on 9/15/15.
 */
@Name("houseContractHome")
public class HouseContractHome extends EntityHome<HouseContract> {

    public PowerPerson.LegalType[] getLegalTypes() {
        return PowerPerson.LegalType.values();
    }


    @In
    private FacesMessages facesMessages;

    private List<ContractPowerPersonHelper> housePoolList;

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

        HouseContract houseContractEntity = new HouseContract(NumberPool.instance().genContractNumber(),
                logonInfo.getGroupCode(), new Date(),
                HouseContract.ContractStatus.PREPARE, logonInfo.getUserId(), logonInfo.getEmployeeName(), PoolType.SINGLE_OWNER);

        NewHouseContract newHouseContract = new NewHouseContract(houseContractEntity);
        houseContractEntity.setNewHouseContract(newHouseContract);

        SaleProxyPerson saleProxyPerson = new SaleProxyPerson(houseContractEntity);
        houseContractEntity.setSaleProxyPerson(saleProxyPerson);

        return houseContractEntity;
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

        housePoolList = new ArrayList<ContractPowerPersonHelper>(getInstance().getPowerPersons().size());
        for (PowerPerson pool : getInstance().getBusinessPoolList()) {
            housePoolList.add(new ContractPowerPersonHelper( pool, getInstance().getHouseArea()));
        }

    }

    public List<ContractPowerPersonHelper> getContractPoolOwners() {
        if (housePoolList == null) {
            getInstance();
        }
        return housePoolList;
    }

    private void savePoolOwner() {

        getInstance().getPowerPersons().clear();
        for (PersonHelper<PowerPerson> pool : getContractPoolOwners()) {
            getInstance().getPowerPersons().add(pool.getPersonEntity());
        }
    }

    private boolean saveOrUpdateContract() {
        savePoolOwner();
        try {
            JSONArray poja = new JSONArray();
            for (PowerPerson bp : getInstance().getBusinessPoolList()) {
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
                getInstance().getPowerPersons().remove(getContractPoolOwners().remove(0).getPersonEntity());
            } else {
                PowerPerson pool = new PowerPerson(UUID.randomUUID().toString().replace("-",""), getInstance(), PowerPerson.ContractPersonType.BUYER, getContractPoolOwners().size() + 1);
                getContractPoolOwners().add(new ContractPowerPersonHelper(pool, getInstance().getHouseArea()));
                getInstance().getPowerPersons().add(pool);
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

        ObjectMapper mapper = new ObjectMapper();
        String jsonData;
        try {
            jsonData = mapper.writeValueAsString(getInstance());
            Logging.getLog(getClass()).debug("commit data:" + jsonData);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("contract to json fail!",e);
        }


        try {
            // store business id;
            ContractService.commitSaleContract(RunParam.instance().getParamValue("server_address"), (DeveloperLogonInfo) Component.getInstance("logonInfo", true, true),jsonData);
            for (ContractNumber cn : getInstance().getContractNumbers()) {
                cn.setPassword(searchPassword);
            }
            getInstance().setStatus(HouseContract.ContractStatus.SUBMIT);
            return super.update();
        } catch (Exception e) {
            Logging.getLog(getClass()).error("sumit contrace error:" ,e);
            if (e instanceof HttpApiServerException){
                Logging.getLog(getClass()).error("respone code:" ,((HttpApiServerException) e).getHttpStatus());
            }

            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "contract_commit_fail");
            return null;
        }


    }

}
