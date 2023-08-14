package com.example.honjarang.domain.secondhand.service;

import com.example.honjarang.domain.post.exception.InvalidUserException;
import com.example.honjarang.domain.secondhand.dto.TransactionCreateDto;
import com.example.honjarang.domain.secondhand.dto.TransactionDto;
import com.example.honjarang.domain.secondhand.dto.TransactionListDto;
import com.example.honjarang.domain.secondhand.dto.TransactionUpdateDto;
import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.example.honjarang.domain.secondhand.exception.TransactionException;
import com.example.honjarang.domain.secondhand.repository.TransactionRepository;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.InsufficientPointsException;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final S3Client s3Client;


    @Transactional
    public Long createSecondHandTransaction(TransactionCreateDto transactionCreateDto, MultipartFile transactionImage, User user) {

        String uuid = UUID.randomUUID().toString();
        String image = "";

        if(transactionImage!=null) {
            try {
                s3Client.putObject(PutObjectRequest.builder()
                        .bucket("honjarang-bucket")
                        .key("transactionImage/" + uuid + transactionImage.getOriginalFilename())
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .contentType(transactionImage.getContentType())
                        .build(), RequestBody.fromInputStream(transactionImage.getInputStream(), transactionImage.getSize()));
            } catch (IOException e) {
                throw new RuntimeException("게시글 이미지 업로드에 실패했습니다.");
            }

           image =  uuid + transactionImage.getOriginalFilename();
        }
        return transactionRepository.save(transactionCreateDto.toEntity(user, image)).getId();
    }

    @Transactional
    public void updateSecondHandTransaction(MultipartFile transactionImage, TransactionUpdateDto transactionUpdateDto, User user, Long transactionId) throws IOException {

        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionException("게시글이 없습니다."));
        if (!Objects.equals(transaction.getSeller().getId(), user.getId())) {
            throw new InvalidUserException("작성자만 수정할 수 있습니다.");
        }

        String uuid = UUID.randomUUID().toString();

        String image = "";
        // 기존에 게시글에 사진이 있었고 사용자가 사진을 첨부했을 때
        // 기존의 사진을 삭제하고 새로운 사진 추가
        if (transaction.getTransactionImage() != null && transactionImage != null) {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket("honjarang-bucket")
                    .key("transactionImage/" + transaction.getTransactionImage())
                    .build();
            s3Client.deleteObject(request);

            // 사진추가
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket("honjarang-bucket")
                    .key("transactionImage/" + uuid + transactionImage.getOriginalFilename())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(transactionImage.getContentType())
                    .build(), RequestBody.fromInputStream(transactionImage.getInputStream(), transactionImage.getSize()));
            image = uuid + transactionImage.getOriginalFilename();
        }

        // 기존에 게시글에 사진이 있었고 사용자가 사진을 첨부하지 않았을 때
        // 그냥 놔둬야 됨, 아무것도 건들이지 않음
        // 아예 parameter를 없애야 성공적으로 됨
        if (transaction.getTransactionImage() != null && transactionImage == null) {
            image = transaction.getTransactionImage();
        }


        // 기존에 사진이 없었고 사용자가 사진을 첨부했을 때
        // 사진 추가와 글 업데이트
        if (transaction.getTransactionImage() == null && transactionImage != null) {
            // 사진추가
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket("honjarang-bucket")
                    .key("transactionImage/" + uuid + transactionImage.getOriginalFilename())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(transactionImage.getContentType())
                    .build(), RequestBody.fromInputStream(transactionImage.getInputStream(), transactionImage.getSize()));

            image = uuid + transactionImage.getOriginalFilename();

        }

        // 기존에 사진이 없었고 사용자가 사진을 첨부하지 않았을 때
        // 그냥 글만 업데이트 하면됨
        if (transaction.getTransactionImage() == null && transactionImage == null) {
            image = null;
        }

        transaction.update(transactionUpdateDto, image);

    }

    @Transactional
    public void deleteSecondHandTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionException("해당 게시글이 없습니다."));
        transactionRepository.delete(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionListDto> getSecondHandTransactions(Integer page, Integer size, String keyword) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        List<Transaction> transactionList = transactionRepository.findAllByTitleContainingIgnoreCaseOrderByIdDesc(keyword, pageable);
        List<TransactionListDto> transactionListDtos = new ArrayList<>();
        for (Transaction tmp : transactionList) {
            TransactionListDto transactionListDto = new TransactionListDto(tmp);
            transactionListDtos.add(transactionListDto);
        }
        return transactionListDtos;
    }

    @Transactional(readOnly = true)
    public TransactionDto getSecondHandTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionException("존재하지 않는 거래입니다."));
        Long buyerId = transaction.getBuyer().getId();
        TransactionDto transactionDto = new TransactionDto(transaction, buyerId);
        return transactionDto;
    }

    @Transactional
    public void buySecondHandTransaction(Long transactionId, User user) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionException("존재하지 않는 거래입니다."));
        if (user.getPoint() < transaction.getPrice()) {
            throw new InsufficientPointsException("포인트가 부족합니다.");
        }
        User loginedUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        loginedUser.subtractPoint(transaction.getPrice());
        transaction.soldout(user);
        transaction.complete();
    }

    @Transactional
    public void checkSecondHandTransaction(Long transactionId, User user) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionException("존재하지 않는 거래입니다."));
        transaction.receive();
        User seller = userRepository.findById(transaction.getSeller().getId()).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        seller.addPoint(transaction.getPrice());
    }

    @Transactional(readOnly = true)
    public Integer getTransactionsPageCount(Integer size, String keyword) {
        return (int) Math.ceil((double) transactionRepository.countAllByTitleContainingIgnoreCase(keyword) / size) ;
    }

}
