INSERT INTO role (role_name, version, created_date, created_by, last_modified_date, last_modified_by)
VALUES ('ADMIN', 0, now(), 'admin', now(), 'admin');
INSERT INTO role (role_name, version, created_date, created_by, last_modified_date, last_modified_by)
VALUES ('EDITOR', 0, now(), 'admin', now(), 'admin');
INSERT INTO user (username, email, enabled, first_name, last_name, locked, password, version, created_date, created_by, last_modified_date, last_modified_by)
VALUES ('admin', 'admin@example.com', TRUE, 'Admin', 'Admin', FALSE,
        '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 0, now(), 'admin', now(), 'admin');
INSERT INTO user_role (username, role_id) SELECT
                                            'admin' AS username,
                                            role_id
                                          FROM role;
INSERT INTO link (url, link_name, version, created_date, created_by, last_modified_date, last_modified_by)
VALUES ('http://google.com', 'Google', 0, now(), 'admin', now(), 'admin');
INSERT INTO link (url, link_name, version, created_date, created_by, last_modified_date, last_modified_by)
VALUES ('http://twitter.com', 'Twitter', 0, now(), 'admin', now(), 'admin');

INSERT INTO entry (version, created_by, created_date, last_modified_by, last_modified_date, contents, format, published, title)
VALUES (0, 'admin', now(), 'admin', now(), '* aa\n* bb\n* cc', 'md', 1, 'This is a sample contents');
INSERT INTO category (category_name, category_order, entry_id) VALUES ('Contents', 1, (SELECT entry_id FROM entry WHERE title = 'This is a sample contents'));
INSERT INTO category (category_name, category_order, entry_id) VALUES ('Sample', 2, (SELECT entry_id FROM entry WHERE title = 'This is a sample contents'));

INSERT INTO entry (version, created_by, created_date, last_modified_by, last_modified_date, contents, format, published, title)
VALUES (0, 'admin', now(), 'admin', now(), 'Code sample\n\n    console.log("hello world!");', 'md', 0, 'Here we go!');
INSERT INTO category (category_name, category_order, entry_id) VALUES ('Contents', 1, (SELECT entry_id FROM entry WHERE title = 'Here we go!'));
INSERT INTO category (category_name, category_order, entry_id) VALUES ('Sample', 2, (SELECT entry_id FROM entry WHERE title = 'Here we go!'));