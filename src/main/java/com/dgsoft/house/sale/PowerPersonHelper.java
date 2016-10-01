package com.dgsoft.house.sale;

import com.dgsoft.common.system.OwnerPersonHelper;
import com.dgsoft.common.system.PersonHelper;
import com.dgsoft.house.sale.model.BusinessPool;
import com.dgsoft.house.sale.model.PowerProxyPerson;

import java.math.BigDecimal;

/**
 * Created by cooper on 9/29/16.
 */
public class PowerPersonHelper extends OwnerPersonHelper<BusinessPool> {

    private PersonHelper<PowerProxyPerson> proxyPersonHelper = new PersonHelper<PowerProxyPerson>();

    public PowerPersonHelper(BusinessPool entity, BigDecimal houseArea) {
        super(entity, houseArea);
    }

    public PowerProxyPerson.ProxyType getProxyType() {
        if (getPersonEntity().getPowerProxyPerson() == null){
            return null;
        }else{
            return getPersonEntity().getPowerProxyPerson().getProxyType();
        }

    }

    public void setProxyType(PowerProxyPerson.ProxyType proxyType) {
        if (proxyType == null){
            getPersonEntity().setPowerProxyPerson(null);
            proxyPersonHelper.setPersonEntity(null);
        }else{
            if (getPersonEntity().getPowerProxyPerson() == null){
                getPersonEntity().setPowerProxyPerson(new PowerProxyPerson(getPersonEntity()));
                proxyPersonHelper.setPersonEntity(getPersonEntity().getPowerProxyPerson());
            }
            getPersonEntity().getPowerProxyPerson().setProxyType(proxyType);
        }
    }

    public PersonHelper<PowerProxyPerson> getProxyPersonHelper() {
        return proxyPersonHelper;
    }
}
