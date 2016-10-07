package com.dgsoft.house;

import cc.coopersoft.comm.District;
import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.house.sale.HouseSellService;
import com.dgsoft.common.system.PersonEntity;
import com.dgsoft.common.system.RunParam;
import com.dgsoft.house.PoolType;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Logging;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by cooper on 9/17/15.
 */
@Name("helpTools")
public class HelpTools {

    @In(value = "districtList",create = true)
    private List<District> districtList;

    public String getDistrictNameById(String id){
        for(District district: districtList){
            if (district.getId().equals(id)){
                return district.getName();
            }
        }
        return id;
    }

}
