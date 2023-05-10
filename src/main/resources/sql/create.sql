create table accesses (
    id bigserial not null,
    name varchar(255),
    primary key (id));

create table dentistry (
    id bigserial not null,
    address varchar(255),
    primary key (id));

create table employee_schedule (
    employee_id bigint not null,
    schedule_id bigint not null);

create table employee_specialization (
    employee_id bigint not null,
    specialization_id bigint not null);

create table employees (
    id bigserial not null,
    job_title varchar(255),
    login varchar(255),
    name varchar(255),
    number varchar(255),
    password varchar(255),
    access_id bigint,
    dentistry_id bigint,
    primary key (id));

create table patients (
    id bigserial not null,
    dob date,
    gender varchar(255),
    mail varchar(255),
    snp varchar(255),
    number varchar(255),
    passport varchar(255),
    primary key (id));

create table procedure (
    id bigserial not null,
    cost float(53) not null check ( cost > 0 ),
    name varchar(255),
    primary key (id));

create table reception_procedure (
    reception_id bigint not null,
    procedure_id bigint not null);

create table receptions (
    id bigserial not null,
    complaints varchar(255),
    diagnostic varchar(255),
    treatment varchar(255),
    registration_id bigint,
    primary key (id));

create table registrations (
    id bigserial not null,
    date date, status varchar(255),
    time time,
    employee_id bigint,
    patient_id bigint,
    primary key (id));

create table schedule (
    id bigserial not null,
    date date,
    time time,
    primary key (id));

create table specialization (
    id bigserial not null,
    name varchar(255),
    primary key (id));


create table patients (
                          id bigserial not null,
                          dob date,
                          gender varchar(255),
                          mail varchar(255),
                          number varchar(255),
                          passport varchar(255),
                          snp varchar(255),
                          primary key (id));


alter table if exists employee_schedule add constraint FKpxt6pq9ljtsvppjmhcnlhu1ko foreign key (schedule_id) references schedule;
alter table if exists employee_schedule add constraint FK8mg193kvii4bh06cocaa0pbi2 foreign key (employee_id) references employees;
alter table if exists employee_specialization add constraint FKsrygpm6s436c4x4rg1f73plb2 foreign key (specialization_id) references specialization;
alter table if exists employee_specialization add constraint FKixkb6qxlckjvh3ulo5s2emm1t foreign key (employee_id) references employees;
alter table if exists employees add constraint FKk9f49u7uhlquqwpo34a6p9qbv foreign key (access_id) references accesses;
alter table if exists employees add constraint FK2ryqbqofymobnx8k22xqycspk foreign key (dentistry_id) references dentistry;
alter table if exists reception_procedure add constraint FK5iomkuy9f7ncarkrjtncrlg69 foreign key (procedure_id) references procedure;
alter table if exists reception_procedure add constraint FKky6b0xramyxhgcmi840k2ggoe foreign key (reception_id) references receptions;
alter table if exists receptions add constraint FK11uu2kyeygmvfh1oi6dw3coy1 foreign key (registration_id) references registrations;
alter table if exists registrations add constraint FKg0yengveo8rct591duvay4k49 foreign key (employee_id) references employees;
alter table if exists registrations add constraint FKiwnxdm14gvo22vlllirek9hya foreign key (patient_id) references patients;