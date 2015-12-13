package com.dgsoft.cms.action;

import org.jboss.seam.annotations.Name;

/**
 * Created by cooper on 12/13/15.
 */
@Name("downloadList")
public class DownloadList extends ArticleQuery{


    private static final String EJBQL = "select a from Article a where a.category.type = 'Download'";


    public DownloadList() {
        super();
        setEjbql(EJBQL);
    }

}
