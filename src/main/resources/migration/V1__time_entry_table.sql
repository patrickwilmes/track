create table time_entry
(
    id           INTEGER
        primary key autoincrement,
    project_name TEXT not null,
    description  TEXT not null,
    start        TEXT not null,
    end          TEXT not null,
    day          DATE not null
);

