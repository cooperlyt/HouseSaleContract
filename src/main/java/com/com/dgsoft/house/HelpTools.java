package com.com.dgsoft.house;

import com.dgsoft.common.system.PersonEntity;
import com.dgsoft.house.PoolType;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;

/**
 * Created by cooper on 9/17/15.
 */
@Name("helpTools")
public class HelpTools {

    @Factory(value = "credentialsTypes",scope = ScopeType.SESSION )
    public PersonEntity.CredentialsType[] getCredentialsTypes(){
        return PersonEntity.CredentialsType.values();
    }

    @Factory(value = "poolTypes",scope = ScopeType.SESSION )
    public PoolType[] getPoolTypes(){
        return PoolType.values();
    }

}
