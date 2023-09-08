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

1) Create restaurant
```
curl -X 'POST' \
  'http://localhost:8080/admin/api/v1/restaurants' \
  -H 'accept: application/json' \
  -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Test Restaurant"
}'
```
2) Create meals connected to this restaurant
```
curl -X 'POST' \
  'http://localhost:8080/admin/api/v1/meals' \
  -H 'accept: application/json' \
  -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Meal 1",
  "price": 9000,
  "restaurantId": "{restaurantId}}"
}'
```
4) Create day menus choosing from these meals
```
curl -X 'POST' \
  'http://localhost:8080/admin/api/v1/day-menu' \
  -H 'accept: application/json' \
  -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
  -H 'Content-Type: application/json' \
  -d '{
  "date": "2023-09-08",
  "mealIds": [
    "{mealId}"
  ],
  "restaurantId": "{restaurantId}"
}'
```


- User:

1) Register/log in
```
curl -X 'POST' \
  'http://localhost:8080/user/api/v1/profile' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Test User",
  "email": "test@yandex.ru",
  "password": "qwerty"
}'
```
2) Get menu list for today
```
curl -X 'GET' \
  'http://localhost:8080/user/api/v1/day-menu' \
  -H 'accept: application/json' \
  -H 'Authorization: Basic dGVzdEB5YW5kZXgucnU6cXdlcnR5'
```
3) Vote for chosen menu using id (1 menu for today = 1 restaurant)
4) Possible: Revote if time is less than 11 A.M.
```
curl -X 'PUT' \
  'http://localhost:8080/user/api/v1/day-menu/{dayMenuId}' \
  -H 'accept: application/json' \
  -H 'Authorization: Basic dGVzdEB5YW5kZXgucnU6cXdlcnR5'
```

