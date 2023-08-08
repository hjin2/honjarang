package com.example.honjarang.domain.videochat.entity;


import com.example.honjarang.domain.BaseTimeEntity;
import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@NoArgsConstructor
@Getter
@Entity
@DynamicInsert
public class VideoChatParticipant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "room_id")
    @ManyToOne
    private VideoChatRoom videoChatRoom;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    private Boolean isDeleted;


}
