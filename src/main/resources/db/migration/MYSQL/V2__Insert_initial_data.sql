insert into role(role_name, version, created_date, created_by, last_modified_date, last_modified_by) values('ADMIN', 0, now(), 'admin', now(), 'admin');
insert into role(role_name, version, created_date, created_by, last_modified_date, last_modified_by) values('EDITOR', 0, now(), 'admin', now(), 'admin');
insert into user (username, email, enabled, first_name, last_name, locked, password, version, created_date, created_by, last_modified_date, last_modified_by) values('admin', 'admin@example.com', true, 'Admin', 'Admin', false, '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 0, now(), 'admin', now(), 'admin');
insert into user_role(username, role_id) select 'admin' as username, role_id from role;
insert into link(url, link_name, version, created_date, created_by, last_modified_date, last_modified_by) values('http://google.com', 'Google', 0, now(), 'admin', now(), 'admin');
insert into link(url, link_name, version, created_date, created_by, last_modified_date, last_modified_by) values('http://twitter.com', 'Twitter', 0, now(), 'admin', now(), 'admin');

insert into entry (version, created_by, created_date, last_modified_by, last_modified_date, contents, format, published, title) values (0, 'admin', now(), 'admin', now(), '* aa\n* bb\n* cc', 'md', 1, 'This is a sample contents');
insert into category (category_name, category_order, entry_id) values ('Contents', 1, 1);
insert into category (category_name, category_order, entry_id) values ('Sample', 2, 1);

insert into entry (version, created_by, created_date, last_modified_by, last_modified_date, contents, format, published, title) values (0, 'admin', now(), 'admin', now(), 'Code sample\n\n    console.log("hello world!");', 'md', 0, 'Here we go!');
insert into category (category_name, category_order, entry_id) values ('Contents', 1, 2);
insert into category (category_name, category_order, entry_id) values ('Sample', 2, 2);