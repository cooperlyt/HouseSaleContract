package com.dgsoft.developersale;

import java.net.MalformedURLException;

/**
 * Created by cooper on 9/8/15.
 */
public class DeveloperSaleServiceImpl extends DeveloperSaleService {

    private static DeveloperSaleService saleService;

    private DeveloperSaleServiceImpl() throws MalformedURLException {
    }

    public static DeveloperSaleService instance(){
        if (saleService == null){
            try {
                saleService = new DeveloperSaleServiceImpl();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return saleService;
    }

    @Override
    protected String getWsdlLocation() {
        return "http://localhost:8080/HMPLAT/DeveloperService?wsdl";
    }
}
