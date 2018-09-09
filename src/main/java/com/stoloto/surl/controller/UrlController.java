package com.stoloto.surl.controller;

import com.stoloto.surl.model.UrlInfo;
import com.stoloto.surl.service.IStatisticService;
import com.stoloto.surl.service.IUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kirio on 09.09.2018.
 */
@RestController
public class UrlController {
    @Autowired
    private IUrlService urlService;

    @Autowired
    private IStatisticService statisticService;


    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    UrlInfo generate(@RequestBody String url) throws Exception {
        return urlService.createUrlInfo(url);
    }


    @RequestMapping(value = "/{url}", method = RequestMethod.GET)
    void redirect(@PathVariable("url") String shortUrl, HttpServletResponse response) throws IOException, ServletException {

        String originalLink = urlService.getOriginalLink(shortUrl);

        if (originalLink == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            response.sendRedirect(originalLink);
            statisticService.saveStatisticAsync(shortUrl);
        }


    }

}
