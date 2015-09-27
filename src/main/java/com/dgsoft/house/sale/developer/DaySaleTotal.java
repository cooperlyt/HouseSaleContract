package com.dgsoft.house.sale.developer;

import com.dgsoft.developersale.LogonInfo;
import com.dgsoft.house.sale.model.HouseContract;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import javax.persistence.EntityManager;
import java.util.*;

/**
 * Created by cooper on 9/21/15.
 */
@Name("daySaleTotalChart")
public class DaySaleTotal {

    private static final int TOTAL_LENGTH = 30;

    @In(create = true)
    private EntityManager entityManager;

    @In
    private LogonInfo logonInfo;

    private List<TimeCountTotalData> totalDatas;

    private boolean haveDay(Calendar calendar){

        for(TimeCountTotalData data: totalDatas){
            Calendar c = new GregorianCalendar(Locale.CHINA);
            c.setTime(data.getDate());
            if (c.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    c.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    c.get(Calendar.DATE) == calendar.get(Calendar.DATE)){
                return true;
            }
        }
        return false;
    }

    public List<TimeCountTotalData> getTotalDatas() {
        if (totalDatas == null){
            totalDatas = new ArrayList<TimeCountTotalData>(TOTAL_LENGTH);
            Calendar calendar = new GregorianCalendar(Locale.CHINA);
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);

            totalDatas = entityManager.createQuery("select new com.dgsoft.house.sale.developer.TimeCountTotalData(max(contract.createTime),count(contract.id)) from HouseContract contract where contract.projectCode =:groupCode and contract.createTime >= :createTime group by year(contract.createTime), month(contract.createTime), day(contract.createTime) order by contract.createTime", TimeCountTotalData.class)
                    .setParameter("groupCode",logonInfo.getGroupCode())
                    .setParameter("createTime", calendar.getTime()).getResultList();
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);

            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - TOTAL_LENGTH);

            while (calendar.getTime().getTime() < new Date().getTime()){
                if(!haveDay(calendar)){
                    totalDatas.add(new TimeCountTotalData(calendar.getTime(),Long.valueOf(0)));
                }
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);

            }


        }
        return totalDatas;
    }

}
