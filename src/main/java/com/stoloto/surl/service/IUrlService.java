package com.stoloto.surl.service;

import com.stoloto.surl.model.UrlInfo;

/**
 * Created by kirio on 09.09.2018.
 */
public interface IUrlService {
    UrlInfo createUrlInfo(String originalUrl) throws Exception;

    String getOriginalLink(String url);
}
