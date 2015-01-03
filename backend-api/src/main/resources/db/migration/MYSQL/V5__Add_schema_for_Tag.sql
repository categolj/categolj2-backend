CREATE TABLE tag (
  tag_name VARCHAR(255) NOT NULL,
  PRIMARY KEY (tag_name)
)
  ENGINE =InnoDB;

CREATE TABLE entry_tags (
  entry INTEGER      NOT NULL,
  tags  VARCHAR(255) NOT NULL,
  PRIMARY KEY (entry, tags),
  FOREIGN KEY (entry) REFERENCES entry (entry_id),
  FOREIGN KEY (tags) REFERENCES tag (tag_name)
)
  ENGINE =InnoDB;