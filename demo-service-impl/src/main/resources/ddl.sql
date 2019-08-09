create table zones (
  id int(6) auto_increment not null,
  name varchar2(32),
  max_size int(6) not null,
  primary key (id)
);

create table planetary_systems (
  id int(6) auto_increment not null,
  name varchar2(32),
  code varchar2(4),
  primary key (id)
);

create table starships (
  id int(8) auto_increment not null,
  number varchar2(10),
  planetary_system_id int(6),
  create_date datetime not null,
  time_count int(6) not null,
  primary key (id),
  constraint starships_ps_fk foreign key (planetary_system_id) references planetary_systems
);