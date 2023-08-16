package com.example.honjarang.domain.secondhandtransaction.repository;

import com.example.honjarang.domain.secondhandtransaction.entity.SecondhandTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecondhandTransactionRepository extends JpaRepository<SecondhandTransaction, Long > {

}
