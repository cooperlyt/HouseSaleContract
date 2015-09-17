package com.dgsoft.common.system;

/**
 * Created by cooper on 9/19/14.
 */
public class PersonHelper<E extends PersonEntity> extends PersonHelperBase<E>  {


    public PersonHelper(E entity) {
        super(entity);
    }

    @Override
    protected PersonIDCard findStorePersonIDCard(String s) {
        //TODO getInfo from fc
        return null;
    }

}
