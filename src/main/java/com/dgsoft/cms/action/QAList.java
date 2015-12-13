package com.dgsoft.cms.action;

import org.jboss.seam.annotations.Name;

/**
 * Created by cooper on 12/13/15.
 */
@Name("QAList")
public class QAList extends ArticleQuery{

    private static final String EJBQL = "select a from Article a where a.category.type = 'QA'";


    public QAList() {
        super();
        setEjbql(EJBQL);
    }
}
