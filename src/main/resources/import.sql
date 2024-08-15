-- Insert roles into AUTHORITY table
INSERT INTO authority (authority_name) VALUES ('role_user');
INSERT INTO authority (authority_name) VALUES ('role_admin');

-- Insert user into `user` table
INSERT INTO member (member_id, name, password, nickname, activated) VALUES (1, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', true);

-- Insert user roles into USER_AUTHORITY table
INSERT INTO member_authority (member_id, authority_name) VALUES (1, 'role_user');
INSERT INTO member_authority (member_id, authority_name) VALUES (1, 'role_admin');