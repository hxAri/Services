# Web Service Rest API

This is a Web Service Rest API built using Spring Boot `v3.1.3`<br/>
It also includes Bearer Token based Authentication using Json Web Token,<br/>
Apart from that, it also uses pageable for taking large numbers of tasks,<br/>
and also Swagger Open API for its API documentation.

# Dependency
* io.jsonwebtoken:jjwt-api:**0.11.5**
* io.jsonwebtoken:jjwt-impl:**0.11.5**
* io.jsonwebtoken:jjwt-jackson:**0.11.5**
* org.springdoc:springdoc-openapi-starter-webmvc-ui:**2.2.0**
* org.springframework.boot:spring-boot-starter-data-jpa:**3.1.3**
* org.springframework.boot:spring-boot-starter-security:**3.1.3**
* org.springframework.boot:spring-boot-starter-validation:**3.1.3**
* org.springframework.boot:spring-boot-starter-web:**3.1.3**
* com.mysql:mysql-connector-j:**8.1.0**

# Request URI
The following is a list of available Request URIs:
* **GET** API get current authority role.
  * http://127.0.0.1:8004/api/v1/auth
* **POST** API for signin.
  * http://127.0.0.1:8004/api/v1/auth/signin
* **POST** API for user signup.
  * http://127.0.0.1:8004/api/v1/auth/signup
* **POST** API for get tasks items.
  * http://127.0.0.1:8004/api/v1/task
* **GET** API for get task by task id.
  * http://127.0.0.1:8004/api/v1/task/{id}
* **PUT** API for update task by task id.
  * http://127.0.0.1:8004/api/v1/task/{id}
* **DELETE** API for delete task by task id.
  * http://127.0.0.1:8004/api/v1/task/{id}
* **POST** API for add new task.
  * http://127.0.0.1:8004/api/v1/task/insert
* **GET** API for get current authenticated user info.
  * http://127.0.0.1:8004/api/v1/user
* **PUT** API for update user info.
  * http://127.0.0.1:8004/api/v1/user/
* **DELETE** API for delete user **ONLY ADMIN ROLE AUTHORITY**
  * http://127.0.0.1:8004/api/v1/user/{id}

## Licence
All source code is licensed under the GNU General Public License v3. Please [see](https://www.gnu.org/licenses) the original document for more details.
