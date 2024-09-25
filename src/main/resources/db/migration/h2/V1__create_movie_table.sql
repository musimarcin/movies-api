create sequence movie_id_seq start with 1 increment by 50;
create table movies (
    id bigint default nextval('movie_id_seq') not null,
    title varchar(255) not null,
    release_year int not null,
    added_when timestamp,
    primary key(id)
);