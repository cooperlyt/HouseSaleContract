package com.dgsoft.common.system;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cooper on 07/10/2016.
 */
@Name("imageHelper")
@AutoCreate
public class ImageHelper {

    private ByteArrayInputStream getImageFromServer(String serverUrl){

        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            URL url = new URL(serverUrl);

            url.openConnection().connect();

            BufferedImage image = ImageIO.read(url.openStream());


            ImageIO.write(image,"JPEG",outStream);
            byte[] imageInByte = outStream.toByteArray();
            outStream.close();
            return new ByteArrayInputStream(imageInByte);

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("url is fail");
        } catch (IOException e) {

            throw new IllegalArgumentException("url is fail");
        }
    }

    public ByteArrayInputStream getImage(String fid){
        return getImageFromServer(RunParam.instance().getParamValue("IMG_SERVER_ADDRESS") + "/img/orig/" + fid);
    }
}
