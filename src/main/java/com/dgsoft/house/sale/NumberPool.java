package com.dgsoft.house.sale;

import com.dgsoft.developersale.LogonInfo;
import com.dgsoft.house.sale.model.*;
import com.dgsoft.house.sale.model.NumberRecord;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 * Created by cooper on 9/15/15.
 */
@Name("numberPool")
@AutoCreate
public class NumberPool {

    private static final String CONTRACT_NUMBER_TYPE ="CONTRACT";


    @In
    private EntityManager entityManager;

    @In
    private LogonInfo logonInfo;


    public String genContractNumber(){
        try {
            NumberRecord numberRecord = entityManager.createQuery("select numberPool from NumberRecord numberPool where numberPool.projectNumber.projectCode = :projectCode and numberPool.type =:poolType", NumberRecord.class)
                    .setParameter("projectCode", logonInfo.getGroupCode())
                    .setParameter("poolType", CONTRACT_NUMBER_TYPE).getSingleResult();
            if (numberRecord != null){
                long i =  numberRecord.getNumber() + 1;
                numberRecord.setNumber(i);
                return "N" + numberRecord.getProjectNumber().getNumber() + "-" + i;
            }
        }catch(NoResultException e){

        }
        ProjectNumber projectNumber = entityManager.createQuery("select p from ProjectNumber p where p.projectCode = :projectCode", ProjectNumber.class)
                .setParameter("projectCode",logonInfo.getGroupCode()).getSingleResult();

        entityManager.persist(new NumberRecord(projectNumber,CONTRACT_NUMBER_TYPE));

        return "N" + projectNumber.getNumber() + "-" + 1;

    }

    public static NumberPool instance(){
        return (NumberPool)Component.getInstance(NumberPool.class,true);
    }

}
