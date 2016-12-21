package com.dgsoft.house.sale.action;

import cc.coopersoft.house.ProxyType;
import cc.coopersoft.house.sale.data.PowerPerson;
import cc.coopersoft.house.sale.data.PowerProxyPerson;
import com.dgsoft.common.system.PersonEntity;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.dgsoft.common.system.PowerPersonEntity.LegalType.LEGAL_OWNER;


/**
 * Created by cooper on 06/10/2016.
 */
@Name("fingerCollection")
@Scope(ScopeType.SESSION)
public class FingerCollection {

    private String contractNumber;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        if (this.contractNumber == null || !this.contractNumber.equals(contractNumber)){
            clear();
        }
        this.contractNumber = contractNumber;
    }

    @In
    private FacesMessages facesMessages;

    @In
    private HouseContractHome houseContractHome;

    private List<PersonFinger> personFingers;

    private List<PersonFinger> getPersonFingers() {
        if (personFingers == null){
            personFingers = new ArrayList<PersonFinger>();
            for(PowerPerson person: houseContractHome.getInstance().getBusinessPoolList()){
                boolean isSeller = PowerPerson.ContractPersonType.SELLER.equals(person.getContractPersonType());

                if(person.getPowerProxyPerson() != null){
                    personFingers.add(new PersonFinger(
                            ProxyType.ENTRUSTED.equals(person.getPowerProxyPerson().getProxyType()) ? FingerPersonType.PROXY_PERSON : FingerPersonType.LEGAL_PROXY_PERSON,
                            person.getPowerProxyPerson(),isSeller));
                }else if (!person.getCredentialsType().isCorp()){
                    personFingers.add(new PersonFinger(FingerPersonType.MASTER,
                            person,isSeller));
                }
                //公司 没有代理人不验证指纹
            }
        }
        return personFingers;
    }

    public List<PersonFinger> getNeedFingerImgPersons(){
        return getPersonFingers();
    }

    private List<PersonFinger> getPersonFingerByType(FingerPersonType type){
        List<PersonFinger> result = new ArrayList<PersonFinger>();
        for(PersonFinger pf: getPersonFingers()){
            if (type.equals(pf.getType())){
                result.add(pf);
            }
        }
        return result;
    }

    public List<PersonFinger> getMasterFingers(){
        return getPersonFingerByType(FingerPersonType.MASTER);
    }

    public List<PersonFinger> getCorpOwnerFingers(){
        return getPersonFingerByType(FingerPersonType.CORP_OWNER);
    }

    public List<PersonFinger> getPorxyPersonFingers(){
        return getPersonFingerByType(FingerPersonType.PROXY_PERSON);
    }

    public List<PersonFinger> getLegalProxyPersonFingers(){
        return getPersonFingerByType(FingerPersonType.LEGAL_PROXY_PERSON);
    }

    public boolean isFingerComplete(){
        for (PersonFinger personFinger: getNeedFingerImgPersons()){
            if (personFinger.getFingerImageCode() == null || personFinger.getFingerImageCode().trim().equals("")){
                return false;
            }
        }
        return true;
    }

    public String validAndPrintContract(){

        if (isFingerComplete()){
            return houseContractHome.printSingleContract();
        }else{
            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,"fingerNotInput");
            return null;
        }
    }

    public void clear(){
        personFingers = null;
    }

    public enum FingerPersonType{
        MASTER,CORP_OWNER,PROXY_PERSON,LEGAL_PROXY_PERSON
    }


    public class PersonFinger{

        private FingerPersonType type;
        private PersonEntity person;

        private String fingerImageCode;
        private boolean seller;

        public PersonFinger(FingerPersonType type, PersonEntity person, boolean seller) {
            this.type = type;
            this.person = person;
            this.seller = seller;
        }

        public FingerPersonType getType() {
            return type;
        }

        public String getName() {
            return person.getPersonName();
        }

        public String getFingerCode(){ return PersonEntity.CredentialsType.MASTER_ID.equals(person.getCredentialsType()) ? person.getCredentialsNumber() : person.getCredentialsType().name().replace("_","").substring(0,3) + person.getCredentialsNumber();}

        public String getFingerImageCode() {
            return fingerImageCode;
        }

        public void setFingerImageCode(String fingerImageCode) {
            this.fingerImageCode = fingerImageCode;
        }

        public boolean isSeller() {
            return seller;
        }

        public ByteArrayInputStream getFingerImg() {

            BASE64Decoder decoder = new BASE64Decoder();
            try {

                byte[] imageByte = decoder.decodeBuffer(getFingerImageCode().substring(getFingerImageCode().indexOf(",") + 1));
                ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                BufferedImage image = ImageIO.read(bis);
                bis.close();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", outStream);
                byte[] imageInByte = outStream.toByteArray();
                outStream.close();

                return new ByteArrayInputStream(imageInByte);

            } catch (IOException e) {
                throw new IllegalArgumentException("img convert fail");
            }


        }
    }


    public static FingerCollection instance()
    {
        if ( !Contexts.isEventContextActive() )
        {
            throw new IllegalStateException("no active event context");
        }
        return (FingerCollection) Component.getInstance(FingerCollection.class, ScopeType.SESSION);
    }


}
