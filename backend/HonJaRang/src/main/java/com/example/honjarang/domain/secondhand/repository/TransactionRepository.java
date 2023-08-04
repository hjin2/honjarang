package com.example.honjarang.domain.secondhand.repository;

import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.secondhand.dto.TransactionListDto;
import com.example.honjarang.domain.secondhand.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findAllByTitleContainingIgnoreCaseOrderByIdDesc(String keyword, Pageable pageable);

}
