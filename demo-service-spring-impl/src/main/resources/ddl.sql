create table zones (
  id int(6) auto_increment not null primary key,
  name varchar2(32),
  max_size int(6) not null
);

create table planetary_systems (
  id int(6) auto_increment not null primary key,
  name varchar2(32),
  code varchar2(4) unique
);

create table starships (
  id int(8) auto_increment not null primary key,
  number varchar2(10) unique,
  planetary_system_id int(6),
  create_date datetime not null,
  time_count int(6) not null,
  constraint starships_ps_fk foreign key (planetary_system_id) references planetary_systems
);

create table zone_spaces (
  id int(8) auto_increment not null,
  zone_id int(6) not null,
  starship_id int(8) not null unique,
  create_date datetime not null,
  primary key (id),
  constraint zone_starship_zone_fk foreign key (zone_id) references zones,
  constraint zone_starship_starship_fk foreign key (starship_id) references starships,
);