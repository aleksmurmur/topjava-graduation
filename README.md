# [Graduation TopJava project (based on BootJava)](https://github.com/JavaWebinar/topjava/blob/doc/doc/graduation.md)
-------------------------------------------------------------
- Stack: [JDK 17](http://jdk.java.net/17/), Spring Boot 3.x, Lombok, H2, Caffeine Cache, SpringDoc OpenApi 2.x
- Run: `mvn spring-boot:run` in root directory.
-----------------------------------------------------
Voting system for deciding where to have lunch
-------------
[REST API documentation](http://localhost:8080/)
Credentials:
```
User:  user@yandex.ru / password
Admin: admin@gmail.com / admin
Guest: guest@gmail.com / guest
```
Database is populated with 3 users, 2 restaurants, and 2 day menus with different meals.

Typical scenarios:

- Admin: 
```
1) Create restaurant
2) Create meals connected to this restaurant
3) Create day menus choosing from these meals
```

- User:
```
1) Register/log in
2) Get menu list for today
3) Vote for chosen menu (1 menu for today = 1 restaurant)
4) Possible: Revote if time is less than 11 A.M.
```
