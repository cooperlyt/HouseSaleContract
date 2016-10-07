package com.dgsoft.house.sale.contract;

import com.dgsoft.common.system.DictionaryService;
import com.dgsoft.common.system.PersonEntity;
import com.dgsoft.developersale.*;
import com.dgsoft.house.PledgeInfo;
import com.dgsoft.house.SaleType;
import com.dgsoft.house.sale.DeveloperSaleServiceImpl;
import com.dgsoft.common.system.RunParam;
import com.dgsoft.house.sale.action.HouseContractHome;
import com.dgsoft.house.sale.model.BusinessPool;
import com.dgsoft.house.sale.model.ContractNumber;
import com.dgsoft.house.sale.model.ContractTemplate;
import com.dgsoft.house.sale.model.HouseContract;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Logging;
import org.json.JSONException;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by cooper on 9/15/15.
 */
@Name("contractCreate")
@Scope(ScopeType.CONVERSATION)
public class ContractCreate {

    @In(create = true)
    private HouseContractHome houseContractHome;

    @In
    private FacesMessages facesMessages;

    @In
    private LogonInfo logonInfo;

    @In
    private Map<String, String> messages;

    @In(create = true)
    private DictionaryService dictionary;

    private ContractTemplate contractTemplate;

    private BigDecimal unitPrice;

    private String moneyType = "人民币";

    private String calcTypeName;

    private int calcType = 1;

    private Locale locale = Locale.CHINA;

    //private Integer contractCount;

    private SaleHouse house;

    private String houseCode;

    public SaleHouse getHouse() {
        if (house == null || !house.getHouseCode().equals(houseCode) ){
            if (houseCode == null || houseCode.trim().equals("")){
                house = null;
            }else {
                house = DeveloperSaleServiceImpl.instance().getHouseInfoBySale((DeveloperLogonInfo) logonInfo, houseCode);
                if (house.getStatus().isCanSale()){
                    if (houseContractHome.getEntityManager().createQuery("select count(contract.id) from HouseContract contract where contract.houseCode = :houseCode and contract.status = 'PREPARE' and contract.groupId = :groupId and contract.type in (:types)", Long.class)
                            .setParameter("houseCode",house.getHouseCode())
                            .setParameter("types", EnumSet.of(SaleType.MAP_SELL,SaleType.NOW_SELL))
                            .setParameter("groupId",logonInfo.getGroupCode()).getSingleResult().compareTo(Long.valueOf(0)) > 0){
                        house.setStatus(SaleStatus.PREPARE_CONTRACT);
                    }
                }
            }

        }
        return house;
    }

