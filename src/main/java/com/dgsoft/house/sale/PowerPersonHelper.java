package com.dgsoft.house.sale;

import cc.coopersoft.house.ProxyType;
import cc.coopersoft.house.sale.data.PowerPerson;
import cc.coopersoft.house.sale.data.PowerProxyPerson;
import com.dgsoft.common.system.OwnerPersonHelper;
import com.dgsoft.common.system.PersonHelper;

import java.math.BigDecimal;

/**
 * Created by cooper on 9/29/16.
 */
public class PowerPersonHelper extends OwnerPersonHelper<PowerPerson> {

    private PersonHelper<PowerProxyPerson> proxyPersonHelper = new PersonHelper<PowerProxyPerson>();

    public PowerPersonHelper(PowerPerson entity, BigDecimal houseArea) {
        super(entity, houseArea);
    }

    public ProxyType getProxyType() {
        if (getPersonEntity().getPowerProxyPerson() == null){
            return null;
        }else{
            return getPersonEntity().getPowerProxyPerson().getProxyType();
        }

    }

    public void setProxyType(ProxyType proxyType) {
        if (proxyType == null){
            getPersonEntity().setPowerProxyPerson(null);
            proxyPersonHelper.setPersonEntity(null);
        }else{
            if (getPersonEntity().getPowerProxyPerson() == null){
                getPersonEntity().setPowerProxyPerson(new PowerProxyPerson(getPersonEntity()));
                proxyPersonHelper.setPersonEntity( getPersonEntity().getPowerProxyPerson());
            }
            getPersonEntity().getPowerProxyPerson().setProxyType(proxyType);
        }
    }

    public PersonHelper<PowerProxyPerson> getProxyPersonHelper() {
        return proxyPersonHelper;
    }
}
