package com.dgsoft.house.sale.contract;


/**
 * Created by cooper on 9/12/15.
 */
public enum ContractType {

    MAP_SALE_NEW_HOUSE("/contract/pre-sale-v1"),
    SALE_NEW_HOUSE(""),
    SALE_OLD_HOUSE("");


    private String[] versions;

    public int getCurrentVersion(){
        return versions.length;
    }

    public String getCurrentPatch(){
        if (versions.length > 0){
            return versions[versions.length - 1];
        }
        return null;
    }

    public String getPatchByVersion(int version){
        if (version > this.versions.length)
            return null;
        return versions[version-1];
    }

    ContractType(String... versions){
        this.versions = versions;

    }

}
