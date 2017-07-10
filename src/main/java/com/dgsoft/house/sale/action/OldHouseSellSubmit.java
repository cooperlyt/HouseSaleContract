package com.dgsoft.house.sale.action;

import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.house.sale.HouseSellService;
import com.dgsoft.common.system.PersonHelper;
import com.dgsoft.common.system.RunParam;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Logging;

/**
 * Created by cooper on 9/22/16.
 */
@Name("oldHouseSellSubmit")
@Scope(ScopeType.CONVERSATION)
public class OldHouseSellSubmit {
//
//    @In
//    private FacesMessages facesMessages;
//
//    private OldHouseQueryData oldHouseQueryData;
//
//    private PersonHelper<HouseQueryData> houseQueryData = new PersonHelper<HouseQueryData>(new HouseQueryData());
//
//    public PersonHelper<HouseQueryData> getHouseQueryData() {
//        return houseQueryData;
//    }
//
//    private OldHouseSell oldHouseSell;
//
//
//    public void setHouseLocateTypeName(String name){
//        houseQueryData.getPersonEntity().setHouseLocateType(HouseQueryData.HouseLocateType.valueOf(name));
//    }
//
//    public String getHouseLocateTypeName(){
//        return houseQueryData.getPersonEntity().getHouseLocateType().name();
//    }
//
//    public String submitHouseIndex(){
//        try {
//            OldHouseQueryResult result = HouseSellService.querySellHouse(RunParam.instance().getParamValue("nginx_address"),houseQueryData.getPersonEntity());
//
//            if(OldHouseQueryResult.ResultStatus.OK.equals(result.getStatus())){
//
//
//                oldHouseQueryData = result.getHouse();
//                oldHouseSell = new OldHouseSell(houseQueryData.getPersonEntity());
//
//            }else if (OldHouseQueryResult.ResultStatus.EXISTS.equals(result.getStatus())){
//                //TODO
//            }else{
//                throw new IllegalArgumentException("Unkonw status:" + result.getStatus());
//            }
//
//
//            return result.getStatus().name();
//        } catch (HttpApiServerException e) {
//            Logging.getLog(getClass()).warn("status is:" + e.getHttpStatus(),e);
//            switch (e.getHttpStatus()){
//
//                case 404 :
//                    facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,"query_old_house_not_found");
//
//                    break;
//                default:
//                    facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,"query_old_house_error");
//
//                    break;
//
//            }
//            return null;
//        }
//    }
//
//    public OldHouseQueryData getOldHouseQueryData() {
//        return oldHouseQueryData;
//    }
}
