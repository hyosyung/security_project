# Product REST API + Auth

## 개요

- 회원 인증(Authentication) 및 인가(Authorization) 기반의 상품 관리 Spring Boot REST API Application 구현

## 목표

- Spring Security를 활용한 Authentication 및 Authorization의 구현
- Token Based Authentication의 이해

## 준비 사항

- Spring Boot >= 2.6
- DBMS (택 1 / Docker 또는 Local 설치)

    - MySQL Server >= 8.0 (Docker)
        - Docker

            ```bash
            $ docker run --name project-01 \
              -e MYSQL_ROOT_PASSWORD=root \
              -e MYSQL_USER=coupang \
              -e MYSQL_PASSWORD=coupang \
              -e MYSQL_DATABASE=project-01 \
              -p 3306:3306 -d mysql:8.0
            ```

    - PostgreSQL >= 13
        - Docker

            ```bash
            $ docker run --name project-01 \
              -e POSTGRES_USER=coupang \
              -e POSTGRES_PASSWORD=coupang \
              -e POSTGRES_DB=project-01 \
              -p 5432:5432 -d postgres:13
            ```

## 요구 사항

### 0. 주의 사항

- 'Request 예시' 및 'Response 예시'는 참고 용도로만 제공되는 것이며, 반드시 **똑같이 구현할 필요 없음**
- 아래의 명세에 명시되지 않은 내용은 **자유롭게** 구현

### 1. Database Scheme

- 상품(Product)은 상품명(name)과 가격(price), 어떤 사용자의 상품인지(accountId)로 구성
- 사용자(Account)는 계정명(username)과 암호(password)로 구성
    - Spring Security의 UserDetails 인터페이스를 상속받아 구현
- Entity Relationship Diagram (ERD)

    ![ERD](README/erd.jpg)

### 2. Endpoints

#### 2.1. Account

- `POST /accounts/singup`
    - 계정명(username)과 비밀번호(password)를 입력받아 회원 생성(회원가입)
        - 기존에 존재하는 username으로 가입 불가능
    - Request Body
        - (필수) `username` - 계정명
            - 영어 대소문자, 숫자, `_`(underscore), `-`(dash)만 허용
            - 길이는 최소 3자, 최대 20자
        - (필수) `password` - 비밀번호
            - 길이는 최소 8자
            - 영어 대문자 및 소문자, 숫자, 특수문자의 조합
    - Request 예시

        ```json
        {
            "username": "john",
            "password": "secRet20*21"
        }
        ```

    - Response 예시 - 200 OK

        ```json
        {
            "id": 1,
            "username": "john"
        }
        ```

- `POST /accounts/login`
    - 계정명(username)과 비밀번호(password)로 로그인하여 token을 발급
    - Request 예시
        - `POST /accounts/login`

            ```json
            {
                "username": "john",
                "password": "secRet20*21"
            }
            ```
    - Response 예시

        ```json
        {
            "id": 1,
            "username": "john",
            "token": "eyJhbGciOiJIUzUxMiJ9..."
        }
        ```

- `GET /me`
    - 토큰(token)으로부터 인증 정보(회원 정보)를 조회
    - Request Header

        ```
        Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
        ```

    - Response 예시

        ```json
        {
            "id": 1,
            "username": "john",
            "token": "eyJhbGciOiJIUzUxMiJ9..."
        }
        ```


#### 2.2. Product

- `GET /products`
    - 모든 상품 조회
    - 인증 유무와 관계없이 조회 가능
    - Response 예시

        ```json
        [
            {
                "id": 1,
                "name": "물",
                "price": 1000,
                "accountId": 1
            },
            {
                "id": 2,
                "name": "콜라",
                "price": 5000,
                "accountId": 1
            }
        ]
        ```

- `POST /products`
    - 새 상품 생성
    - 상품의 생성은 **인증된 사용자**만 가능
    - Request Header

        ```
        Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
        ```
    
    - Request Body
        - (필수) `name` - 상품명
        - (필수) `price` - 가격
    - Request 예시
        - `POST /products`

            ```json
            {
                "name": "물",
                "price": 1000
            }
            ```

    - Response 예시

        ```json
        {
            "id": 1,
            "name": "물",
            "price": 1000,
            "accountId": 1
        }
        ```

- `GET /products/{id}`
    - 특정 상품 정보 조회
    - 인증 유무와 관계없이 조회 가능
    - Request 예시
        - `GET /products/1`
    - Response 예시

        ```json
        {
            "id": 1,
            "name": "물",
            "price": 1000
        }
        ```

- `PUT /products/{id}`
    - 특정 상품 정보 수정(업데이트)
    - 특정 사용자가 생성한 **상품**은 해당 사용자만이 **수정** 가능하도록 구현
    - Request Header

        ```
        Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
        ```
    
    - Request Body
        - (필수) `name` - 상품명
        - (필수) `price` - 가격
    - Request 예시
        - `PUT /products/1`

            ```json
            {
              "name": "물",
              "price": 2000
            }
            ```

    - Response 예시

        ```json
        {
            "id": 1,
            "name": "물",
            "price": 2000,
            "accountId": 1
        }
        ```

- `DELETE /products/{id}`
    - 특정 상품 삭제
    - 특정 사용자가 생성한 **상품**은 해당 사용자만이 **삭제** 가능하도록 구현
    - Request Header

        ```
        Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
        ```
    
    - Request 예시
        - `DELETE /products/1`
    - Response 예시
        - 204 No Content


### 3. Account Roles (선택)

- 권한은 ADMIN, USER만 존재
- 'ADMIN' Role을 가진 사용자의 경우, Product의 등록자와 관계 없이 모든 Product의 생성 및 수정, 삭제가 가능하도록 구현


## 제출 방법

- Git을 통해 프로젝트 URL의 GitHub repository를 로컬로 clone

    ```bash
    $ git clone https://github.com/coupang-edu/a4e-spring-security-project-01
    $ cd a4e-spring-security-project-01
    ```

- 본인의 영문명으로 branch 생성 및 이동

    ```bash
    $ git switch -c john
    ```

- 코드 작성이 완료된 프로젝트를, 아래와 같이 프로젝트 폴더명을 본인의 영문명으로 변경

    ```bash
    README.md
    john/
        ├── gradle/
        ├── src/
        ├── build.gradle
        ├── gradlew
        ├── gradlew.bat
        └── settings.gradle
    ```

- 코드 작성 완료 후, 해당 branch를 push

    ```bash
    $ git push origin john
    ```

- Pull Request 생성하여 제출 완료
