drop table if exists member CASCADE;
create table member
(
    id       bigint auto_increment primary key,
    login_id varchar(20),
    email    varchar(20),
    password varchar(255),
    role     varchar(10)
);

drop table if exists party CASCADE;
create table party
(
    id         bigint auto_increment primary key,
    name       varchar(50),
    content    varchar(255),
    start_date DATE
);