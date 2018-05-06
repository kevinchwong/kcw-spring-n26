package com.kevinchwong.example.service;

import com.kevinchwong.example.dao.jpa.StatInfoRepository;
import com.kevinchwong.example.domain.Statistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatInfoService {

    private static final Logger log = LoggerFactory.getLogger(StatInfoService.class);

    @Autowired
    private StatInfoRepository statInfoRepository;

    public Optional<Statistic> loadStatisticByTimestamp(long timestamp, int levelLimit) {
        return statInfoRepository.loadStatisticByTimestamp(timestamp, levelLimit);
    }

}
