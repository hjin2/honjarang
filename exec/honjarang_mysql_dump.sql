create database honjarang
use honjarang
create table chat_room
(
    id         bigint auto_increment
        primary key,
    is_deleted bit default b'0' null,
    name       varchar(255)     not null
);

create table email_verification
(
    id          bigint auto_increment
        primary key,
    created_at  datetime(6)      null,
    updated_at  datetime(6)      null,
    code        varchar(255)     not null,
    email       varchar(255)     not null,
    expired_at  datetime(6)      not null,
    is_verified bit default b'0' null
);

create table store
(
    id         bigint       not null
        primary key,
    address    varchar(255) not null,
    image      varchar(255) not null,
    latitude   double       not null,
    longitude  double       not null,
    store_name varchar(255) not null
);

create table user
(
    id            bigint auto_increment
        primary key,
    created_at    datetime(6)                                          null,
    updated_at    datetime(6)                                          null,
    address       varchar(255)                                         not null,
    email         varchar(255)                                         not null,
    is_deleted    bit                              default b'0'        null,
    latitude      double                                               not null,
    longitude     double                                               not null,
    nickname      varchar(255)                                         not null,
    password      varchar(255)                                         not null,
    point         int                              default 0           null,
    profile_image varchar(255)                                         null,
    role          enum ('ROLE_ADMIN', 'ROLE_USER') default 'ROLE_USER' null
);

create table chat_participant
(
    id                   bigint auto_increment
        primary key,
    deleted_at           datetime(6)                                     null,
    is_deleted           bit          default b'0'                       null,
    last_read_message_id varchar(255) default '000000000000000000000000' null,
    chat_room_id         bigint                                          null,
    user_id              bigint                                          null,
    constraint FKj5uagkg4rkuuabepnkvsairma
        foreign key (user_id) references user (id),
    constraint FKqaqt420qk0puto2opt6st1u42
        foreign key (chat_room_id) references chat_room (id)
);

create table joint_delivery
(
    id               bigint auto_increment
        primary key,
    created_at       datetime(6)      null,
    updated_at       datetime(6)      null,
    content          varchar(255)     not null,
    deadline         datetime(6)      not null,
    delivery_charge  int              not null,
    is_canceled      bit default b'0' null,
    target_min_price int              not null,
    chat_room_id     bigint           null,
    store_id         bigint           null,
    user_id          bigint           null,
    constraint UK_n59ubdlsd574xrpjhgiscng3p
        unique (chat_room_id),
    constraint FK4pcf314s2j37t0dxru2d1629k
        foreign key (user_id) references user (id),
    constraint FKbikf0cgnqidycj5sg4ne9f415
        foreign key (store_id) references store (id),
    constraint FKbochild9to1yoox018qddkqpw
        foreign key (chat_room_id) references chat_room (id)
);

create table joint_delivery_applicant
(
    id                bigint auto_increment
        primary key,
    created_at        datetime(6)      null,
    updated_at        datetime(6)      null,
    is_received       bit default b'0' null,
    joint_delivery_id bigint           null,
    user_id           bigint           null,
    constraint FKdofqcgac7t9q7v51459wvffo3
        foreign key (user_id) references user (id),
    constraint FKejudj8geq7wgxc4951ev8d413
        foreign key (joint_delivery_id) references joint_delivery (id)
);

create table joint_delivery_cart
(
    id                bigint auto_increment
        primary key,
    menu_id           varchar(255) not null,
    quantity          int          not null,
    joint_delivery_id bigint       null,
    user_id           bigint       null,
    constraint FK71ugj19ijw2dieeyy4db6ay99
        foreign key (user_id) references user (id),
    constraint FKqbwmprlgpyvity3bp675jkp7g
        foreign key (joint_delivery_id) references joint_delivery (id)
);

