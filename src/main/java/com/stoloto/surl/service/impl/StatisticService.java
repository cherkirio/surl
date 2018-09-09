package com.stoloto.surl.service.impl;

import com.stoloto.surl.dao.StatisticRepository;
import com.stoloto.surl.dao.UrlRepository;
import com.stoloto.surl.model.Statistic;
import com.stoloto.surl.model.UrlInfo;
import com.stoloto.surl.service.IStatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by kirio on 09.09.2018.
 */
@Service
@Primary
public class StatisticService implements IStatisticService {


    private static Logger LOG = LoggerFactory.getLogger(StatisticService.class);

    @Value("${com.stoloto.statistic.queue.size:10000}")
    private int queueSize;


    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private StatisticRepository statisticRepository;


    private BlockingQueue<Statistic> queue;

    @PostConstruct
    void initQueue() {
        queue = new ArrayBlockingQueue<>(queueSize);
    }


    @Scheduled(fixedRateString = "${com.stoloto.statistic.save_timeout:2000}")
    @Override
    public void saveStatisticsFromQueue() {

        ArrayList<Statistic> statsToSave = new ArrayList<>();
        if (queue.drainTo(statsToSave) != 0) {
            LOG.debug("Save statistics {}", statsToSave.size());

            for (Statistic statistic : statsToSave) {
                UrlInfo urlInfo = urlRepository.getByShortUrl(statistic.getShortUrl());
                statistic.setUrlInfo(urlInfo);
                statisticRepository.save(statistic);
            }
        }

    }


    @Override
    public void saveStatisticAsync(String shortUrl) {
        Statistic stat = new Statistic(shortUrl);
        if (queue.offer(stat)) {
            return;
        }
        // queue full

        saveStatisticsFromQueue();
        if (!queue.offer(stat)) {
            LOG.error("statistic queue overflowed. Cant save statistic ({}:{})", stat.getShortUrl(), stat.getCreateDate());
        }
    }


    @Override
    public long countForShortUrl(String shortUrl) {
        return statisticRepository.countByShortUrl(shortUrl);
    }

}
