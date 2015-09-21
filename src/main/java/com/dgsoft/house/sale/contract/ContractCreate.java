package com.dgsoft.house.sale.contract;

import com.dgsoft.common.system.PersonEntity;
import com.dgsoft.developersale.*;
import com.dgsoft.house.PledgeInfo;
import com.dgsoft.house.PoolType;
import com.dgsoft.house.sale.DeveloperSaleServiceImpl;
import com.dgsoft.house.sale.action.HouseContractHome;
import com.dgsoft.house.sale.model.BusinessPool;
import com.dgsoft.house.sale.model.ContractNumber;
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

    private int contractCount;

    private SaleHouse house;

    private String houseCode;

    public SaleHouse getHouse() {
        if (house == null || !house.getHouseCode().equals(houseCode) ){
            if (houseCode == null || houseCode.trim().equals("")){
                house = null;
            }else {
                house = DeveloperSaleServiceImpl.instance().getHouseInfoBySale((DeveloperLogonInfo) logonInfo, houseCode);
                if (house.getStatus().isCanSale()){
                    if (houseContractHome.getEntityManager().createQuery("select count(contract.id) from HouseContract contract where contract.houseCode = :houseCode", Long.class)
                            .setParameter("houseCode",house.getHouseCode()).getSingleResult().compareTo(Long.valueOf(0)) > 0){
                        house.setStatus(SaleStatus.PREPARE_CONTRACT);
                    }
                }
            }

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

    public int getContractCount() {
        return contractCount;
    }

    public void setContractCount(int contractCount) {
        this.contractCount = contractCount;
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

        if (contractTemplate != null){
            try {
                contractContext.setContext(contractTemplate.getContext());
            } catch (JSONException e) {
                facesMessages.addFromResourceBundle(StatusMessage.Severity.WARN,"ContractTemplateFail");
                contractContext.clearContext();
            }
        }
        Logging.getLog(getClass()).debug("fill sale info");
        genContractContext();

        houseContractHome.getInstance().setContext(contractContext.getContext());

        if ("persisted".equals(houseContractHome.persist())) {
            //contractContext.
            return "edit-contract-" + houseContractHome.getInstance().getType().getCurrentPatch();
        }

        return null;
    }

    private void genContractContext() {
        DeveloperLogonInfo dLogonInfo = (DeveloperLogonInfo) logonInfo;

        //出卖人
        contractContext.getContractContextMap().put("dname",new ContractContextMap.ContarctContextItem(getHouse().getSaleBuild().getProjectSellCard().getDeveloperName()));
        contractContext.getContractContextMap().put("daddress", new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getAddress()));
        contractContext.getContractContextMap().put("dpost", new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getPostCode()));
        contractContext.getContractContextMap().put("dln",new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getLicenseNumber()));
        contractContext.getContractContextMap().put("dln2", new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getCerCode()));
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

        // sale card

        ProjectSellCard sellCard = getHouse().getSaleBuild().getProjectSellCard();

        contractContext.getContractContextMap().put("c_1_1", new ContractContextMap.ContarctContextItem(sellCard.getLandGetMode()));
        contractContext.getContractContextMap().put("c_1_2", new ContractContextMap.ContarctContextItem(sellCard.getLandAddress()));
        contractContext.getContractContextMap().put("c_1_3", new ContractContextMap.ContarctContextItem(sellCard.getLandCardType()));
        contractContext.getContractContextMap().put("c_1_4", new ContractContextMap.ContarctContextItem(sellCard.getLandCardNumber()));
        contractContext.getContractContextMap().put("c_1_5", new ContractContextMap.ContarctContextItem(sellCard.getLandArea()));
        contractContext.getContractContextMap().put("c_1_6", new ContractContextMap.ContarctContextItem(sellCard.getLandUseType()));
        contractContext.getContractContextMap().put("c_1_7", new ContractContextMap.ContarctContextItem(sellCard.getLandEndUseTime()));

        contractContext.getContractContextMap().put("c_1_8", new ContractContextMap.ContarctContextItem(getHouse().getProjectName()));
        contractContext.getContractContextMap().put("c_1_9", new ContractContextMap.ContarctContextItem(sellCard.getCreatePrepareCardNumber()));
        contractContext.getContractContextMap().put("c_1_10", new ContractContextMap.ContarctContextItem(sellCard.getCreateCardNumber()));

        contractContext.getContractContextMap().put("c_2_1", new ContractContextMap.ContarctContextItem(dLogonInfo.getOrgName()));
        contractContext.getContractContextMap().put("c_2_2", new ContractContextMap.ContarctContextItem(sellCard.getCardNumber()));

        //house
        contractContext.getContractContextMap().put("c_3_1" , new ContractContextMap.ContarctContextItem(getHouse().getUseType()));
        contractContext.getContractContextMap().put("c_3_2", new ContractContextMap.ContarctContextItem(getHouse().getStructure()));
        contractContext.getContractContextMap().put("c_3_3", new ContractContextMap.ContarctContextItem(BigDecimal.valueOf(getHouse().getFloorCount())));
        contractContext.getContractContextMap().put("c_3_4" , new ContractContextMap.ContarctContextItem(BigDecimal.valueOf(getHouse().getUpFloorCount())));
        contractContext.getContractContextMap().put("c_3_5", new ContractContextMap.ContarctContextItem(BigDecimal.valueOf(getHouse().getDownFloorCount())));
        contractContext.getContractContextMap().put("c_3_6", new ContractContextMap.ContarctContextItem(getHouse().getBuildNo()));
        contractContext.getContractContextMap().put("c_3_8", new ContractContextMap.ContarctContextItem(getHouse().getHouseUnitName()));
        contractContext.getContractContextMap().put("c_3_9", new ContractContextMap.ContarctContextItem(getHouse().getInFloorName()));
        contractContext.getContractContextMap().put("c_3_10", new ContractContextMap.ContarctContextItem(getHouse().getHouseOrder()));
        contractContext.getContractContextMap().put("c_3_11", new ContractContextMap.ContarctContextItem(getHouse().getSaleBuild().getProjectSellCard().getMappingCropName()));
        contractContext.getContractContextMap().put("c_3_12", new ContractContextMap.ContarctContextItem(getHouse().getHouseArea()));
        contractContext.getContractContextMap().put("c_3_13", new ContractContextMap.ContarctContextItem(getHouse().getUseArea()));
        contractContext.getContractContextMap().put("c_3_14", new ContractContextMap.ContarctContextItem(getHouse().getCommArea()));


        //pleg
        List<PledgeInfo> pledgeInfos = getHouse().getPledgeInfoList();

        Logging.getLog(getClass()).debug("rledge count:" + ((pledgeInfos == null) ? "null" : "" + pledgeInfos.size()));
        if (pledgeInfos != null && !pledgeInfos.isEmpty()){


            List<ContractContextMap> pledgeInfoContext = new ArrayList<ContractContextMap>();
            for(PledgeInfo pledgeInfo: pledgeInfos){
                ContractContextMap pledgeInfoMap = new ContractContextMap();
                pledgeInfoMap.put("plg_type", new ContractContextMap.ContarctContextItem(pledgeInfo.getType()));
                pledgeInfoMap.put("plg_owner", new ContractContextMap.ContarctContextItem(pledgeInfo.getOwnerPersonName()));
                pledgeInfoMap.put("plg_corp", new ContractContextMap.ContarctContextItem(pledgeInfo.getPledgePersonName()));
                pledgeInfoMap.put("plg_org", new ContractContextMap.ContarctContextItem(pledgeInfo.getPledgeCorpName()));
                pledgeInfoMap.put("plg_begin", new ContractContextMap.ContarctContextItem(pledgeInfo.getBeginTime()));
                pledgeInfoMap.put("plg_end", new ContractContextMap.ContarctContextItem(pledgeInfo.getEndTime()));
                pledgeInfoContext.add(pledgeInfoMap);

            }

            contractContext.getContractContextMap().put("plg",new ContractContextMap.ContarctContextItem(pledgeInfoContext));

        }

        //price

        contractContext.getContractContextMap().put("c_6_1", new ContractContextMap.ContarctContextItem(BigDecimal.valueOf(calcType)));
        contractContext.getContractContextMap().put("money_type", new ContractContextMap.ContarctContextItem(moneyType));
        contractContext.getContractContextMap().put("total_price", new ContractContextMap.ContarctContextItem(getTotalMoney()));
        contractContext.getContractContextMap().put("unit_price", new ContractContextMap.ContarctContextItem(unitPrice));
        if (calcType == 4){
            contractContext.getContractContextMap().put("c_6_12", new ContractContextMap.ContarctContextItem(calcTypeName));
        }
    }

    public String commitContract(){

        try {
            contractContext.setContext(houseContractHome.getInstance().getContext());
        } catch (JSONException e) {
            facesMessages.addFromResourceBundle(StatusMessage.Severity.WARN,"ContractContextFail");
            return null;
        }


        return "view-contract-" + houseContractHome.getInstance().getType().getCurrentPatch();
    }

    public String contextComplete(){


        houseContractHome.getInstance().setContext(contractContext.getContext());
        for(String number: DeveloperSaleServiceImpl.instance().applyContractNumber(logonInfo,contractCount,houseContractHome.getInstance().getType())){
            houseContractHome.getInstance().getContractNumbers().add(new ContractNumber(number,houseContractHome.getInstance()));
        }
        return houseContractHome.update();
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
