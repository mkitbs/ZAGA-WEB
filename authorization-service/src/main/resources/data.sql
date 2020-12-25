-- permissions
insert into permission (id, name) values (1, 'CREATE');
insert into permission (id, name) values (2, 'READ');

-- roles ROLE_SUPER_ADMIN,ROLE_ADMIN, ROLE_MANAGER
insert into roles (id, name) values (1, 'ROLE_SUPER_ADMIN');
insert into roles (id, name) values (2, 'ROLE_MANAGER');
insert into roles (id, name) values (3, 'ROLE_ADMIN');
insert into roles (id, name) values (4, 'ROLE_TRACTOR_DRIVER');

-- settings
insert into setting (id, use_sap, master_data_format) values (1, 1, 'WITH_ID');
insert into setting (id, use_sap, master_data_format) values (2, 1, 'WITHOUT_ID');
insert into setting (id, use_sap, master_data_format) values (3, 0, 'WITHOUT_ID');

-- tenants
insert into tenant (id, company_code, name, setting_id) values (1, '1300', 'MKBS', 1);
insert into tenant (id, company_code, name, setting_id) values (2, '1200', 'PIKB', 2);

-- users
insert into users (id, name, surname, email, sap_user_id,enabled, password, non_locked, date_of_birth, telephone, tenant_id) 
values (1, 'Milan', 'Katic', 'katic@gmail.com', '12', 1, '$2a$10$tlHY4ACO1oM5KR5eFRNdaOcIfOR0ZQXEnCB0TjnBgHPIOWHIfZN7K', 1, '1996-10-07', '060696969', 1);
insert into users (id, name, surname, email, sap_user_id,enabled, password, non_locked, date_of_birth, telephone, tenant_id) 
values (2, 'Milos', 'Vrgovic', 'vrgovicm@gmail.com', '13', 1, '$2a$10$tlHY4ACO1oM5KR5eFRNdaOcIfOR0ZQXEnCB0TjnBgHPIOWHIfZN7K', 1, '1998-01-19', '06012345', 2);
insert into users (id, name, surname, email, sap_user_id,enabled, password, non_locked, date_of_birth, telephone, tenant_id) 
values (3, 'Pera', 'Peric', 'traktorista@gmail.com', '5', 1, '$2a$10$tlHY4ACO1oM5KR5eFRNdaOcIfOR0ZQXEnCB0TjnBgHPIOWHIfZN7K', 1, '1990-01-20', '060000000', 1);

insert into user_roles (user_id, role_id) values (1,1);
insert into user_roles (user_id, role_id) values (2,1);
insert into user_roles (user_id, role_id) values (3,4);

insert into role_permissions (role_id, permission_id) values (1,1);
insert into role_permissions (role_id, permission_id) values (2,1);
insert into role_permissions (role_id, permission_id) values (3,1);
insert into role_permissions (role_id, permission_id) values (4,1);