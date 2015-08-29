package com.dgsoft;

import com.K1.biz.uitl.Base64;
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

    @In
    private FacesMessages facesMessages;

    @In
    private Credentials credentials;

    public boolean authenticate() {
        String keySeed = "A21D420F322842EBBAA323E7257E2772";

        try {
            return CheckHashValues(keySeed, rndData, credentials.getPassword());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage(),e);
        }

    }

    public boolean CheckHashValues(String Seed, String Random, String ClientDigest) throws NoSuchAlgorithmException {

        Logging.getLog(getClass()).debug("seed:" + Seed + ";random:" + Random + ";ClientDigest:" + ClientDigest);
        MessageDigest md = MessageDigest.getInstance("SHA1");
        String a = Random+Seed;
        byte[] serverDigest = md.digest(a.getBytes());
        byte[] clientDigest = Base64.decode(ClientDigest);

        return Arrays.equals(serverDigest, clientDigest);

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
