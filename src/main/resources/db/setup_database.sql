-- TODO: rename these to bocean
create database trickle;

create user trickle with password 'trickle';

grant all privileges on database "trickle" to trickle;