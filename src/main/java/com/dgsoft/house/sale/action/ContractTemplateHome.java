package com.dgsoft.house.sale.action;

import com.dgsoft.developersale.DeveloperLogonInfo;
import com.dgsoft.developersale.LogonInfo;
import com.dgsoft.house.sale.contract.ContractContext;
import com.dgsoft.house.sale.model.ContractTemplate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Logging;
import org.json.JSONException;

/**
 * Created by cooper on 9/14/15.
 */
@Name("contractTemplateHome")
public class ContractTemplateHome extends EntityHome<ContractTemplate> {

    @In(create = true)
    private ContractContext contractContext;

    @In
    private FacesMessages facesMessages;

    @In
    private LogonInfo logonInfo;

    public void initInstance(){
        super.initInstance();
        if (isIdDefined()){
            try {
                contractContext.setContext(getInstance().getContext());
            } catch (JSONException e) {
                facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,"ContractTemplateFail");
            }
        }
    }

    @Override
    public String persist(){
        getInstance().setType(contractContext.getContractType());
        getInstance().setContractVersion(contractContext.getContractType().getCurrentVersion());

        getInstance().setContext(contractContext.getContext());
        Logging.getLog(getClass()).debug(contractContext.getContext());
        getInstance().setProjectCode(((DeveloperLogonInfo)logonInfo).getSaleProject().getProjectCode());

        return super.persist();
    }

    @Override
    public String update(){

        getInstance().setContext(contractContext.getContext());
        return super.update();
    }


}
