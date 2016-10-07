package com.dgsoft.common.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by cooper on 07/10/2016.
 */
@Name("personCardCopyGen")
@AutoCreate
public class PersonCardCopyGen {

    private static final int X_OFFSET = 317;
    private static final int Y_OFFSET = 0;

    @In
    private FacesContext facesContext;

    @In
    private ImageHelper imageHelper;

    @In
    private Map<String, String> messages;

    public static class PersonCardInfo{

        private String name;
        private String sex;
        private String ethnic;
        private String dateOfBirth;
        private String address;
        private String number;
        private String org;
        private String validDateBegin;
        private String validDateEnd;
        private String imgId;

        public PersonCardInfo() {
        }

        public PersonCardInfo(String name, String sex, String ethnic, String dateOfBirth, String address, String number, String org, String validDateBegin, String validDateEnd) {
            this.name = name;
            this.sex = sex;
            this.ethnic = ethnic;
            this.dateOfBirth = dateOfBirth;
            this.address = address;
            this.number = number;
            this.org = org;
            this.validDateBegin = validDateBegin;
            this.validDateEnd = validDateEnd;
        }

        private Calendar getBirthDay() {
            SimpleDateFormat sdf  =   new SimpleDateFormat( "yyyy-MM-dd" );
            Calendar ca = Calendar.getInstance();
            try {
                ca.setTime(sdf.parse(getDateOfBirth()));
            } catch (ParseException e) {
                throw new IllegalArgumentException("dateOfBirth not format date:" + getDateOfBirth());
            }
            return ca;
        }

        public int getYearOfBirth(){
            return getBirthDay().get(Calendar.YEAR);
        }

        public int getMonthOfBirth(){
            return getBirthDay().get(Calendar.MONTH) + 1;
        }

        public int getDayOfBirth(){
            return getBirthDay().get(Calendar.DAY_OF_MONTH);
        }

        public Date getValidBeginDate(){
            SimpleDateFormat sdf  =   new SimpleDateFormat( "yyyy-MM-dd" );
            try {
                return sdf.parse(getValidDateBegin());
            } catch (ParseException e) {
                throw new IllegalArgumentException("getValidBeginDate not format date:" + getDateOfBirth());
            }
        }

        public Date getValidEndDate(){
            SimpleDateFormat sdf  =   new SimpleDateFormat( "yyyy-MM-dd" );
            try {
                return sdf.parse(getValidDateEnd());
            } catch (ParseException e) {
                throw new IllegalArgumentException("getValidEndDate not format date:" + getDateOfBirth());
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getEthnic() {
            return ethnic;
        }

        public void setEthnic(String ethnic) {
            this.ethnic = ethnic;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getOrg() {
            return org;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public String getValidDateBegin() {
            return validDateBegin;
        }

        public void setValidDateBegin(String validDateBegin) {
            this.validDateBegin = validDateBegin;
        }

        public String getValidDateEnd() {
            return validDateEnd;
        }

        public void setValidDateEnd(String validDateEnd) {
            this.validDateEnd = validDateEnd;
        }

        @JsonProperty("img_fid")
        public String getImgId() {
            return imgId;
        }

        public void setImgId(String imgId) {
            this.imgId = imgId;
        }
    }

    class WhiteFilter extends RGBImageFilter {
        public WhiteFilter() {
            canFilterIndexColorModel = true;
        }
        public int filterRGB(int x, int y, int rgb) {

            int color = rgb & 0xFFFFFF;
            return (color <= 16991422 && color >= 16001422)  ? 0:rgb;
        }
    }

    public ByteArrayInputStream getPersonCardCopy(String infos){

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            PersonCardInfo person = mapper.readValue(infos,PersonCardInfo.class);


            BufferedInputStream backBIS = new BufferedInputStream(facesContext.getExternalContext().getResourceAsStream("/img/idb1.png"));
            Image backImage = ImageIO.read(backBIS);

            //读取头像图片
            BufferedInputStream headBIS = new BufferedInputStream(  imageHelper.getImage(person.getImgId()));
            Image headImage = ImageIO.read(headBIS);

            RGBImageFilter imageFilter = new WhiteFilter();
            ImageProducer imageProducer = headImage.getSource();
            imageProducer = new FilteredImageSource(imageProducer, imageFilter);
            headImage = Toolkit.getDefaultToolkit().createImage(imageProducer);


            if (backImage == null)
                throw new IllegalArgumentException("background is null");
            int backAlphaType = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha(backImage)) {
                backAlphaType = BufferedImage.TYPE_INT_ARGB;
            }

            //画图
            BufferedImage backgroundImage = new BufferedImage(backImage.getWidth(null), backImage.getHeight(null), backAlphaType);
            Graphics2D g = backgroundImage.createGraphics();

            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g.drawImage(backImage, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_ATOP, 1));
            g.drawImage(headImage, 197, 20, headImage.getWidth(null),headImage.getHeight(null), null);



            g.setFont(new Font("STHeiti", Font.PLAIN, 13)); //设置字体
            g.setColor(Color.BLACK); //设置颜色
            //g.drawRect(0, 0, width - 1, height - 1); //画边框
            g.drawString(person.getName(), 60 ,38);

            g.setFont(new Font("STHeiti", Font.PLAIN, 12));
            g.drawString(messages.get(person.getSex()), 60 ,64);

            g.drawString(person.getEthnic(), 128 ,64);

            int lineCount = 150 / 15;
            int pos = 115;

            String address = person.getAddress();
            while (address.length() > 0){
                if (address.length() <= lineCount){
                    g.drawString(address, 60 ,pos);
                    address = "";
                }else{
                    g.drawString(address.substring(0,lineCount), 60 ,pos);
                    address = address.substring(lineCount);
                }
                pos += 16;

            }


            g.setFont(new Font("STHeiti", Font.PLAIN, 12));
            g.drawString(String.valueOf(person.getYearOfBirth()), 60 ,89);
            g.drawString(String.valueOf(person.getMonthOfBirth()), person.getMonthOfBirth() > 10 ? 108 : 112 ,89);
            g.drawString(String.valueOf(person.getDayOfBirth()), person.getDayOfBirth() > 10 ? 138 : 142 ,89);


            g.setFont(new Font("OCR-B 10 BT", Font.BOLD, 15));
            g.drawString(person.getNumber(), 116 ,178);


            g.setFont(new Font("STHeiti", Font.PLAIN, 10));
            g.drawString(person.getOrg(), X_OFFSET + 136 ,Y_OFFSET + 153);


            SimpleDateFormat sdf  =   new SimpleDateFormat( "yyyy.MM.dd" );

            String validDateString = sdf.format(person.getValidBeginDate()) + "-" + sdf.format(person.getValidEndDate());
            g.drawString(validDateString, X_OFFSET + 136 ,Y_OFFSET + 180);

            //输出
            byte[] imageInByte;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(backgroundImage, "png", baos);
            imageInByte = baos.toByteArray();
            baos.close();

            return new ByteArrayInputStream(imageInByte);

        } catch (IOException e) {
            throw new IllegalArgumentException("info error:",e);
        }

    }

    /**
     * 是否开启alpha通道
     * @param image
     * @return
     */
    public boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }
}
