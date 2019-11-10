
CREATE TABLE hibernate_sequence (id INT NOT NULL);
INSERT INTO hibernate_sequence VALUES (0);
UPDATE hibernate_sequence SET id=LAST_INSERT_ID(id+1);

create table message (
    id int8 not null,
    filename varchar(255),
    tag varchar(255),
    text varchar(2048) not null,
    user_id int8,
    primary key (id)
);

create table user_role (
    user_id int8 not null,
    roles varchar(255)
);

create table usr (
    id int8 not null,
    activation_code varchar(255),
    active boolean not null,
    email varchar(255),
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
);

CREATE TABLE message_user_fk (
    user_id int8 NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id)  REFERENCES usr(id)
);

CREATE TABLE user_role_user_fk (
    user_id int8 NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id)  REFERENCES usr(id)
);

insert into usr (id, username, password, active)
    values (1, 'admin', '123', true);

insert into user_role (user_id, roles)
    values (1, 'USER'), (1, 'ADMIN');


update usr set password = MD5(password)