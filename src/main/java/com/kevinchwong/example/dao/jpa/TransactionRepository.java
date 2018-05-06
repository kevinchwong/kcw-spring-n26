package com.kevinchwong.example.dao.jpa;

import com.kevinchwong.example.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;


public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
    Page findAll(Pageable pageable);
}
