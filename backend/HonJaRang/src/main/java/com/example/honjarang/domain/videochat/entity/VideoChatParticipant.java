package com.example.honjarang.domain.videochat.entity;


import com.example.honjarang.domain.BaseTimeEntity;
import com.example.honjarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
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

    @ColumnDefault("false")
    private Boolean isDeleted;

    @Builder
    public VideoChatParticipant(VideoChatRoom videoChatRoom, User user, Boolean isDeleted) {
        this.videoChatRoom = videoChatRoom;
        this.user = user;
        this.isDeleted = isDeleted;
    }



}
