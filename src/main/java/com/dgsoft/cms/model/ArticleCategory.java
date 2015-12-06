package com.dgsoft.cms.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cooper on 12/6/15.
 */
@Entity
@Table(name = "ARTICLE_CATEGORY", catalog = "CONTRACT")
public class ArticleCategory implements java.io.Serializable{

    public enum CategoryType{
        News,Events,Welcome,Download
    }

    private String id;

    private String name;

    private int pri;

    private CategoryType type;
    private Set<Article> articles = new HashSet<Article>(0);
    private String description;


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

    @Column(name = "NAME", nullable = false,length = 32)
    @NotNull
    @Size(max = 32)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "PRI", nullable = false)
    public int getPri() {
        return pri;
    }

    public void setPri(int pri) {
        this.pri = pri;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE",nullable = false,length = 16)
    @NotNull
    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "category", orphanRemoval = true)
    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    @Column(name = "DESCRIPTION", nullable = true,length = 512)
    @NotNull
    @Size(max = 512)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
