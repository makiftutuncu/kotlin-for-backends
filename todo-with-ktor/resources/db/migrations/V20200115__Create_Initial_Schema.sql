CREATE TABLE todo(
    id      IDENTITY      PRIMARY KEY,
    user_id BIGINT        NOT NULL,
    title   VARCHAR(128)  NOT NULL,
    details VARCHAR(1024),
    time    TIMESTAMP(3) WITH TIME ZONE
);
