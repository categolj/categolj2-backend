CREATE TABLE entry_tags (
  entry INTEGER      NOT NULL,
  tags  VARCHAR(255) NOT NULL,
  PRIMARY KEY (entry, tags)
);

CREATE TABLE tag (
  tag_name VARCHAR(255) NOT NULL,
  PRIMARY KEY (tag_name)
);

ALTER TABLE entry_tags
ADD CONSTRAINT FK_a4axhndj52pjnbyd5dg66q0gu
FOREIGN KEY (tags)
REFERENCES tag;

ALTER TABLE entry_tags
ADD CONSTRAINT FK_5h7a631jgq73p168bbhp4528p
FOREIGN KEY (entry)
REFERENCES entry;