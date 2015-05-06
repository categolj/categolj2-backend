CREATE TABLE config (
  config_name        VARCHAR(512)  NOT NULL,
  version            BIGINT,
  created_by         VARCHAR(128),
  created_date       DATETIME,
  last_modified_by   VARCHAR(128),
  last_modified_date DATETIME,
  config_value       VARCHAR(2048) NOT NULL,
  PRIMARY KEY (config_name)
)
  ENGINE = InnoDB ROW_FORMAT=DYNAMIC;


INSERT INTO config (config_name, config_value, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('BLOG_URL', 'http://blog.ik.am', 'admin', now(), 'admin', now());
INSERT INTO config (config_name, config_value, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('BLOG_TITLE', 'BLOG.IK.AM', 'admin', now(), 'admin', now());
INSERT INTO config (config_name, config_value, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('BLOG_DESCRIPTION', 'making''s memo', 'admin', now(), 'admin', now());
INSERT INTO config (config_name, config_value, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('API_ROOT', 'api/v1', 'admin', now(), 'admin', now());
INSERT INTO config (config_name, config_value, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('FRONTEND_ROOT', 'http://blog.ik.am', 'admin', now(), 'admin', now());