    public boolean isHouseCanSale(){

        if (getHouse() == null){
            return false;
        }
        if (getHouse().getInBiz()){
            return false;
        }
        if (SaleStatus.PROJECT_PLEDGE.equals(getHouse().getStatus())){
            return RunParam.instance().getParamValue("allow_project_pledge_sale").equals("yes");
        }

        return getHouse().getStatus().isCanSale();


    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String createContract(){

        if (SaleStatus.PREPARE_CONTRACT.equals(getHouse().getStatus())){
            houseContractHome.setId(houseContractHome.getEntityManager()
                    .createQuery("select hc from HouseContract hc where hc.houseCode=:houseCode and hc.type in (:types) and hc.groupId = :groupId and hc.status = :status", HouseContract.class)
                    .setParameter("houseCode",getHouse().getHouseCode()).setParameter("groupId",logonInfo.getGroupCode())
                    .setParameter("status",HouseContract.ContractStatus.PREPARE)
                    .setParameter("types", EnumSet.of(SaleType.MAP_SELL,SaleType.NOW_SELL)).getSingleResult().getId());

            return "edit-contract-" + RunParam.instance().getParamValue("CONTRACT_LOCATION") + houseContractHome.getInstance().getType().getCurrentPatch();

        }

        if (SaleStatus.CAN_SALE.equals(getHouse().getStatus())){
            try {
                houseContractHome.getEntityManager().remove(
                houseContractHome.getEntityManager()
                        .createQuery("select hc from HouseContract hc where hc.houseCode=:houseCode and hc.type in (:types) and hc.groupId = :groupId and hc.status in (:status)", HouseContract.class)
                        .setParameter("houseCode", getHouse().getHouseCode())
                        .setParameter("status",EnumSet.of(HouseContract.ContractStatus.SUBMIT,HouseContract.ContractStatus.RECORD))
                        .setParameter("groupId",logonInfo.getGroupCode())
                        .setParameter("types", EnumSet.of(SaleType.MAP_SELL, SaleType.NOW_SELL)).getSingleResult());
                houseContractHome.getEntityManager().flush();
            }catch (NoResultException e){

            }
        }

        houseContractHome.clearInstance();
        houseContractHome.getInstance().setHouseCode(houseCode);
        houseContractHome.getInstance().setHouseArea(getHouse().getHouseArea());

        houseContractHome.getInstance().getNewHouseContract().setProjectCode(getHouse().getProjectCode());

        houseContractHome.getInstance().getNewHouseContract().setProjectCerNumber(getHouse().getSaleBuild().getProjectSellCard().getCardNumber());

        houseContractHome.getInstance().setHouseDescription(getHouse().getBuildName() + " " + getHouse().getHouseOrder());

        houseContractHome.getInstance().setType(getHouse().getSaleType());
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

    public enum MoneyCalcType{
        MONEY_FULL_MONEY,MONEY_ROUND_DOWN,MONEY_ROUND_HALF_UP
    }

    private MoneyCalcType moneyCalcType = MoneyCalcType.MONEY_FULL_MONEY;

    public MoneyCalcType getMoneyCalcType() {
        return moneyCalcType;
    }

    public void setMoneyCalcType(MoneyCalcType moneyCalcType) {
        this.moneyCalcType = moneyCalcType;
    }

    public MoneyCalcType[] getMoneyCalcTypes(){
        return MoneyCalcType.values();
    }

    public BigDecimal getTotalMoney(){

        if (getUnitPrice() == null){
            return null;
        }

        BigDecimal result;
        switch (calcType){
            case 1:
                result = getHouse().getUseArea().multiply(getUnitPrice());
                break;
            case 2:
                result = getHouse().getHouseArea().multiply(getUnitPrice());
                break;
            default:
                result = getUnitPrice();
                break;
        }

        switch (moneyCalcType){
            case MONEY_ROUND_DOWN:
                return result.setScale(0,BigDecimal.ROUND_DOWN);
            case MONEY_ROUND_HALF_UP:
                return result.setScale(0,BigDecimal.ROUND_HALF_UP);
            default:
                return result.setScale(2,BigDecimal.ROUND_HALF_UP);
        }

    }

    @Transactional
    public String fillContractContext(){

        if (contractTemplate != null){
            try {
                houseContractHome.setContext(contractTemplate.getContext());
            } catch (JSONException e) {
                facesMessages.addFromResourceBundle(StatusMessage.Severity.WARN,"ContractTemplateFail");
                houseContractHome.clearContext();
            }
        }
        Logging.getLog(getClass()).debug("fill sale info");
        genContractContext();

        if ("persisted".equals(houseContractHome.persist())) {
            //contractContext.
            return "edit-contract-" + RunParam.instance().getParamValue("CONTRACT_LOCATION") + houseContractHome.getInstance().getType().getCurrentPatch();
        }

        return null;
    }

    private void genContractContext() {
        DeveloperLogonInfo dLogonInfo = (DeveloperLogonInfo) logonInfo;

        //出卖人
        houseContractHome.getContractContextMap().put("dname",new ContractContextMap.ContarctContextItem(getHouse().getSaleBuild().getProjectSellCard().getDeveloperName()));
        houseContractHome.getContractContextMap().put("daddress", new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getAddress()));
        houseContractHome.getContractContextMap().put("dpost", new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getPostCode()));
        houseContractHome.getContractContextMap().put("dln",new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getLicenseNumber()));
        houseContractHome.getContractContextMap().put("dln2", new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getCerCode()));
        houseContractHome.getContractContextMap().put("downer", new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getOwnerPerson()));
        houseContractHome.getContractContextMap().put("downertel",new ContractContextMap.ContarctContextItem(dLogonInfo.getAttachCorpInfo().getOwnerTel()));
        houseContractHome.getContractContextMap().put("dproxy",new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getSaleProxyPerson().getPersonName()));
        houseContractHome.getContractContextMap().put("dproxytel",new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getSaleProxyPerson().getTel()));

        houseContractHome.getContractContextMap().put("dproxycertype",new ContractContextMap.ContarctContextItem(messages.get(houseContractHome.getInstance().getSaleProxyPerson().getCredentialsType().name())));
        houseContractHome.getContractContextMap().put("dproxycernumber",new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getSaleProxyPerson().getCredentialsNumber()));
        houseContractHome.getContractContextMap().put("houseCode",new ContractContextMap.ContarctContextItem(getHouse().getHouseCode()));
        houseContractHome.getContractContextMap().put("houseDisplayCode",new ContractContextMap.ContarctContextItem(getHouse().getDisplayHouseCode()));



        //买受人
