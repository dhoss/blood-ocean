create table videos(
  id int not null generated always as identity primary key,
  filename varchar(255),
  filename_hash varchar(6) not null unique,
  description text,
  filesize int not null,
  mime_type varchar(100) not null,
  created_on timestamptz not null default now(),
  updated_on timestamptz not null default now()
);

create index videos_created_on_idx on videos(created_on);