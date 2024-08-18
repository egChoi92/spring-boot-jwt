-- Insert roles into AUTHORITY table
INSERT INTO authority (authority_name) VALUES ('ROLE_USER');
INSERT INTO authority (authority_name) VALUES ('ROLE_ADMIN');

-- Insert user into `user` table
INSERT INTO member (user_id, username, password, nickname, activated) VALUES (1, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', true);
INSERT into member (user_id, username, password, nickname, activated) VALUES (2, 'user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user', 1);

-- Insert user roles into USER_AUTHORITY table
INSERT INTO member_authority (user_id, authority_name) VALUES (1, 'ROLE_USER');
INSERT INTO member_authority (user_id, authority_name) VALUES (1, 'ROLE_ADMIN');