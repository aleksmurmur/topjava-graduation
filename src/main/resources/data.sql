INSERT INTO USERS (id, name, email, password)
VALUES (UUID(), 'User', 'user@yandex.ru', '{noop}password'),
       (UUID(), 'Admin', 'admin@gmail.com', '{noop}admin'),
       (UUID(), 'Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
SELECT 'USER', id FROM USERS u WHERE u.name != 'Admin';

INSERT INTO USER_ROLE (role, user_id)
SELECT 'ADMIN', id FROM USERS u WHERE u.name = 'Admin';

INSERT INTO RESTAURANTS (id, name)
VALUES (UUID(), 'Beverly Hills');

INSERT INTO MEALS (id, name, price, restaurant_id)
SELECT UUID(), 'Hamburger', 600, r.id FROM RESTAURANTS r WHERE r.name = 'Beverly Hills';

INSERT INTO MEALS (id, name, price, restaurant_id)
SELECT UUID(), 'Coffee', 250, r.id FROM RESTAURANTS r WHERE r.name = 'Beverly Hills';

INSERT INTO DAY_MENUS (id, menu_date, restaurant_id, votes_counter)
SELECT UUID(), now(), r.id, 0 FROM RESTAURANTS r WHERE r.name = 'Beverly Hills';

INSERT INTO RESTAURANTS (id, name)
VALUES (UUID(), 'HellHole');

INSERT INTO DAY_MENUS (id, menu_date, restaurant_id, votes_counter)
SELECT UUID(), now(), r.id, 0 FROM RESTAURANTS r WHERE r.name = 'HellHole';

INSERT INTO DAY_MENUS_MEALS (day_menu_id, meal_id)
SELECT dm.id, m.id FROM DAY_MENUS dm JOIN MEALS m on true;