# Web Service Rest API

Web Services Rest API with Sping Boot v3.1.3 which is equipped with JWT (JSON Web Token)<br/>
Authentication feature, uses Elasticsearch as Storage Database and also Swagger UI for<br/>
Endpoint API documentation, MySQL version is available [here](https://github.com/hxAri/Services/tree/v1.0.2)

# Dependency
* co.elastic.clients:elasticsearch-java:**7.17.8**
* com.fasterxml.jackson.core:jackson-databind:**2.15.2**
* com.fasterxml.jackson.datatype:jackson-datatype-jsr310:**2.15.2**
* io.jsonwebtoken:jjwt-api:**0.11.5**
* io.jsonwebtoken:jjwt-impl:**0.11.5**
* io.jsonwebtoken:jjwt-jackson:**0.11.5**
* org.springdoc:springdoc-openapi-starter-webmvc-ui:**2.2.0**
* org.springframework.boot:spring-boot-starter-security:**3.1.3**
* org.springframework.boot:spring-boot-starter-web:**3.1.3**

# Request URI
The following is a list of available Request URIs:
* **GET** API get current authority role.
  * http://127.0.0.1:8004/api/v1/auth
* **POST** API for signin.
  * http://127.0.0.1:8004/api/v1/auth/signin
* **POST** API for user signup.
  * http://127.0.0.1:8004/api/v1/auth/signup
* **GET** API for get all product (Only by Authenticated User ID).
  * http://127.0.0.1:8004/api/v1/product
* **POST** API for add product.
  * http://127.0.0.1:8004/api/v1/product
* **PUT** API for update product.
  * http://127.0.0.1:8004/api/v1/product/{id}
* **DELETE** API for delete product.
  * http://127.0.0.1:8004/api/v1/product/{id}
* **GET** API for get current authenticated user info.
  * http://127.0.0.1:8004/api/v1/user
* **PUT** API for update user info.
  * http://127.0.0.1:8004/api/v1/user/
* **DELETE** API for delete user **ONLY ROOT ROLE AUTHORITY**
  * http://127.0.0.1:8004/api/v1/user/{id}

## Licence
All source code is licensed under the GNU General Public License v3. Please [see](https://www.gnu.org/licenses) the original document for more details.
