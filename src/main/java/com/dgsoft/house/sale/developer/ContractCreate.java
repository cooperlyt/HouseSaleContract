package com.dgsoft.house.sale.developer;

import com.dgsoft.developersale.*;
import com.dgsoft.house.sale.DeveloperSaleServiceImpl;
import com.dgsoft.house.sale.action.HouseContractHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Logging;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Created by cooper on 9/15/15.
 */
@Name("contractCreate")
@Scope(ScopeType.CONVERSATION)
public class ContractCreate {

    @In(create = true)
    private HouseContractHome houseContractHome;

    private BigDecimal unitPrice;

    private String moneyType = "人民币";

    private String calcTypeName;

    private int calcType = 1;

    private Locale locale = Locale.CHINA;

    @In
    private LogonInfo logonInfo;

    private SaleHouse house;

    private String houseCode;

    public SaleHouse getHouse() {
        if (house == null || !house.getHouseCode().equals(houseCode) ){
            if (houseCode == null || houseCode.trim().equals("")){
                house = null;
            }else
                house = DeveloperSaleServiceImpl.instance().getHouseInfoBySale((DeveloperLogonInfo) logonInfo,houseCode);
        }
        return house;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String createContract(){
        houseContractHome.clearInstance();
        houseContractHome.getInstance().setHouseCode(houseCode);




        Logging.getLog(getClass()).debug("1:" + getHouse().getSaleBuild());
        Logging.getLog(getClass()).debug("2:" + getHouse().getSaleBuild().getProjectSellCard());

        houseContractHome.getInstance().setType(getHouse().getSaleBuild().getProjectSellCard().getCardType());
        return "contract-begin";
    }


    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getCalcTypeName() {
        return calcTypeName;
    }

    public void setCalcTypeName(String calcTypeName) {
        this.calcTypeName = calcTypeName;
    }

    public int getCalcType() {
        return calcType;
    }

    public void setCalcType(int calcType) {
        this.calcType = calcType;
    }

    public Locale getLocale() {
        return locale;
    }

    public BigDecimal getTotalMoney(){

        if (getUnitPrice() == null){
            return null;
        }
        switch (calcType){
            case 1:

                return getHouse().getUseArea().multiply(getUnitPrice()).setScale(0,BigDecimal.ROUND_HALF_UP);
            case 2:

                return getHouse().getHouseArea().multiply(getUnitPrice()).setScale(0,BigDecimal.ROUND_HALF_UP);
            default:
                return  getUnitPrice().setScale(0,BigDecimal.ROUND_HALF_UP);
        }

    }
}
