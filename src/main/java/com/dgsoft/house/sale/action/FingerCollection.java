package com.dgsoft.house.sale.action;

import com.dgsoft.house.sale.model.BusinessPool;
import com.dgsoft.house.sale.model.PowerProxyPerson;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import java.util.ArrayList;
import java.util.List;

import static com.dgsoft.house.sale.model.BusinessPool.LegalType.LEGAL_OWNER;

/**
 * Created by cooper on 06/10/2016.
 */
@Name("fingerCollection")
public class FingerCollection {



    @In
    private HouseContractHome houseContractHome;

    private List<PersonFinger> personFingers;

    private List<PersonFinger> getPersonFingers() {
        if (personFingers == null){
            personFingers = new ArrayList<PersonFinger>();
            for(BusinessPool person: houseContractHome.getInstance().getBusinessPoolList()){
                boolean isSeller = BusinessPool.ContractPersonType.SELLER.equals(person.getContractPersonType());
                personFingers.add(new PersonFinger(FingerPersonType.MASTER,
                        person.getPersonName(),
                        person.getCredentialsType().isCorp() ? null : person.getFingerprint(),isSeller));
                if (person.getCredentialsType().isCorp() && LEGAL_OWNER.equals(person.getLegalType())){
                    personFingers.add(new PersonFinger(FingerPersonType.CORP_OWNER,
                            person.getLegalPerson(),
                            person.getFingerprint(),isSeller));
                }
                if (person.getPowerProxyPerson() != null){
                    personFingers.add(new PersonFinger(
                            PowerProxyPerson.ProxyType.ENTRUSTED.equals(person.getPowerProxyPerson().getProxyType()) ? FingerPersonType.PROXY_PERSON : FingerPersonType.LEGAL_PROXY_PERSON,
                            person.getPowerProxyPerson().getPersonName(),person.getPowerProxyPerson().getFingerprint(),isSeller));
                }
            }
        }
        return personFingers;
    }

    public List<PersonFinger> getNeedFingerImgPersons(){
        List<PersonFinger> result = new ArrayList<PersonFinger>();
        for(PersonFinger pf: getPersonFingers()){
            if (pf.getFingerCode() != null && !"".equals(pf.getFingerCode().trim())){
                result.add(pf);
            }
        }
        return result;
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

    public enum FingerPersonType{
        MASTER,CORP_OWNER,PROXY_PERSON,LEGAL_PROXY_PERSON
    }

    public class PersonFinger{

        private FingerPersonType type;
        private String name;
        private String fingerCode;
        private String fingerImageCode;
        private boolean seller;

        public PersonFinger(FingerPersonType type,
                            String name, String fingerCode, boolean seller) {
            this.type = type;
            this.name = name;
            this.fingerCode = fingerCode;
            this.seller = seller;
        }

        public FingerPersonType getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getFingerCode() {
            return fingerCode;
        }

        public String getFingerImageCode() {
            return fingerImageCode;
        }

        public void setFingerImageCode(String fingerImageCode) {
            this.fingerImageCode = fingerImageCode;
        }

        public boolean isSeller() {
            return seller;
        }
    }


}
