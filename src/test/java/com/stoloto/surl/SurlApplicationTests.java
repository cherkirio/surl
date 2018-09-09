package com.stoloto.surl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stoloto.surl.dao.UrlRepository;
import com.stoloto.surl.model.UrlInfo;
import com.stoloto.surl.service.IStatisticService;
import com.stoloto.surl.service.IUrlService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class SurlApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    UrlRepository urlRepository;

    @Autowired
    IStatisticService statisticService;
    @Autowired
    IUrlService urlService;

    @Test
    public void miniTest() throws Exception {


        String url = "http://127.0.0.1:8080/test/very_very_very_very_long_url?a=10&a=11&b=222&cd=hello";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("http://127.0.0.1:8080/generate").content(url))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();


        String urlInfoStr = mvcResult.getResponse().getContentAsString();
        final UrlInfo urlInfo = mapper.readValue(urlInfoStr, UrlInfo.class);

        mockMvc.perform(MockMvcRequestBuilders.get("http://127.0.0.1:8080/" + urlInfo.getShortUrl()))
                .andExpect(MockMvcResultMatchers.status().is(302)).
                andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult mvcResult) throws Exception {
                        Assert.assertEquals(mvcResult.getResponse().getHeader("Location"), url);
                    }

                    ;

                });


        UrlInfo byShortUrl = urlRepository.getByShortUrl(urlInfo.getShortUrl());
        Assert.assertNotNull(byShortUrl);

        Assert.assertEquals(byShortUrl.getOriginalUrl(), url);

        statisticService.saveStatisticsFromQueue();


        long accessCount = statisticService.countForShortUrl(byShortUrl.getShortUrl());
        Assert.assertEquals(accessCount, 1);

        Assert.assertEquals(statisticService.countForShortUrl("unknown"), 0);


    }
}