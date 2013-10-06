drop table if exists category;
drop table if exists entry;
drop table if exists link;
drop table if exists user_role;
drop table if exists user;
drop table if exists role;

create table category (category_order integer not null, entry_id integer not null, category_name varchar(128) not null, primary key (category_order, entry_id));
create table entry (entry_id integer not null auto_increment, contents longtext not null, published boolean not null, title longtext not null, version bigint, created_date datetime, last_modified_date datetime, created_by varchar(128), last_modified_by varchar(128), primary key (entry_id));
create table link (url varchar(128) not null, link_name varchar(128) not null, version bigint, created_date datetime, last_modified_date datetime, created_by varchar(128), last_modified_by varchar(128), primary key (url));
create table user (username varchar(128) not null, email varchar(128) not null, enabled boolean not null, first_name varchar(128) not null, last_name varchar(128) not null, locked boolean not null, password longtext not null, version bigint, created_date datetime, last_modified_date datetime, created_by varchar(128), last_modified_by varchar(128), primary key (username));
create table role (role_id integer not null auto_increment, role_name varchar(25) not null, version bigint, created_date datetime, last_modified_date datetime, created_by varchar(128), last_modified_by varchar(128), primary key (role_id));
create table user_role (username varchar(128) not null, role_id integer not null, primary key (username, role_id));

alter table category add index FK_category_entry_id (entry_id), add constraint FK_category_entry_id foreign key (entry_id) references entry (entry_id);
alter table entry add index FK_entry_created_by (created_by), add constraint FK_entry_created_by foreign key (created_by) references user (username);
alter table entry add index FK_entry_last_modified_by4 (last_modified_by), add constraint FK_entry_last_modified_by4 foreign key (last_modified_by) references user (username);
alter table link add index FK_link_created_by (created_by), add constraint FK_link_created_by foreign key (created_by) references user (username);
alter table link add index FK_link_last_modified_by (last_modified_by), add constraint FK_link_last_modified_by foreign key (last_modified_by) references user (username);
alter table user add index FK_user_created_by (created_by), add constraint FK_user_created_by foreign key (created_by) references user (username);
alter table user add index FK_user_last_modified_by (last_modified_by), add constraint FK_user_last_modified_by foreign key (last_modified_by) references user (username);
alter table user_role add index FK_user_role_role_id (role_id), add constraint FK_user_role_role_id foreign key (role_id) references role (role_id);
alter table user_role add index FK_user_role_username (username), add constraint FK_user_role_username foreign key (username) references user (username);

insert into user (username, email, enabled, first_name, last_name, locked, password, version, created_date, created_by, last_modified_date, last_modified_by) values('admin', 'admin@example.com', true, 'Admin', 'Admin', false, '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 0, now(), 'admin', now(), 'admin');
insert into role(role_name, version, created_date, created_by, last_modified_date, last_modified_by) values('ADMIN', 0, now(), 'admin', now(), 'admin');
insert into role(role_name, version, created_date, created_by, last_modified_date, last_modified_by) values('EDITOR', 0, now(), 'admin', now(), 'admin');
insert into user_role(username, role_id) values('admin', 1);
insert into user_role(username, role_id) values('admin', 2);


insert into link(url, link_name, version, created_date, created_by, last_modified_date, last_modified_by) values('Google', 'http://google.com', 0, now(), 'admin', now(), 'admin');
insert into link(url, link_name, version, created_date, created_by, last_modified_date, last_modified_by) values('Twitter', 'http://twitter.com', 0, now(), 'admin', now(), 'admin');

