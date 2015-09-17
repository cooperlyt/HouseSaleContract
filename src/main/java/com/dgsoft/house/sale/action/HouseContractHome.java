package com.dgsoft.house.sale.action;

import com.dgsoft.common.SetLinkList;
import com.dgsoft.common.system.PersonHelper;
import com.dgsoft.developersale.LogonInfo;
import com.dgsoft.house.sale.ContractOwnerHelper;
import com.dgsoft.house.sale.NumberPool;
import com.dgsoft.house.sale.model.BusinessPool;
import com.dgsoft.house.sale.model.ContractOwner;
import com.dgsoft.house.sale.model.HouseContract;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Credentials;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cooper on 9/15/15.
 */
@Name("houseContractHome")
public class HouseContractHome extends EntityHome<HouseContract> {


    private List<PersonHelper<BusinessPool>> housePoolList;

    private ContractOwnerHelper contractOwner;


    @Override
    protected HouseContract createInstance(){

        LogonInfo logonInfo = (LogonInfo) Component.getInstance("logonInfo", ScopeType.SESSION);
        return new HouseContract(NumberPool.instance().genContractNumber(),
                logonInfo.getGroupCode(),new Date(),
                HouseContract.ContractStatus.PREPARE,logonInfo.getUserId(),logonInfo.getEmployeeName());
    }

    @Override
    protected void initInstance(){
        super.initInstance();
        ContractOwner owner = getInstance().getContractOwner();
        if (owner == null){
            owner = new ContractOwner();
        }
        contractOwner = new ContractOwnerHelper(owner);

        housePoolList = new ArrayList<PersonHelper<BusinessPool>>(getInstance().getBusinessPools().size());
        for(BusinessPool pool: getInstance().getBusinessPools()){
            housePoolList.add(new PersonHelper<BusinessPool>(pool));
        }
    }

    public List<PersonHelper<BusinessPool>> getContractPoolOwners() {
        if (housePoolList == null){
            getInstance();
        }
        return housePoolList;
    }

    public ContractOwnerHelper getContractOwnerHelper(){
        if (contractOwner == null){
            getInstance();
        }
        return contractOwner;
    }

    private void savePoolOwner(){
        getInstance().getBusinessPools().clear();
        for(PersonHelper<BusinessPool> pool: housePoolList){
            getInstance().getBusinessPools().add(pool.getPersonEntity());
        }
    }

    @Override
    @Transactional
    public String persist(){
        savePoolOwner();
        return super.persist();
    }


    @Override
    @Transactional
    public String update(){
        savePoolOwner();
        return super.update();
    }


}
