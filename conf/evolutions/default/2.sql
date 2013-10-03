
# --- !Ups

create table game_group (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_game_group primary key (id))
;

alter table game add column game_group_id bigint;

create sequence game_group_seq;

alter table game add constraint fk_game_group_1 foreign key (game_group_id) references game_group (id) on delete restrict on update restrict;
create index ix_game_gameGroup_1 on game (game_group_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists game_group;

alter table game drop column game_group_id;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists game_group_seq;

