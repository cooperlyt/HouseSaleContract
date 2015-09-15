package com.dgsoft.house.sale.contract;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Logging;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cooper on 9/12/15.
 */
@Name("contractContext")
public class ContractContext {


    private ContractContextMap contractContextMap;

    public void setContext(String context) throws JSONException {
        contractContextMap = new ContractContextMap(new JSONObject(context));
    }

    public String getContext(){
        try {
            return getContractContextMap().toJson().toString();
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public ContractContextMap getContractContextMap() {
        if (contractContextMap == null){
            contractContextMap = new ContractContextMap();
        }

        return contractContextMap;
    }

    private ContractType contractType;

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }


    public String getContractTypeName(){
        if (contractType == null){
            return null;
        }
        return contractType.name();
    }

    public void setContractTypeName(String name){
        if ((name == null) || name.trim().equals("")){
            contractType = null;
        }else{
            contractType = ContractType.valueOf(name);
        }

    }

    public String createTemplate(){
        //operType = OperType.CREATE_TEMPLATE;
        Logging.getLog(getClass()).debug(contractType.getCurrentVersion());
        Logging.getLog(getClass()).debug(contractType.getCurrentPatch());

        return contractType.getCurrentPatch();

    }






}
