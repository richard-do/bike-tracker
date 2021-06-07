insert into SEC_User (email, encryptedPassword, ENABLED)
values ('frank@frank.com', '$2a$10$1ltibqiyyBJMJQ4hqM7f0OusP6np/IHshkYc4TjedwHnwwNChQZCy', 1);

insert into SEC_User (email, encryptedPassword, ENABLED)
values ('pooja.kakkar@sheridancollege.ca', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);

 
insert into sec_role (roleName)
values ('ROLE_ADMIN');
 
insert into sec_role (roleName)
values ('ROLE_USER');
 
insert into manufacturer (manufacturer) values
('Mountain Bikes'),
('eXtreme'),
('Power Bikes'),
('Insanity');

 
insert into user_role (userId, roleId)
values (1, 1);
 
insert into user_role (userId, roleId)
values (1, 2);
 
insert into user_role (userId, roleId)
values (2, 2);

