package com.dgsoft.cms.action;

import org.jboss.seam.annotations.Name;

/**
 * Created by cooper on 12/14/15.
 */
@Name("telList")
public class TelList extends ArticleQuery{

    private static final String EJBQL = "select a from Article a where a.category.type = 'TEL'";

    public TelList() {
        super();
        setMaxResults(null);
        setEjbql(EJBQL);

    }

}
