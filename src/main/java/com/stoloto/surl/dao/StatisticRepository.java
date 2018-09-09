package com.stoloto.surl.dao;

import com.stoloto.surl.model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by kirio on 09.09.2018.
 */
public interface StatisticRepository extends JpaRepository<Statistic, Long>{

    @Query("SELECT COUNT(u) FROM statistic as s " +
            "inner join s.urlInfo as u where u.shortUrl = ?1 ")
    Long countByShortUrl(String shortUrl);
}
