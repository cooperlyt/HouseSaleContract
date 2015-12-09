package com.dgsoft.cms.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by cooper on 12/6/15.
 */
@Entity
@Table(name = "ARTICLE", catalog = "CONTRACT")
public class Article implements java.io.Serializable{

    public enum ArticleViewType{
        PDF(true),WORD(true),APPLICATION(true),HTML(false),TEXT(false);

        //private String mineType;

        private boolean outView;

        public boolean isOutView() {
            return outView;
        }

        ArticleViewType(boolean outView) {
            this.outView = outView;
        }
    }

    private String id;
    private String mainTitle;
    private String subTitle;
    private Date publishTime;
    private String context;
    private boolean fixTop;
    private ArticleCategory category;
    private ArticleViewType viewType;
    private byte[] resource;


    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "MAIN_TITLE",nullable = false,length = 256)
    @NotNull
    @Size(max = 256)
    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    @Column(name = "SUB_TITLE", nullable = true, length = 1024)
    @Size(max = 1024)
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PUBLISH_TIME",nullable = false)
    @NotNull
    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTEXT", columnDefinition = "LONGTEXT")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Column(name = "FIX_TOP",nullable = false)
    public boolean isFixTop() {
        return fixTop;
    }

    public void setFixTop(boolean fixTop) {
        this.fixTop = fixTop;
    }

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "CATEGORY",nullable = false)
    @NotNull
    public ArticleCategory getCategory() {
        return category;
    }

    public void setCategory(ArticleCategory category) {
        this.category = category;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "VIEW_TYPE",length = 16, nullable = false)
    @NotNull
    public ArticleViewType getViewType() {
        return viewType;
    }

    public void setViewType(ArticleViewType viewType) {
        this.viewType = viewType;
    }


    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "RESOURCE",columnDefinition="blob")
    public byte[] getResource() {
        return resource;
    }

    public void setResource(byte[] resource) {
        this.resource = resource;
    }
}
