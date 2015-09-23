package com.dgsoft.house.sale.developer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cooper on 9/21/15.
 */
public class TimeCountTotalData implements java.io.Serializable{

    private Date date;

    private Long count;

    public TimeCountTotalData(Date date, Long count) {
        this.date = date;
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public Long getCount() {
        return count;
    }

    public String getTitle(){
        SimpleDateFormat sf=new SimpleDateFormat("dd");
        return  sf.format(date);
    }
}
