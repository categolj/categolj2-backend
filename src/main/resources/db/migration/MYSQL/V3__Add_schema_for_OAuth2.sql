CREATE TABLE oauth_access_token (
  token_id          VARCHAR(255),
  token             BLOB,
  authentication_id VARCHAR(255),
  user_name         VARCHAR(255),
  client_id         VARCHAR(255),
  authentication    BLOB,
  refresh_token     VARCHAR(255)
)
  ENGINE =InnoDB;

CREATE TABLE oauth_refresh_token (
  token_id       VARCHAR(255),
  token          BLOB,
  authentication BLOB
)
  ENGINE =InnoDB;

CREATE INDEX UK_AT_authentication_id ON oauth_access_token (authentication_id);
CREATE INDEX UK_AT_user_name ON oauth_access_token (user_name);
CREATE INDEX UK_AT_client_id ON oauth_access_token (client_id);
CREATE INDEX UK_AT_refresh_token ON oauth_access_token (refresh_token);