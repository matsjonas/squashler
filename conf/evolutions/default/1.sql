
# --- !Ups

create table game (
  id                        bigint not null,
  date                      timestamp,
  game_nbr                  integer,
  player_left_id            bigint,
  player_right_id           bigint,
  points_left               integer,
  points_right              integer,
  constraint pk_game primary key (id))
;

create table player (
  id                        bigint not null,
  name                      varchar(255),
  constraint uq_player_name unique (name),
  constraint pk_player primary key (id))
;

create sequence game_seq;

create sequence player_seq;

alter table game add constraint fk_game_playerLeft_1 foreign key (player_left_id) references player (id) on delete restrict on update restrict;
create index ix_game_playerLeft_1 on game (player_left_id);
alter table game add constraint fk_game_playerRight_2 foreign key (player_right_id) references player (id) on delete restrict on update restrict;
create index ix_game_playerRight_2 on game (player_right_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists game;

drop table if exists player;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists game_seq;

drop sequence if exists player_seq;