//        houseContractHome.getContractContextMap().put("owner",new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getContractOwner().getPersonName()));
//
//        if (PersonEntity.CredentialsType.COMPANY_CODE.equals(houseContractHome.getInstance().getContractOwner().getCredentialsType())){
//            houseContractHome.getContractContextMap().put("ownertype", new ContractContextMap.ContarctContextItem(messages.get(houseContractHome.getInstance().getContractOwner().getLegalType().name())));
//        }
//        if(PersonEntity.CredentialsType.PASSPORT.equals(houseContractHome.getInstance().getContractOwner().getCredentialsType())){
//            houseContractHome.getContractContextMap().put("ownercitytype",new ContractContextMap.ContarctContextItem(messages.get("ContractOwner_country")));
//        }else{
//            houseContractHome.getContractContextMap().put("ownercitytype",new ContractContextMap.ContarctContextItem(messages.get("ContractOwner_rootAddress")));
//        }
//        houseContractHome.getContractContextMap().put("ownercityname", new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getContractOwner().getRootAddress()));
//        houseContractHome.getContractContextMap().put("ownertypevalue", new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getContractOwner().getLegalPerson()));
//        houseContractHome.getContractContextMap().put("ownercertype", new ContractContextMap.ContarctContextItem(messages.get(houseContractHome.getInstance().getContractOwner().getCredentialsType().name())));
//        houseContractHome.getContractContextMap().put("ownercernumber", new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getContractOwner().getCredentialsNumber()));
//        houseContractHome.getContractContextMap().put("owneraddress", new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getContractOwner().getAddress()));
//        houseContractHome.getContractContextMap().put("ownertel", new ContractContextMap.ContarctContextItem(houseContractHome.getInstance().getContractOwner().getPhone()));


        switch (houseContractHome.getInstance().getPoolType()) {

            case SINGLE_OWNER:
                houseContractHome.getContractContextMap().put("ownerpooltype",new ContractContextMap.ContarctContextItem(BigDecimal.ZERO));
                break;
            case TOGETHER_OWNER:
                houseContractHome.getContractContextMap().put("ownerpooltype",new ContractContextMap.ContarctContextItem(new BigDecimal("2")));
                break;
            case SHARE_OWNER:
                houseContractHome.getContractContextMap().put("ownerpooltype",new ContractContextMap.ContarctContextItem(BigDecimal.ONE));
                break;
        }
        houseContractHome.getContractContextMap().put("houseaddress",new ContractContextMap.ContarctContextItem(getHouse().getAddress()));


        List<ContractContextMap> poolOwners = new ArrayList<ContractContextMap>();
        for(BusinessPool poolOwner: houseContractHome.getInstance().getBusinessPoolList()){
            ContractContextMap poolInfoMap = new ContractContextMap();
            poolInfoMap.put("owner", new ContractContextMap.ContarctContextItem(poolOwner.getPersonName()));

            if (PersonEntity.CredentialsType.COMPANY_CODE.equals(poolOwner.getCredentialsType())){
                poolInfoMap.put("ownertype", new ContractContextMap.ContarctContextItem(messages.get(poolOwner.getLegalType().name())));
                poolInfoMap.put("ownertypevalue", new ContractContextMap.ContarctContextItem(poolOwner.getLegalPerson()));
            }
            if(PersonEntity.CredentialsType.PASSPORT.equals(poolOwner.getCredentialsType())){
                poolInfoMap.put("ownercitytype", new ContractContextMap.ContarctContextItem(messages.get("ContractOwner_country")));
            }else{
                poolInfoMap.put("ownercitytype", new ContractContextMap.ContarctContextItem(messages.get("ContractOwner_rootAddress")));
            }
            poolInfoMap.put("ownercityname", new ContractContextMap.ContarctContextItem(poolOwner.getRootAddress()));


            poolInfoMap.put("ownercertype", new ContractContextMap.ContarctContextItem(messages.get(poolOwner.getCredentialsType().name())));
            poolInfoMap.put("ownercernumber", new ContractContextMap.ContarctContextItem(poolOwner.getCredentialsNumber()));

            poolInfoMap.put("ownerbirthday", new ContractContextMap.ContarctContextItem(poolOwner.getBirthday()));
            poolInfoMap.put("ownersex", new ContractContextMap.ContarctContextItem(messages.get(poolOwner.getSex().name())));
            poolInfoMap.put("owneraddress", new ContractContextMap.ContarctContextItem(poolOwner.getAddress()));
            poolInfoMap.put("ownerpost", new ContractContextMap.ContarctContextItem(poolOwner.getPostCode()));

            poolInfoMap.put("poolRelation", new ContractContextMap.ContarctContextItem(dictionary.getWordValue(poolOwner.getRelation())));
            poolInfoMap.put("poolPerc", new ContractContextMap.ContarctContextItem(poolOwner.getPoolPerc()));
            poolInfoMap.put("ownertel", new ContractContextMap.ContarctContextItem(poolOwner.getPhone()));

            if (poolOwner.getPaperCopyInfo() != null && !"".equals(poolOwner.getPaperCopyInfo()));
                poolInfoMap.put("ownerpaperinfo", new ContractContextMap.ContarctContextItem(poolOwner.getPaperCopyInfo()));

            if (poolOwner.getPowerProxyPerson() != null){
                poolInfoMap.put("ownerproxytype", new ContractContextMap.ContarctContextItem(messages.get(poolOwner.getPowerProxyPerson().getProxyType().name())));

                poolInfoMap.put("ownerproxyname", new ContractContextMap.ContarctContextItem(poolOwner.getPowerProxyPerson().getPersonName()));

                if(PersonEntity.CredentialsType.PASSPORT.equals(poolOwner.getPowerProxyPerson().getCredentialsType())){
                    poolInfoMap.put("ownerproxycitytype", new ContractContextMap.ContarctContextItem(messages.get("ContractOwner_country")));
                }else{
                    poolInfoMap.put("ownerproxycitytype", new ContractContextMap.ContarctContextItem(messages.get("ContractOwner_rootAddress")));
                }

                poolInfoMap.put("ownerproxyrootaddress", new ContractContextMap.ContarctContextItem(poolOwner.getPowerProxyPerson().getRootAddress()));
                poolInfoMap.put("ownerproxycertype", new ContractContextMap.ContarctContextItem(messages.get(poolOwner.getPowerProxyPerson().getCredentialsType().name())));
                poolInfoMap.put("ownerproxycernumber", new ContractContextMap.ContarctContextItem(poolOwner.getPowerProxyPerson().getCredentialsNumber()));
                poolInfoMap.put("ownerproxybirthday", new ContractContextMap.ContarctContextItem(poolOwner.getPowerProxyPerson().getBirthday()));
                poolInfoMap.put("ownerproxysex", new ContractContextMap.ContarctContextItem(messages.get(poolOwner.getPowerProxyPerson().getSex().name())));
                poolInfoMap.put("ownerproxyaddress", new ContractContextMap.ContarctContextItem(poolOwner.getPowerProxyPerson().getAddress()));
                poolInfoMap.put("ownerproxypost", new ContractContextMap.ContarctContextItem(poolOwner.getPowerProxyPerson().getPostCode()));
                poolInfoMap.put("ownerproxytel", new ContractContextMap.ContarctContextItem(poolOwner.getPowerProxyPerson().getPhone()));
                if (poolOwner.getPowerProxyPerson().getPaperCopyInfo() != null && !"".equals(poolOwner.getPowerProxyPerson().getPaperCopyInfo()));
                    poolInfoMap.put("ownerproxypaperinfo",new ContractContextMap.ContarctContextItem(poolOwner.getPowerProxyPerson().getPaperCopyInfo()));
            }




            poolOwners.add(poolInfoMap);
        }

        Logging.getLog(getClass()).debug("put pool count:" + poolOwners.size());

        houseContractHome.getContractContextMap().put("poolOwner", new ContractContextMap.ContarctContextItem(poolOwners));

        // sale card

        ProjectSellCard sellCard = getHouse().getSaleBuild().getProjectSellCard();

        houseContractHome.getContractContextMap().put("c_1_1", new ContractContextMap.ContarctContextItem(sellCard.getLandGetMode()));
        houseContractHome.getContractContextMap().put("c_1_2", new ContractContextMap.ContarctContextItem(sellCard.getLandAddress()));
        houseContractHome.getContractContextMap().put("c_1_3", new ContractContextMap.ContarctContextItem(sellCard.getLandCardType()));
        houseContractHome.getContractContextMap().put("c_1_4", new ContractContextMap.ContarctContextItem(sellCard.getLandCardNumber()));
        houseContractHome.getContractContextMap().put("c_1_5", new ContractContextMap.ContarctContextItem(sellCard.getLandArea()));
        houseContractHome.getContractContextMap().put("c_1_6", new ContractContextMap.ContarctContextItem(sellCard.getLandUseType()));
        if (getHouse().getLandEndUseTime() != null && getHouse().getLandEndUseTime().getTime() != 0)
            //Logging.getLog(getClass()).debug("end time:" + sellCard.getLandEndUseTime().getTime());
            houseContractHome.getContractContextMap().put("c_1_7", new ContractContextMap.ContarctContextItem(getHouse().getLandEndUseTime()));

        houseContractHome.getContractContextMap().put("c_1_8", new ContractContextMap.ContarctContextItem(getHouse().getProjectName()));
        houseContractHome.getContractContextMap().put("c_1_9", new ContractContextMap.ContarctContextItem(sellCard.getCreatePrepareCardNumber()));
        houseContractHome.getContractContextMap().put("c_1_10", new ContractContextMap.ContarctContextItem(sellCard.getCreateCardNumber()));

        houseContractHome.getContractContextMap().put("c_2_1", new ContractContextMap.ContarctContextItem(dLogonInfo.getOrgName()));


        if (getHouse().getSaleType().equals(SaleType.MAP_SELL)) {
            houseContractHome.getContractContextMap().put("c_2_2", new ContractContextMap.ContarctContextItem(sellCard.getCardNumber()));
        }else if (getHouse().getOwnerCardNumber() != null){
            houseContractHome.getContractContextMap().put("c_2_2", new ContractContextMap.ContarctContextItem(getHouse().getOwnerCardNumber()));
        }

        //house
        houseContractHome.getContractContextMap().put("c_3_1" , new ContractContextMap.ContarctContextItem(getHouse().getUseType()));
        houseContractHome.getContractContextMap().put("c_3_2", new ContractContextMap.ContarctContextItem(getHouse().getStructure()));

        if (!BigDecimal.ZERO.equals(BigDecimal.valueOf(getHouse().getFloorCount()))) {
            houseContractHome.getContractContextMap().put("c_3_3", new ContractContextMap.ContarctContextItem(BigDecimal.valueOf(getHouse().getFloorCount())));
            houseContractHome.getContractContextMap().put("c_3_4", new ContractContextMap.ContarctContextItem(BigDecimal.valueOf(getHouse().getUpFloorCount())));
            houseContractHome.getContractContextMap().put("c_3_5", new ContractContextMap.ContarctContextItem(BigDecimal.valueOf(getHouse().getDownFloorCount())));
        }

        houseContractHome.getContractContextMap().put("c_3_6", new ContractContextMap.ContarctContextItem(getHouse().getBuildNo()));
        houseContractHome.getContractContextMap().put("c_3_8", new ContractContextMap.ContarctContextItem(getHouse().getHouseUnitName()));
        houseContractHome.getContractContextMap().put("c_3_9", new ContractContextMap.ContarctContextItem(getHouse().getInFloorName()));
        houseContractHome.getContractContextMap().put("c_3_10", new ContractContextMap.ContarctContextItem(getHouse().getHouseOrder()));
        houseContractHome.getContractContextMap().put("c_3_11", new ContractContextMap.ContarctContextItem(getHouse().getSaleBuild().getProjectSellCard().getMappingCropName()));
        houseContractHome.getContractContextMap().put("c_3_12", new ContractContextMap.ContarctContextItem(getHouse().getHouseArea()));
        houseContractHome.getContractContextMap().put("c_3_13", new ContractContextMap.ContarctContextItem(getHouse().getUseArea()));
        houseContractHome.getContractContextMap().put("c_3_14", new ContractContextMap.ContarctContextItem(getHouse().getCommArea()));
        houseContractHome.getContractContextMap().put("houseAddress", new ContractContextMap.ContarctContextItem(getHouse().getAddress()));
        houseContractHome.getContractContextMap().put("c_geluo",new ContractContextMap.ContarctContextItem(getHouse().getLoftArea()));

                //pay Type
        switch (houseContractHome.getInstance().getNewHouseContract().getSalePayType()){

            case ALL_PAY:
                houseContractHome.getContractContextMap().put("c_7_4", new ContractContextMap.ContarctContextItem("1"));
                break;
            case PART_PAY:
                houseContractHome.getContractContextMap().put("c_7_4", new ContractContextMap.ContarctContextItem("2"));
                break;
            case DEBIT_PAY:
                houseContractHome.getContractContextMap().put("c_7_4", new ContractContextMap.ContarctContextItem("3"));
                break;
            case OTHER_PAY:
                houseContractHome.getContractContextMap().put("c_7_4", new ContractContextMap.ContarctContextItem("4"));
                break;
        }

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

            houseContractHome.getContractContextMap().put("plg",new ContractContextMap.ContarctContextItem(pledgeInfoContext));

        }

        //price

        houseContractHome.getContractContextMap().put("c_6_1", new ContractContextMap.ContarctContextItem(BigDecimal.valueOf(calcType)));
        houseContractHome.getContractContextMap().put("money_type", new ContractContextMap.ContarctContextItem(moneyType));
        houseContractHome.getContractContextMap().put("total_price", new ContractContextMap.ContarctContextItem(getTotalMoney()));
        houseContractHome.getContractContextMap().put("unit_price", new ContractContextMap.ContarctContextItem(unitPrice));
        if (calcType == 4){
            houseContractHome.getContractContextMap().put("c_6_12", new ContractContextMap.ContarctContextItem(calcTypeName));
        }
    }

    private Integer contractCount;
    private String removeContractNumber;

    public Integer getContractCount() {
        return contractCount;
    }

    public void setContractCount(Integer contractCount) {
        this.contractCount = contractCount;
    }

    public String getRemoveContractNumber() {
        return removeContractNumber;
    }

    public void setRemoveContractNumber(String removeContractNumber) {
        this.removeContractNumber = removeContractNumber;
    }


    public String removeContract(){
        for(ContractNumber contractNumber: houseContractHome.getInstance().getContractNumbers()){
            if (contractNumber.getContractNumber().equals(removeContractNumber)){
                houseContractHome.getInstance().getContractNumbers().remove(contractNumber);
                return houseContractHome.update();
            }
        }
        return null;
    }

    private void addContractToCount(){
        Logging.getLog(getClass()).debug("add number to:" + contractCount);

        int count = ((contractCount == null) ? 1 : contractCount) - houseContractHome.getInstance().getContractNumbers().size();
        if (count > 0)
            for(String number: DeveloperSaleServiceImpl.instance().applyContractNumber(logonInfo,count,houseContractHome.getInstance().getType())){
                houseContractHome.getInstance().getContractNumbers().add(new ContractNumber(number,houseContractHome.getInstance()));
            }

    }

    public String contextComplete(){
        addContractToCount();
        return houseContractHome.update();
    }

    @Transactional
    public String beginContract(){
        houseContractHome.getInstance().setPrice(getTotalMoney());
        houseContractHome.getInstance().setContractVersion(houseContractHome.getInstance().getType().getCurrentVersion());
        //if (!PoolType.SINGLE_OWNER.equals(houseContractHome.getPoolType())) {


        houseContractHome.genPoolOwner();
        return "persisted-poolOwner";
       // }else{
       //     return fillContractContext();
       // }
    }



}
