INSERT INTO USERS (id, name, email, password)
VALUES (UUID(), 'User', 'user@yandex.ru', '{noop}password'),
       (UUID(), 'Admin', 'admin@gmail.com', '{noop}admin'),
       (UUID(), 'Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
SELECT 'USER', id FROM USERS u ;

INSERT INTO USER_ROLE (role, user_id)
SELECT 'ADMIN', id FROM USERS u WHERE u.name = 'Admin';

INSERT INTO RESTAURANT (id, name)
VALUES (UUID(), 'Beverly Hills');

INSERT INTO MEAL (id, name, price, restaurant_id)
SELECT UUID(), 'Hamburger', 60000, r.id FROM RESTAURANT r WHERE r.name = 'Beverly Hills';

INSERT INTO MEAL (id, name, price, restaurant_id)
SELECT UUID(), 'Coffee', 25000, r.id FROM RESTAURANT r WHERE r.name = 'Beverly Hills';

INSERT INTO DAY_MENU (id, menu_date, restaurant_id, votes_counter)
SELECT UUID(), now(), r.id, 0 FROM RESTAURANT r WHERE r.name = 'Beverly Hills';

INSERT INTO DAY_MENU_MEAL (day_menu_id, meal_id)
SELECT dm.id, m.id FROM DAY_MENU dm JOIN MEAL m on true;

INSERT INTO RESTAURANT (id, name)
VALUES (UUID(), 'JackJohns');

INSERT INTO MEAL (id, name, price, restaurant_id)
SELECT UUID(), 'French fries', 30000, r.id FROM RESTAURANT r WHERE r.name = 'JackJohns';

INSERT INTO MEAL (id, name, price, restaurant_id)
SELECT UUID(), 'Tomato soup', 27000, r.id FROM RESTAURANT r WHERE r.name = 'JackJohns';

INSERT INTO MEAL (id, name, price, restaurant_id)
SELECT UUID(), 'Beefsteak', 85000, r.id FROM RESTAURANT r WHERE r.name = 'JackJohns';

INSERT INTO DAY_MENU (id, menu_date, restaurant_id, votes_counter)
SELECT UUID(), now(), r.id, 0 FROM RESTAURANT r WHERE r.name = 'JackJohns';

INSERT INTO DAY_MENU_MEAL (day_menu_id, meal_id)
SELECT dm.id, m.id FROM DAY_MENU dm
    JOIN RESTAURANT r ON dm.restaurant_id = r.id
    JOIN MEAL m on r.id = m.restaurant_id WHERE r.name = 'JackJohns';