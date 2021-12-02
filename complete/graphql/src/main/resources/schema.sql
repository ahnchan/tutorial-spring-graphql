create table product (
     id serial primary key,
     title varchar(200) not null,
     author varchar(100),
     ISBN varchar(20)
);

create table review (
    id serial primary key,
    productid int not null,
    reviewer varchar(100),
    text varchar(255)
);