package com.dgsoft.house.sale.action;

import com.dgsoft.developersale.DeveloperLogonInfo;
import com.dgsoft.developersale.LogonInfo;
import com.dgsoft.house.sale.contract.ContractContextMap;
import com.dgsoft.house.sale.model.ContractTemplate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Logging;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cooper on 9/14/15.
 */
@Name("contractTemplateHome")
public class ContractTemplateHome extends EntityHome<ContractTemplate> {


    private ContractContextMap contractContextMap;

    @In
    private FacesMessages facesMessages;

    @In
    private LogonInfo logonInfo;

    public void initInstance(){
        super.initInstance();
        if (isIdDefined() && getInstance().getContext() != null && !getInstance().getContext().trim().equals("")){
            try {
                contractContextMap = new ContractContextMap(new JSONObject(getInstance().getContext()));
            } catch (JSONException e) {
                facesMessages.addFromResourceBundle(StatusMessage.Severity.WARN,"ContractTemplateFail");
                contractContextMap = new ContractContextMap();
            }

        }else{
            contractContextMap = new ContractContextMap();
        }
    }

    @Override
    public String persist(){
        getInstance().setContractVersion(getInstance().getType().getCurrentVersion());

        try {
            getInstance().setContext(contractContextMap.toJson().toString());
        } catch (JSONException e) {
            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "ContractTemplateFail");
            return null;
        }

        getInstance().setProjectCode(((DeveloperLogonInfo)logonInfo).getSaleProject().getProjectCode());

        return super.persist();
    }

    @Override
    public String update(){

        try {
            getInstance().setContext(contractContextMap.toJson().toString());
        } catch (JSONException e) {
            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "ContractTemplateFail");
            return null;
        }
        return super.update();
    }

    public ContractContextMap getContractContextMap() {
        if (contractContextMap == null){
            getInstance();
        }
        return contractContextMap;
    }
}
