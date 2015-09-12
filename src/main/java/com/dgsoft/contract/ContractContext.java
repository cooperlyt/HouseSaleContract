package com.dgsoft.contract;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cooper on 9/12/15.
 */
@Name("contractContext")
public class ContractContext {

    @Out(required = false, scope = ScopeType.PAGE)
    @In(required = false,scope = ScopeType.PAGE)
    private String context;

    @In
    private FacesMessages facesMessages;

    private ContractContextMap contractContextMap;

    public ContractContextMap getContractContextMap() {
        if (contractContextMap == null){
            if (isContextDefined()){
                try {
                    contractContextMap = new ContractContextMap(new JSONObject(context));
                } catch (JSONException e) {
                    facesMessages.addFromResourceBundle(StatusMessage.Severity.WARN,"ContractTemplateFail");
                    contractContextMap = new ContractContextMap();
                }
            }else{
                contractContextMap = new ContractContextMap();
            }
        }

        return contractContextMap;
    }

    public boolean isContextDefined(){
        return (context != null) && !context.trim().equals("");
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        if (context == null || context.trim().equals("") || ((this.context != null) && !this.context.equals(context))){
            contractContextMap = null;
        }
        this.context = context;
    }

    public void contextChangeListener(){
        try {
            context = getContractContextMap().toJson().toString();
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
