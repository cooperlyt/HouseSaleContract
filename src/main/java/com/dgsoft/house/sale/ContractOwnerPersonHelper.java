package com.dgsoft.house.sale;

import com.dgsoft.common.system.PersonHelper;
import com.dgsoft.common.system.PersonIDCard;
import com.dgsoft.house.sale.model.ContractOwner;

/**
 * Created by cooper on 9/17/15.
 */
public class ContractOwnerPersonHelper extends PersonHelper<ContractOwner> {


    public ContractOwnerPersonHelper(ContractOwner entity) {
        super(entity);
    }


    @Override
    protected void fillPerson(PersonIDCard person){
        super.fillPerson(person);
        getPersonEntity().setAddress(person.getAddress());
        //TODO rootAddress
    }

}
