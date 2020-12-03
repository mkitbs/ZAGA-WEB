-- permissions
insert into permission (id, name) values (1, 'CREATE');
insert into permission (id, name) values (2, 'READ');

-- roles ROLE_SUPER_ADMIN,ROLE_MANAGER
insert into roles (id, name) values (1, 'ROLE_SUPER_ADMIN');
insert into roles (id, name) values (2, 'ROLE_MANAGER');

-- users
insert into users (id, name, surname, email, sap_user_id,enabled, password, non_locked, date_of_birth, telephone) 
values (1, 'Milan', 'Katic', 'katic@gmail.com', '12', 1, '$2a$10$tlHY4ACO1oM5KR5eFRNdaOcIfOR0ZQXEnCB0TjnBgHPIOWHIfZN7K', 1, '1996-10-07', '060696969');

insert into user_roles (user_id, role_id) values (1,1);

insert into role_permissions (role_id, permission_id) values (1,1);