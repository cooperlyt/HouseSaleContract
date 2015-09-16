package com.dgsoft.house.sale.action;

import com.dgsoft.developersale.LogonInfo;
import com.dgsoft.house.sale.NumberPool;
import com.dgsoft.house.sale.model.HouseContract;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Credentials;

import java.util.Date;

/**
 * Created by cooper on 9/15/15.
 */
@Name("houseContractHome")
public class HouseContractHome extends EntityHome<HouseContract> {


    @Override
    protected HouseContract createInstance(){
        LogonInfo logonInfo = (LogonInfo) Component.getInstance("logonInfo");
        Credentials credentials = (Credentials) Component.getInstance("credentials");
        return new HouseContract(NumberPool.instance().genContractNumber(),
                logonInfo.getGroupCode(),new Date(),
                HouseContract.ContractStatus.PREPARE,credentials.getUsername(),logonInfo.getEmployeeName());
    }
}
