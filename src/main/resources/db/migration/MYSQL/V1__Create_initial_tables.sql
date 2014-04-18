
    create table access_log (
        access_log_id varchar(36) not null,
        access_date datetime,
        method varchar(10) not null,
        query varchar(128),
        remote_address varchar(128) not null,
        uri varchar(128) not null,
        user_agent varchar(128) not null,
        x_track varchar(32),
        primary key (access_log_id)
    ) ENGINE=InnoDB;

    create table category (
        category_order integer not null,
        entry_id integer not null,
        category_name varchar(128) not null,
        primary key (category_order, entry_id)
    ) ENGINE=InnoDB;

    create table entry (
        entry_id integer not null auto_increment,
        version bigint,
        created_by varchar(128),
        created_date datetime,
        last_modified_by varchar(128),
        last_modified_date datetime,
        contents longtext not null,
        format varchar(10) not null,
        published bit not null,
        title varchar(512) not null,
        primary key (entry_id)
    ) ENGINE=InnoDB;

    create table entry_history (
        entry_histry_id varchar(36) not null,
        version bigint,
        created_by varchar(128),
        created_date datetime,
        last_modified_by varchar(128),
        last_modified_date datetime,
        contents longtext not null,
        format varchar(10) not null,
        title varchar(512) not null,
        entry_id integer not null,
        primary key (entry_histry_id)
    ) ENGINE=InnoDB;

    create table link (
        url varchar(128) not null,
        version bigint,
        created_by varchar(128),
        created_date datetime,
        last_modified_by varchar(128),
        last_modified_date datetime,
        link_name varchar(128) not null,
        primary key (url)
    ) ENGINE=InnoDB;

    create table login_history (
        login_history_id varchar(36) not null,
        login_agent varchar(128) not null,
        login_date datetime not null,
        login_host varchar(128) not null,
        username varchar(128) not null,
        primary key (login_history_id)
    ) ENGINE=InnoDB;

    create table role (
        role_id integer not null auto_increment,
        version bigint,
        created_by varchar(128),
        created_date datetime,
        last_modified_by varchar(128),
        last_modified_date datetime,
        role_name varchar(25) not null,
        primary key (role_id)
    ) ENGINE=InnoDB;

    create table upload_file (
        file_id varchar(36) not null,
        version bigint,
        created_by varchar(128),
        created_date datetime,
        last_modified_by varchar(128),
        last_modified_date datetime,
        file_content longblob,
        file_name varchar(255),
        primary key (file_id)
    ) ENGINE=InnoDB;

    create table user (
        username varchar(255) not null,
        version bigint,
        created_by varchar(128),
        created_date datetime,
        last_modified_by varchar(128),
        last_modified_date datetime,
        email varchar(128) not null,
        enabled bit not null,
        first_name varchar(128) not null,
        last_name varchar(128) not null,
        locked bit not null,
        password varchar(256) not null,
        primary key (username)
    ) ENGINE=InnoDB;

    create table user_role (
        username varchar(255) not null,
        role_id integer not null,
        primary key (username, role_id)
    ) ENGINE=InnoDB;

    create index UK_yj8qxec5iw5prreilrw5uojs on access_log (method);

    create index UK_61mv3850375ocbci95horak6f on access_log (uri);

    create index UK_3npv4tqwmeri5o9rrx5tob054 on access_log (remote_address);

    create index UK_ikrhoc5rey94top5qv9lleo3k on access_log (x_track);

    create index UK_imw5hl7fsuv7p4ojvd2ngwrr6 on access_log (access_date);

    create index UK_lroeo5fvfdeg4hpicn4lw7x9b on category (category_name);

    create index UK_h89shgwewnwgby2u10ludyqnq on entry (last_modified_date);

    create index UK_jje1bkbmu5eyx0yebiprac8e on entry (created_by);

    create index UK_cpj76n28t4naq74hvp6afb3dm on entry_history (last_modified_date);

    create index UK_ctq5stwm9s6pfyiiirsvhfbph on link (last_modified_date);

    create index UK_5mvyukln9xc3gdsetytdwxihi on login_history (login_date);

    alter table role 
        add constraint UK_iubw515ff0ugtm28p8g3myt0h  unique (role_name);

    create index UK_cp5402wudt8t9t71vu303f0ge on upload_file (last_modified_date);

    create index UK_bhd2crxuienxl2fhijgb7oodh on user (last_modified_date);

    alter table category 
        add constraint FK_ihjisvvo7d8b0vu26asrl0d3a 
        foreign key (entry_id) 
        references entry (entry_id);

    alter table entry_history 
        add constraint FK_f2mu7h50hhd8dmrkyb01jpch8 
        foreign key (entry_id) 
        references entry (entry_id);

    alter table user_role 
        add constraint FK_it77eq964jhfqtu54081ebtio 
        foreign key (role_id) 
        references role (role_id);

    alter table user_role 
        add constraint FK_aphxiciwirrvuc0y7y2s2rufj 
        foreign key (username) 
        references user (username);
