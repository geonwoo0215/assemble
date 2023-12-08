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
    role     varchar(10)
);

create table party
(
    id         bigint auto_increment primary key,
    name       varchar(50),
    content    varchar(255),
    start_date DATE
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
    party_id        bigint  NOT NULL,
    party_member_id bigint  NOT NULL,
    payer           tinyint NOT NULL,

    FOREIGN KEY (party_id) references party (id),
    FOREIGN KEY (party_member_id) references party_member (id)
)