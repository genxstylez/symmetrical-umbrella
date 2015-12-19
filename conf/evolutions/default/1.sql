# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table Page (
  id                        serial not null,
  url                       varchar(255),
  short_url                 varchar(255),
  visit_count               integer,
  constraint uq_Page_url unique (url),
  constraint uq_Page_short_url unique (short_url),
  constraint pk_Page primary key (id))
;

create table Visit (
  id                        serial not null,
  ip                        varchar(255),
  count                     integer,
  page_id                   integer,
  constraint pk_Visit primary key (id))
;

alter table Visit add constraint fk_Visit_page_1 foreign key (page_id) references Page (id);
create index ix_Visit_page_1 on Visit (page_id);



# --- !Downs

drop table if exists Page cascade;

drop table if exists Visit cascade;

