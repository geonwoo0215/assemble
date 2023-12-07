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