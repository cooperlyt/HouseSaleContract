package com.dgsoft.house.sale;

import com.dgsoft.developersale.DeveloperSaleService;

import java.net.MalformedURLException;

/**
 * Created by cooper on 9/8/15.
 */
public class DeveloperSaleServiceImpl extends DeveloperSaleService {

    private static DeveloperSaleService saleService;

    private DeveloperSaleServiceImpl() {
    }

    public static DeveloperSaleService instance(){
        if (saleService == null){
                saleService = new DeveloperSaleServiceImpl();

        }
        return saleService;
    }

    @Override
    protected String getWsdlLocation() {
        return "http://localhost:8080/HMPLAT/DeveloperService?wsdl";
    }
}
