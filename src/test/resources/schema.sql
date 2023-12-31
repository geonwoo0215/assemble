drop table if exists expense_comment CASCADE;
drop table if exists invitation CASCADE;
drop table if exists party_member_expense CASCADE;
drop table if exists expense CASCADE;
drop table if exists party_member CASCADE;
drop table if exists party CASCADE;
drop table if exists member CASCADE;

create table member
(
    id       bigint auto_increment primary key,
    login_id varchar(20),
    email    varchar(20),
    password varchar(255),
    nickname varchar(20),
    role     varchar(10)
);

create table party
(
    id         bigint auto_increment primary key,
    name       varchar(50),
    content    varchar(255),
    event_date timestamp
);

create table party_member
(
    id        bigint auto_increment primary key,
    party_id  bigint      NOT NULL,
    member_id bigint      NOT NULL,
    role      varchar(10) NOT NULL,

    FOREIGN KEY (party_id) REFERENCES party (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

create table expense
(
    id       bigint auto_increment primary key,
    party_id bigint      NOT NULL,
    price    int         NOT NULL,
    content  varchar(20) NOT NULL,

    FOREIGN KEY (party_id) REFERENCES party (id)
);

create table party_member_expense
(
    id              bigint auto_increment primary key,
    expense_id      bigint  NOT NULL,
    party_member_id bigint  NOT NULL,
    payer           tinyint NOT NULL,

    FOREIGN KEY (expense_id) references expense (id),
    FOREIGN KEY (party_member_id) references party_member (id)
);

create table invitation
(
    id           bigint auto_increment primary key,
    party_id     bigint NOT NULL,
    expired_date timestamp,
    invite_code  varchar(255),

    FOREIGN KEY (party_id) references party (id)
);

create table expense_comment
(
    id              bigint auto_increment primary key,
    expense_id      bigint NOT NULL,
    party_member_id bigint NOT NULL,
    comment         varchar(255),
    group_no        bigint NOT NULL,
    depth           bigint NOT NULL,
    comment_order   bigint NOT NULL,
    parent_id       bigint DEFAULT 0,

    FOREIGN KEY (expense_id) references expense (id),
    FOREIGN KEY (party_member_id) references party_member (id)
)