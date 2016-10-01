package com.dgsoft.faces.convert;

import org.jboss.seam.contexts.Contexts;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by cooper on 12/28/15.
 */
@FacesConverter("timeConverter")
public class TimeConverter extends javax.faces.convert.DateTimeConverter{

    public TimeConverter()
    {
        setTimeZone( getTimeZone() );
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) {
        if (value == null || value.trim().equals("")){
            return null;
        }
        Date result = (Date) super.getAsObject(context,component,value);
        return result.getTime();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
                              Object value) {
        if (value instanceof Long) {
            return super.getAsString(context, component, new Date((Long) value));
        }else{
            return null;
        }
    }

    @Override
    public TimeZone getTimeZone()
    {
        if ( Contexts.isApplicationContextActive() )
        {
            return org.jboss.seam.international.TimeZone.instance();
        }
        else
        {
            return TimeZone.getDefault();
        }
    }
}
