create table videos(
  id int not null generated always as identity primary key,
  filename varchar(255) not null,
  filename_hash varchar(128) not null unique,
  path varchar(138) not null unique, -- e.g.: /77/07/1baa4df0c5e48df0987cced5013fd454
  description text,
  filesize int not null,
  mime_type varchar(100) not null,
  created_on timestamptz not null default now(),
  updated_on timestamptz not null default now()
);