package com.dgsoft.house.sale.developer;

import com.dgsoft.common.system.PersonEntity;
import com.dgsoft.developersale.*;
import com.dgsoft.house.PoolType;
import com.dgsoft.house.sale.DeveloperSaleServiceImpl;
import com.dgsoft.house.sale.action.HouseContractHome;
import com.dgsoft.house.sale.contract.ContractContext;
import com.dgsoft.house.sale.contract.ContractContextMap;
import com.dgsoft.house.sale.model.BusinessPool;
import com.dgsoft.house.sale.model.ContractTemplate;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Logging;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by cooper on 9/15/15.
 */
@Name("contractCreate")
@Scope(ScopeType.CONVERSATION)
public class ContractCreate {

    @In(create = true)
    private HouseContractHome houseContractHome;

    @In(create = true)
    private ContractContext contractContext;

    @In
    private FacesMessages facesMessages;

    @In
    private LogonInfo logonInfo;

    @In
    private Map<String, String> messages;

    private ContractTemplate contractTemplate;

    private BigDecimal unitPrice;

    private String moneyType = "人民币";

    private String calcTypeName;

    private int calcType = 1;

    private Locale locale = Locale.CHINA;



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

    public ContractTemplate getContractTemplate() {
        return contractTemplate;
    }

    public void setContractTemplate(ContractTemplate contractTemplate) {
        this.contractTemplate = contractTemplate;
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

    @Transactional
    public String fillContractContext(){

        if ("persisted".equals(houseContractHome.persist())) {

            //contractContext.
            if (contractTemplate != null){
                try {
                    contractContext.setContext(contractTemplate.getContext());
                } catch (JSONException e) {
                    facesMessages.addFromResourceBundle(StatusMessage.Severity.WARN,"ContractTemplateFail");
                    contractContext.clearContext();
                }
            }

            DeveloperLogonInfo dLogonInfo = (DeveloperLogonInfo) logonInfo;

            //出卖人
            contractContext.getContractContextMap().put("dname",new ContractContextMap.ContarctContextItem(getHouse().getSaleBuild().getProjectSellCard().getDeveloperName()));
            contractContext.getContractContextMap().put("daddress", new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getAddress()));
            contractContext.getContractContextMap().put("dpost", new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getPostCode()));
            contractContext.getContractContextMap().put("dln",new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getLicenseNumber()));
            contractContext.getContractContextMap().put("dln", new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getCerCode()));
            contractContext.getContractContextMap().put("downer", new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getOwnerPerson()));
            contractContext.getContractContextMap().put("downertel",new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getOwnerTel()));


            //买受人
            contractContext.getContractContextMap().put("owner",new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getContractOwner().getPersonName()));

            if (PersonEntity.CredentialsType.COMPANY_CODE.equals(houseContractHome.getInstance().getContractOwner().getCredentialsType())){
                contractContext.getContractContextMap().put("ownertype", new ContractContextMap.ContarctContextItem(messages.get(houseContractHome.getInstance().getContractOwner().getLegalType().name())));
            }
            if(PersonEntity.CredentialsType.PASSPORT.equals(houseContractHome.getInstance().getContractOwner().getCredentialsType())){
                contractContext.getContractContextMap().put("ownercitytype",new ContractContextMap.ContarctContextItem(messages.get("ContractOwner_country")));
            }else{
                contractContext.getContractContextMap().put("ownercitytype",new ContractContextMap.ContarctContextItem(messages.get("ContractOwner_rootAddress")));
            }
            contractContext.getContractContextMap().put("ownercityname", new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getContractOwner().getRootAddress()));
            contractContext.getContractContextMap().put("ownertypevalue", new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getContractOwner().getLegalPerson()));
            contractContext.getContractContextMap().put("ownercertype", new ContractContextMap.ContarctContextItem(messages.get(houseContractHome.getInstance().getContractOwner().getCredentialsType().name())));
            contractContext.getContractContextMap().put("ownercernumber", new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getContractOwner().getCredentialsNumber()));
            contractContext.getContractContextMap().put("owneraddress", new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getContractOwner().getAddress()));


            List<ContractContextMap> poolOwners = new ArrayList<ContractContextMap>();
            for(BusinessPool poolOwner: houseContractHome.getInstance().getBusinessPools()){
                ContractContextMap poolInfoMap = new ContractContextMap();
                poolInfoMap.put("owner", new ContractContextMap.ContarctContextItem(poolOwner.getPersonName()));

                if (PersonEntity.CredentialsType.COMPANY_CODE.equals(poolOwner.getCredentialsType())){
                    poolInfoMap.put("ownertype", new ContractContextMap.ContarctContextItem(messages.get(poolOwner.getLegalType().name())));
                }
                if(PersonEntity.CredentialsType.PASSPORT.equals(poolOwner.getCredentialsType())){
                    poolInfoMap.put("ownercitytype", new ContractContextMap.ContarctContextItem(messages.get("ContractOwner_country")));
                }else{
                    poolInfoMap.put("ownercitytype", new ContractContextMap.ContarctContextItem(messages.get("ContractOwner_rootAddress")));
                }

                poolInfoMap.put("ownertypevalue", new ContractContextMap.ContarctContextItem(poolOwner.getLegalPerson()));
                poolInfoMap.put("ownercertype", new ContractContextMap.ContarctContextItem(messages.get(poolOwner.getCredentialsType().name())));
                poolInfoMap.put("ownercernumber", new ContractContextMap.ContarctContextItem(poolOwner.getCredentialsNumber()));
                poolOwners.add(poolInfoMap);
            }

            contractContext.getContractContextMap().put("poolOwner", new ContractContextMap.ContarctContextItem(poolOwners));


            return "edit-contract-" + houseContractHome.getInstance().getType().getCurrentPatch();
        }

        return null;
    }



    @Transactional
    public String beginContract(){
        houseContractHome.getInstance().setPrice(getTotalMoney());
        houseContractHome.getInstance().setContractVersion(houseContractHome.getInstance().getType().getCurrentVersion());
        if (!PoolType.SINGLE_OWNER.equals(houseContractHome.getPoolType())) {
            houseContractHome.genPoolOwner();
            return "persisted-poolOwner";
        }else{
            return fillContractContext();
        }

    }
}
