package com.dgsoft.house;

import cc.coopersoft.comm.District;
import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.house.ProxyType;
import cc.coopersoft.house.sale.HouseSellService;
import com.dgsoft.common.system.PersonEntity;
import com.dgsoft.common.system.RunParam;
import com.dgsoft.common.system.Sex;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cooper on 9/25/16.
 */
@Name("commDataFactory")
public class CommDataFactory {

    @Factory(value = "credentialsTypes",scope = ScopeType.APPLICATION )
    public PersonEntity.CredentialsType[] getCredentialsTypes(){
        return PersonEntity.CredentialsType.values();
    }

    @Factory(value = "personCredentialsTypes", scope = ScopeType.APPLICATION)
    public List<PersonEntity.CredentialsType> getPersonCredentialsTypes(){
        List<PersonEntity.CredentialsType> result = new ArrayList<PersonEntity.CredentialsType>();
        for(PersonEntity.CredentialsType ct: getCredentialsTypes()){
            if (!ct.isCorp()){
                result.add(ct);
            }
        }
        return result;
    }

    @Factory(value = "poolTypes",scope = ScopeType.APPLICATION )
    public PoolType[] getPoolTypes(){
        return PoolType.values();
    }

    @Factory(value = "salePayType", scope = ScopeType.APPLICATION)
    public SalePayType[] getSalePayType(){
        return SalePayType.values();
    }

    @Factory(value = "ownerShareCalcTypes",scope = ScopeType.APPLICATION)
    public OwnerShareCalcType[] getOwnerShareCalcTypes(){return OwnerShareCalcType.values();}

    @Factory(value = "sexValues",scope = ScopeType.APPLICATION)
    public Sex[] getSexValues(){
        return Sex.values();
    }

    @Factory(value = "proxyTypes", scope = ScopeType.APPLICATION)
    public ProxyType[] getProxyTypes(){
        return ProxyType.values();
    }

    @In
    private FacesMessages facesMessages;

    @Factory(scope = ScopeType.APPLICATION,autoCreate = true)
    public List<District> getDistrictList(){
        try {
            return HouseSellService.listDistrict(RunParam.instance().getParamValue("nginx_address"));
        } catch (HttpApiServerException e) {
            Logging.getLog(getClass()).warn("get District error:", e);
            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,"query_old_house_error");
            return null;
        }
    }

}
