package com.stoloto.surl.service.impl;

import com.stoloto.surl.dao.UrlRepository;
import com.stoloto.surl.model.UrlInfo;
import com.stoloto.surl.service.IUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by kirio on 09.09.2018.
 */
@Service
@Primary
public class UrlService implements IUrlService {


    private static Logger LOG = LoggerFactory.getLogger(UrlService.class);


    @Autowired
    private UrlService self; // for transaction purposes


    @Value("${com.stoloto.domain}")
    private String domain;


    @Value("${com.stoloto.shortUrl.baseLength:4}")
    private int shortUrlBaseLength;


    @Autowired
    private UrlRepository urlRepository;


    @Override
    public UrlInfo createUrlInfo(String originalUrl) throws Exception {
        checkUrlValid(originalUrl);

        UrlInfo link = urlRepository.getByOriginalUrl(originalUrl);
        if (link != null) {
            return link;
        }
        return createNewUrlInfo(originalUrl);
    }

    @Override
    public String getOriginalLink(String url) {

        UrlInfo link = urlRepository.getByShortUrl(url);
        return link == null ? null : link.getOriginalUrl();
    }


    private UrlInfo createNewUrlInfo(String originalUrl) {
        int attempts = 0;

        UrlInfo urlInfo = new UrlInfo(originalUrl, generateShortLink(shortUrlBaseLength), createMasterLink());

        UrlInfo saved = self.tryToCreateUrlInfo(urlInfo);
        while (saved == null) {
            urlInfo.setShortUrl(generateShortLink(shortUrlBaseLength));
            urlInfo.setMasterUrl(createMasterLink());
            saved = self.tryToCreateUrlInfo(urlInfo);
            attempts++;
            if (attempts > 10) {
                // throw excetprion or increase link length
            }
        }


        return saved;
    }


    private void checkUrlValid(String originalUrl) {
        // if invalid throw new InvalidLinkException("url invalid");
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    private UrlInfo tryToCreateUrlInfo(UrlInfo link) {

        if (urlRepository.getByShortUrl(link.getShortUrl()) != null) {
            LOG.warn("Short url '{}' already used", link.getShortUrl());
            return null;
        }

        return urlRepository.save(link);
    }


    private String generateShortLink(int lenght) {

        String uuid = UUID.randomUUID().toString();
        return (lenght >= uuid.length()
                ? uuid : uuid.substring(0, lenght));

    }

    private String createMasterLink() {
        return UUID.randomUUID().toString();
    }
}
