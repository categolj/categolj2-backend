insert into role(role_name, version, created_date, created_by, last_modified_date, last_modified_by) values('ADMIN', 0, now(), 'admin', now(), 'admin');
insert into role(role_name, version, created_date, created_by, last_modified_date, last_modified_by) values('EDITOR', 0, now(), 'admin', now(), 'admin');
insert into user (username, email, enabled, first_name, last_name, locked, password, version, created_date, created_by, last_modified_date, last_modified_by) values('admin', 'admin@example.com', true, 'Admin', 'Admin', false, '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 0, now(), 'admin', now(), 'admin');
insert into user_role(username, role_id) select 'admin' as username, role_id from role;
insert into link(url, link_name, version, created_date, created_by, last_modified_date, last_modified_by) values('Google', 'http://google.com', 0, now(), 'admin', now(), 'admin');
insert into link(url, link_name, version, created_date, created_by, last_modified_date, last_modified_by) values('Twitter', 'http://twitter.com', 0, now(), 'admin', now(), 'admin');
COMMIT;