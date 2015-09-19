package com.dgsoft.house.sale.contract;

import com.dgsoft.house.SaleType;
import com.dgsoft.house.sale.model.ContractTemplate;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Logging;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by cooper on 9/12/15.
 */
@Name("contractContext")
@Scope(ScopeType.CONVERSATION)
public class ContractContext {


    private ContractContextMap contractContextMap;


    public void setContext(String context) throws JSONException {
        if (context == null || context.trim().equals("")){
            clearContext();
        }else
            contractContextMap = new ContractContextMap(new JSONObject(context));
    }

    public void clearContext(){
        contractContextMap = new ContractContextMap();
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

    private SaleType contractType;

    public SaleType getContractType() {
        return contractType;
    }

    public void setContractType(SaleType contractType) {
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
            contractType = SaleType.valueOf(name);
        }

    }

    public String toContextEdit(){
        //operType = OperType.CREATE_TEMPLATE;
        Logging.getLog(getClass()).debug(contractType.getCurrentVersion());
        Logging.getLog(getClass()).debug(contractType.getCurrentPatch());

        return "edit-contract-" +contractType.getCurrentPatch();


    }






}
