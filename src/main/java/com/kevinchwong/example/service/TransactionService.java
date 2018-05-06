package com.kevinchwong.example.service;

import com.kevinchwong.example.dao.jpa.StatInfoRepository;
import com.kevinchwong.example.dao.jpa.TransactionRepository;
import com.kevinchwong.example.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static com.kevinchwong.example.Application.WINDOW_DURATION_MS;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private StatInfoRepository statInfoRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    CounterService counterService;

    public TransactionService() {
    }

    public Transaction createTransaction(Transaction transaction) {

        statInfoRepository.createByTimestampAndDurationAndAmount(transaction.getTimestamp(), WINDOW_DURATION_MS, transaction.getAmount());
        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(long id) {
        return transactionRepository.findOne(id);
    }

    public void deleteAllTransaction() {
        statInfoRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    public Page<Transaction> getAllTransactions(Integer page, Integer size) {
        Page pageOfTransactions = transactionRepository.findAll(new PageRequest(page, size));
        if (size > 50) {
            counterService.increment("Kevinchwong.TransactionService.getAll.largePayload");
        }
        return pageOfTransactions;
    }
}
