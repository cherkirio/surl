package com.stoloto.surl.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by kirio on 09.09.2018.
 */
@Entity(name = "statistic")
@Table(indexes = {@Index(columnList = "url_id", name = "idx_url_id")})
public class Statistic {

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "url_id")
    @ManyToOne
    private UrlInfo urlInfo;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate = new Date();


    @Transient
    private String shorUrl;

    public Statistic() {
    }

    public Statistic(String shorUrl) {
        this.shorUrl = shorUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getShortUrl() {
        return shorUrl;
    }

    public UrlInfo getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(UrlInfo urlInfo) {
        this.urlInfo = urlInfo;
    }

}

