package com.stoloto.surl.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by kirio on 09.09.2018.
 */
@Entity(name = "urlInfo")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "originalUrl", name = "idx_original_url"),
        @UniqueConstraint(columnNames = "shortUrl", name = "idx_short_url"),
        @UniqueConstraint(columnNames = "masterUrl", name = "idx_master_url")})
public class UrlInfo {
    @Id
    @GeneratedValue
    private Long id;


    private String originalUrl;
    private String shortUrl;
    private String masterUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    public UrlInfo() {
    }

    public UrlInfo(String originalUrl, String shortUrl, String masterUrl) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.masterUrl = masterUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
