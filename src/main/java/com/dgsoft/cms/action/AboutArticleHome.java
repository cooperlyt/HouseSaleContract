package com.dgsoft.cms.action;

import com.dgsoft.cms.model.Article;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import javax.persistence.NoResultException;

/**
 * Created by cooper on 12/8/15.
 */
@Name("aboutArticleHome")
public class AboutArticleHome extends EntityHome<Article>{

    @Override
    public void create(){
        super.create();
        try {
            setId(getEntityManager().createQuery("select a from Article a where a.category.type = 'Welcome'", Article.class).getSingleResult().getId());
        }catch (NoResultException e){
            clearInstance();
        }
    }



}
