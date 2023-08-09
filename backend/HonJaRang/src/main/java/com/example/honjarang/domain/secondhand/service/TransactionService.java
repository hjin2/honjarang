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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final S3Client s3Client;


    @Transactional
    public Long createSecondHandTransaction(TransactionCreateDto transactionCreateDto, MultipartFile transactionImage, User user) {

        String uuid = UUID.randomUUID().toString();

        try {
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket("honjarang-bucket")
                    .key("transactionImage/" + uuid + transactionImage.getOriginalFilename())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(transactionImage.getContentType())
                    .build(), RequestBody.fromInputStream(transactionImage.getInputStream(), transactionImage.getSize()));
        }catch (IOException e){
            throw new RuntimeException("게시글 이미지 업로드에 실패했습니다.");
        }

        String image = uuid + transactionImage.getOriginalFilename();
        return transactionRepository.save(transactionCreateDto.toEntity(user, image)).getId();
    }

    @Transactional
    public void updateSecondHandTransaction(MultipartFile transactionImage, TransactionUpdateDto transactionUpdateDto, User user) throws IOException {

        System.out.println(transactionUpdateDto.getId());
        Transaction transaction = transactionRepository.findById(transactionUpdateDto.getId()).orElseThrow(() -> new TransactionException("게시글이 없습니다."));
        if(!Objects.equals(transaction.getSeller().getId(),user.getId())){
            throw new InvalidUserException("작성자만 수정할 수 있습니다.");
        }

        if(transaction.getTransactionImage()!=""){
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket("honjarang-bucket")
                    .key("transaction/"+transaction.getTransactionImage())
                    .build();
            s3Client.deleteObject(request);
        }

        String uuid = UUID.randomUUID().toString();
        s3Client.putObject(PutObjectRequest.builder()
                .bucket("honjarang-bucket")
                .key("transaction/" + uuid + transactionImage.getOriginalFilename())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .contentType(transactionImage.getContentType())
                .build(), RequestBody.fromInputStream(transactionImage.getInputStream(), transactionImage.getSize()));

        String image = uuid + transactionImage.getOriginalFilename();
        transaction.update(transactionUpdateDto,image);

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
        TransactionDto transactionDto = new TransactionDto(transaction);
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
}
