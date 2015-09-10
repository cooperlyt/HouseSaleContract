package com.dgsoft;

import com.K1.biz.uitl.Base64;
import com.dgsoft.developersale.DeveloperSaleServiceImpl;
import com.dgsoft.developersale.LogonInfo;
import com.dgsoft.developersale.LogonStatus;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Credentials;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by cooper on 8/29/15.
 */
@Name("authenticationManager")
public class AuthenticationManager {


    @In(required = false,scope = ScopeType.SESSION)
    @Out(required = false,scope = ScopeType.SESSION)
    private String rndData;


    @Out(required = false, scope = ScopeType.SESSION)
    private LogonInfo logonInfo;

    @In
    private FacesMessages facesMessages;

    @In
    private Credentials credentials;


    public boolean authenticate() {

            try {
                logonInfo = DeveloperSaleServiceImpl.instance().logon(credentials.getUsername(), credentials.getPassword(), rndData);
                if (logonInfo == null) {
                    facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "ConnectHouseOrgServerError");
                    return false;
                } else
                    switch (logonInfo.getLogonStatus()) {

                        case KEY_NOT_FOUND:
                            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "LogonStatus_KEY_NOT_FOUND");
                            return false;
                        case PASSWORD_ERROR:
                            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "LogonStatus_PASSWORD_ERROR");
                            return false;
                        case TYPE_ERROR:
                            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "LogonStatus_TYPE_ERROR");
                            return false;
                        case EMP_DISABLE:
                            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "LogonStatus_EMP_DISABLE");
                            return false;
                        case CORP_DISABLE:
                            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "LogonStatus_CORP_DISABLE");
                            return false;
                        case CORP_OUT_TIME:
                            facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "LogonStatus_CORP_OUT_TIME");
                            return false;
                        case LOGON:
                            return true;
                    }

                return false;
            }catch (Exception e){
                Logging.getLog(getClass()).error(e.getMessage(),e);
                facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "ConnectHouseOrgServerError");
                return false;
            }
    }


    public void genLogonRnd(){
        rndData = "";
        int b ;
        int a ;
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < 32; i++) {
            a = r.nextInt(26);
            b = (char) (a + 65);
            rndData += new Character((char) b).toString();
        }
    }

    public String getEncodeRndData(){
        return new String(Base64.encode(rndData.getBytes()));
    }


}