create table joint_purchase
(
    id                  bigint auto_increment
        primary key,
    created_at          datetime(6)      null,
    updated_at          datetime(6)      null,
    content             varchar(255)     not null,
    deadline            datetime(6)      not null,
    delivery_charge     int              not null,
    image               varchar(255)     not null,
    is_canceled         bit default b'0' null,
    latitude            double           not null,
    longitude           double           not null,
    place_name          varchar(255)     not null,
    price               int              not null,
    product_name        varchar(255)     not null,
    target_person_count int              not null,
    chat_room_id        bigint           null,
    user_id             bigint           null,
    constraint UK_jaikugh43hmqfvgtato89g4rb
        unique (chat_room_id),
    constraint FK7q94nhy5hciglxpsqmd6m1469
        foreign key (user_id) references user (id),
    constraint FK89sk1llttucu6wf2grnx6517k
        foreign key (chat_room_id) references chat_room (id)
);

create table joint_purchase_applicant
(
    id                bigint auto_increment
        primary key,
    created_at        datetime(6)      null,
    updated_at        datetime(6)      null,
    is_received       bit default b'0' null,
    quantity          int              not null,
    joint_purchase_id bigint           null,
    user_id           bigint           null,
    constraint FK1rnkcad7h7mw8jqcjxktb6j7m
        foreign key (user_id) references user (id),
    constraint FKsor8hy4nxgcht6n5o637hyg9e
        foreign key (joint_purchase_id) references joint_purchase (id)
);

create table post
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)          null,
    updated_at datetime(6)          null,
    category   enum ('FREE', 'TIP') not null,
    content    text                 not null,
    is_notice  bit default b'0'     not null,
    post_image varchar(255)         null,
    title      varchar(255)         not null,
    views      int default 0        not null,
    user_id    bigint               null,
    constraint FK72mt33dhhs48hf9gcqrq4fxte
        foreign key (user_id) references user (id)
);

create table comment
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6) null,
    updated_at datetime(6) null,
    content    text        null,
    post_id    bigint      null,
    user_id    bigint      null,
    constraint FK8kcum44fvpupyw6f5baccx25c
        foreign key (user_id) references user (id)
            on delete cascade,
    constraint FKs1slvnkuemjsq2kj4h3vhx7i1
        foreign key (post_id) references post (id)
            on delete cascade
);

create table like_post
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6) null,
    updated_at datetime(6) null,
    post_id    bigint      not null,
    user_id    bigint      not null,
    constraint FKnu91sbh82a5nj1o3xh0sgwhu8
        foreign key (post_id) references post (id)
            on delete cascade,
    constraint FKrub77t61jwevsytkws4hqytqe
        foreign key (user_id) references user (id)
            on delete cascade
);

create table secondhand_transaction
(
    id          int auto_increment
        primary key,
    created_at  datetime(6)      null,
    updated_at  datetime(6)      null,
    buyer_id    bigint           null,
    content     text             null,
    is_complete bit default b'0' null,
    price       int              not null,
    title       text             null,
    user_id     bigint           null,
    constraint FK1gxm505qq99o2ugnd1bniwcjj
        foreign key (user_id) references user (id)
);

create table transaction
(
    id                bigint auto_increment
        primary key,
    created_at        datetime(6)      null,
    updated_at        datetime(6)      null,
    content           varchar(255)     not null,
    is_completed      bit default b'0' null,
    is_received       bit default b'0' null,
    price             int              not null,
    title             varchar(255)     not null,
    transaction_image varchar(255)     null,
    buyer_id          bigint           null,
    seller_id         bigint           null,
    constraint FKosd6qqlkyqp8gk4gjisggqev0
        foreign key (buyer_id) references user (id),
    constraint FKs37irexq9hyvl7pqyqya2i0dn
        foreign key (seller_id) references user (id)
);

create table video_chat_room
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    updated_at datetime(6)  null,
    category   tinyint      not null,
    only_voice bit          null,
    session_id varchar(255) not null,
    thumbnail  varchar(255) null,
    title      varchar(255) null,
    check (`category` between 0 and 5)
);

create table video_chat_participant
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)      null,
    updated_at datetime(6)      null,
    is_deleted bit default b'0' null,
    user_id    bigint           null,
    room_id    bigint           null,
    constraint FK82nnj4lwflwhev26c34ac4qf8
        foreign key (user_id) references user (id),
    constraint FKfyv5k2utdui340es55xd83dub
        foreign key (room_id) references video_chat_room (id)
);

