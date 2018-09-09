package com.stoloto.surl.dao;

import com.stoloto.surl.model.UrlInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by kirio on 09.09.2018.
 */
public interface UrlRepository extends JpaRepository<UrlInfo, Long> {

    @Query("select u from urlInfo u where u.originalUrl = ?1")
    UrlInfo getByOriginalUrl(String url);

    @Query("select u from urlInfo u where u.shortUrl = ?1")
    UrlInfo getByShortUrl(String shortUrl);
}
