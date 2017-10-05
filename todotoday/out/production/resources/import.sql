--This is the source of the initial data for the database because it's
--recreated everytime based on hibernate.hbm2ddl.auto property

--Insert roles
insert into role (name) values ('ROLE_USER');

--Insert 2 users (Plaintext Password for both users: password)
insert into user (username, password, enabled, role_id) values ('user','$2a$06$Dw0nsYQ90PplYniMsVXBiubEBP0ijI03tZ/ShzseMx9xEv3TUUmO6',true,1);
insert into user (username, password, enabled, role_id) values ('user2','$2a$06$Dw0nsYQ90PplYniMsVXBiubEBP0ijI03tZ/ShzseMx9xEv3TUUmO6',true,1);

-- Insert tasks
insert into task (complete,description,user_id) values (true,'Code Task entity',1);
insert into task (complete,description,user_id) values (false,'Discuss users and roles',1);
insert into task (complete,description,user_id) values (false,'Enable Spring Security',2);
insert into task (complete,description,user_id) values (false,'Test application',2);