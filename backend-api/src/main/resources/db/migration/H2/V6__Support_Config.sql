CREATE TABLE config (
  config_name        VARCHAR(512)  NOT NULL,
  version            BIGINT,
  created_by         VARCHAR(128),
  created_date       TIMESTAMP,
  last_modified_by   VARCHAR(128),
  last_modified_date TIMESTAMP,
  config_value       VARCHAR(2048) NOT NULL,
  PRIMARY KEY (config_name)
);

INSERT INTO config (config_name, config_value, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('BLOG_URL', 'http://blog.ik.am', 'admin', now(), 'admin', now());
INSERT INTO config (config_name, config_value, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('BLOG_TITLE', 'BLOG_TITLE', 'admin', now(), 'admin', now());
INSERT INTO config (config_name, config_value, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('BLOG_DESCRIPTION', 'making''s memo', 'admin', now(), 'admin', now());
INSERT INTO config (config_name, config_value, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('API_ROOT', 'api/v1', 'admin', now(), 'admin', now());
INSERT INTO config (config_name, config_value, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('FRONTEND_ROOT', 'http://blog.ik.am', 'admin', now(), 'admin', now());