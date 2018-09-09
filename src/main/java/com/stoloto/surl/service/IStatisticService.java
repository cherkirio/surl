package com.stoloto.surl.service;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by kirio on 09.09.2018.
 */
public interface IStatisticService {
    @Scheduled(fixedRateString = "${com.stoloto.statistic.save_timeout:2000}")
    void saveStatisticsFromQueue();

    void saveStatisticAsync(String shortUrl);

    long countForShortUrl(String shortUrl);
}
