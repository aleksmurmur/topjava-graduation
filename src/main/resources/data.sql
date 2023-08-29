INSERT INTO USERS (id, name, email, password)
VALUES (UUID(), 'User', 'user@yandex.ru', '{noop}password'),
       (UUID(), 'Admin', 'admin@gmail.com', '{noop}admin'),
       (UUID(), 'Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
SELECT 'USER', id FROM USERS u WHERE u.name != 'Admin';

INSERT INTO USER_ROLE (role, user_id)
SELECT 'ADMIN', id FROM USERS u WHERE u.name = 'Admin';