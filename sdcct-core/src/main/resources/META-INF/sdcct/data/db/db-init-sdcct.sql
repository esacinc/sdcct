/*====================================================================================================
= SEQUENCES
=====================================================================================================*/
create sequence search_param_entity_id as bigint minvalue 1;

create sequence resource_entity_id as bigint minvalue 1;

/*====================================================================================================
= TABLES
=====================================================================================================*/
create table search_param_coord (
    entity_id bigint primary key,
    resource_entity_id bigint not null,
    name varchar(64) not null,
    latitude double not null,
    longitude double not null
);

create table search_param_date (
    entity_id bigint primary key,
    resource_entity_id bigint not null,
    name varchar(64) not null,
    value timestamp not null
);

create table search_param_num (
    entity_id bigint primary key,
    resource_entity_id bigint not null,
    name varchar(64) not null,
    value blob(1048576) not null
);

create table search_param_quantity (
    entity_id bigint primary key,
    resource_entity_id bigint not null,
    name varchar(64) not null,
    code_system varchar(64),
    units varchar(1024),
    value blob(1048576) not null
);

create table search_param_str (
    entity_id bigint primary key,
    resource_entity_id bigint not null,
    name varchar(64) not null,
    value clob(1048576) not null
);

create table search_param_token (
    entity_id bigint primary key,
    resource_entity_id bigint not null,
    name varchar(64) not null,
    code_system varchar(64),
    value varchar(64) not null
);

create table search_param_uri (
    entity_id bigint primary key,
    resource_entity_id bigint not null,
    name varchar(64) not null,
    value varchar(1024) not null
);

create table form_fhir (
    entity_id bigint primary key,
    id varchar(64) not null,
    content clob(10485760) not null,
    text clob(1048576)
);

create table form_rfd (
    entity_id bigint primary key,
    id varchar(64) not null,
    content clob(10485760) not null
);
