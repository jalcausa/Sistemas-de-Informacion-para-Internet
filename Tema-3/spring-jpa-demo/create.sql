create sequence book_seq start with 1 increment by 50;
create table book (id bigint not null, isbn varchar(255), nombre varchar(255), primary key (id));
create table book_etiquetas (book_id bigint not null, etiquetas varchar(255));
alter table book_etiquetas add constraint FKdf5om74surofqord1qa8rx64c foreign key (book_id) references book;
create sequence book_seq start with 1 increment by 50;
create table book (id bigint not null, isbn varchar(255), nombre varchar(255), primary key (id));
create table book_etiquetas (book_id bigint not null, etiquetas varchar(255));
alter table book_etiquetas add constraint FKdf5om74surofqord1qa8rx64c foreign key (book_id) references book;
