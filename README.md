# SPRING PLUS


## 📖 **주특기 플러스 주차 과제 회고**

처음 과제를 fork했는데 yml 파일이 따로 없어서 yml 파일부터 생성을 하며 과제를 시작하였다.

### \[Level 1\]

#### 1\. 코드 개선 퀴즈 \@transactional의 이해

기본적으로 서비스단은 \@transactional(readOnly = true)로 설정한다. 그리고 트랜잭션 내에서 DB의 데이터를 추가, 변경, 삭제하는 경우에만 해당 메서드에 \@transactional 설정을 해주어야 한다. 그런데 saveTodo()에는 해당 설정이 되어 있지 않아 에러가 발생하게된 것이다. \@transactional 설정을 해 주면 해당 에러가 더 이상 발생하지 않는 것을 확인할 수 있다.

---

#### 2.코드 추가 퀴즈 - JWT의 이해

\[요구 사항\]

-   User의 정보에 nickname이 필요해졌어요.
-   User 테이블에 nickname 컬럼을 추가해주세요.
-   nickname은 중복 가능합니다.
-   프론트엔드 개발자가 JWT에서 유저의 닉네임을 꺼내 화면에 보여주길 원하고 있어요.

\[변경 사항\]

-   SignupRequest.java
    -   String nickname 필드 추가(\@notblank 설정 적용)
    -   생성자 매개변수 및 필드 초기화 코드에 nickname 추가
-   User 엔티티
    -   String nickname 필드 추가
    -   생성자 매개변수 및 초기화 코드에 nickname 추가
    -   fromAuthUser() 메서드에서 User 객체의 생성자를 호출하여 반환하는 부분에서 매개변수 nickname 추가
-   AuthService.java
    -   signup() 메서드 수정
        -   newUser의 생성자에 nickname 매개변수 추가
        -   jwtUtil.createToken() 매개변수에 nickname 추가
    -   signin() 메서드 수정
        -   jwtUtil.createToken() 매개변수에 nickname 추가
-   JwtUtil.java
    -   createToken() 메서드 수정
        -   JWT토큰 빌더에 nickname claim 추가
-   JwtFilter.java
    -   httpRequest에 nickname 속성 추가
-   AuthUser.java
    -   String nickname 필드 추가
    -   생성자 매개변수에 nickname 추가
    -   생성자에서 nickname 초기화 코드 추가
-   AuthUserArgumentResolver.java
    -   JwtFilter에서 set한 nickname 값 가져오는 코드 추가
    -   AuthUser 객체의 생성자 호출하여 반환하는 코드에 매개변수 nickname 추가
-   UserResponse.java
    -   String nickname 필드 추가
    -   생성자 매개변수에 nickname 추가
    -   생성자에서 nickname 필드 초기화 하도록 코드 추가
-   UserResponse객체 생성자 호출하는 부분에서 nickname 매개변수 추가
    -   CommentService.java
    -   ManagerService.java
    -   TodoService.java
    -   UserService.java

---

#### 3\. 코드 개선 퀴즈 - AOP의 이해

\[요구 사항\]

-   `UserAdminController` 클래스의 `changeUserRole()` 메소드가 실행 전 동작해야해요.
-   `AdminAccessLoggingAspect` 클래스에 있는 AOP가 개발 의도에 맞도록 코드를 수정해주세요.

\[변경 사항\]

-   AOP 실행 시점 변경
    -   @after -> @before
-   AOP Target 변경
    -   UserController.getUser() -> UserAdminController.changeUserRole()

---

#### 4.테스트 코드 퀴즈 - 컨트롤러 테스트의 이해

\[요구 사항\]

-   테스트 패키지 `org.example.expert.domain.todo.controller`의  
    `todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다()` 테스트가 실패하고 있어요.
-   테스트가 정상적으로 수행되어 통과할 수 있도록 코드를 수정해주세요.

\[변경 사항\]  
MockHttpServletResponse  
Status = 400  
Body = {"code":400,"message":"Todo not found","status":"BAD\_REQUEST"}  
따라서 then 부분에서 결과를 예측하는 부분(.andExpect()) 코드 수정

-   status().isOk() -> status().isBadRequest()
-   jsonPath("$.status").value(HttpStatus.OK.name()) -> jsonPath("$.status").value(HttpStatus.BAD\_REQUEST.name())
-   jsonPath("$.code").value(HttpStatus.OK.value()) -> HttpStatus.BAD\_REQUEST.value())

---

#### 5.코드 개선 퀴즈 - JPA의 이해

\[요구 사항\]

-   할 일 검색 시 `weather` 조건으로도 검색할 수 있어야해요.
    -   `weather` 조건은 있을 수도 있고, 없을 수도 있어요!
-   할 일 검색 시 수정일 기준으로 기간 검색이 가능해야해요.
    -   기간의 시작과 끝 조건은 있을 수도 있고, 없을 수도 있어요!
-   JPQL을 사용하고, 쿼리 메소드명은 자유롭게 지정하되 너무 길지 않게 해주세요.  
    (필요할 시, 서비스 단에서 if문을 사용해 여러 개의 쿼리 메소드를 사용하셔도 좋습니다.)

\[변경 사항\]

-   TodoController.java
    -   getTodos() 메서드에 매개변수 추가
        -   @RequestParam으로 조건을 받음
            -   String weather : 날씨 조건
            -   LocalDateTime startDate : 기간 시작 조건
            -   LocalDateTime endDate : 기간 끝 조건  
                (weather, 가간 시작과 끝 조건 모두 있을 수도 있고 없을 수도 있다고 했으므로)
        -   weather에는 required = false 옵션 적용
        -   기간 시작 조건에는 MySQL DateTime 최솟값을 defaultValue로 설정
            -   defaultValue = "1000-01-01T00:00:00"
        -   기간 끝 조건에는 MySQL DateTime 최댓값을 defaultValue로 설정
            -   defaultValue = "9999-12-31T23:59:59"
    -   반환하는 ResponseEntity의 body에서 호출하는 todoService의 getTodos()메서드에 weather, 기간 시작과 끝 조건 매개변수 추가
-   TodoService.java
    -   매개변수 추가
        -   String weather : 날씨 조건
        -   LocalDateTime startDate : 기간 시작 조건
        -   LocalDateTime endDate : 기간 끝 조건
    -   weather 조건 유무에 따라 Page todos를 초기화
        -   weather이 null일 때, findAllByDateRange() 메서드 사용
        -   weather 조건이 있을 때, findAllByWeatherAndDateRage() 메서드 사용
    -   TodoRepository.java
        -   findAllByDateRage() : 기간 조건만으로 할일 다건 조회하는 쿼리 메서드 추가
        -   findAllByWeatherAndDateRage() : weather 조건과 기간 조건으로 할일 다건 조회하는 쿼리 메서드 추가

---

#### 6.JPA Cascade

\[요구사항\]

-   할 일을 새로 저장할 시, 할 일을 생성한 유저는 담당자로 자동 등록되어야 합니다.
-   JPA의 `cascade` 기능을 활용해 할 일을 생성한 유저가 담당자로 등록될 수 있게 해주세요.

\[변경 사항\]

-   CaseCadeType.PERSIST 설정을 하면 부모 엔티티가 영속화될 때, 자식 엔티티도 자동으로 영속화됩니다.
-   다대다 관계에서 두 엔티티 간의 관계를 저장할 때, 중간 테이블에 저장해야 하는 데이터를 자동으로 영속화하는 경우 사용할 수 있습니다.
-   Todo.java의 managers 필드에 cascade 옵션으로 CadecadeType.PERSIST를 설정하여 문제를 해결하였습니다.

---

#### 7\. N+1 - 1) JOIN FETCH 사용

\[요구 사항\]

-   `CommentController` 클래스의 `getComments()` API를 호출할 때 N+1 문제가 발생하고 있어요. N+1 문제란, 데이터베이스 쿼리 성능 저하를 일으키는 대표적인 문제 중 하나로, 특히 연관된 엔티티를 조회할 때 발생해요.
-   해당 문제가 발생하지 않도록 코드를 수정해주세요.

\[N+1 문제란?\]  
N+1문제는 Lazy Loading이 설정된 다대일 또는 일대다 관계에서 발생할 수 있는 성능 문제로  
1개의 부모 엔티티를 조회할 때, N개의 자식 엔티티를 개별적으로 조회하는 추가적인 쿼리가 발생하여 총 N+1번의 쿼리가 실행되는 현상입니다.

\[실패한 방법\]  
처음에는 FetchType.LAZY로 변경하여 N+1문제를 해결해 보려고 했는데  
Comment의 user 필드의 FetchType을 변경하였지만 여전히 N+1 문제가 발생하였습니다.  
이 결과와 추가적인 검색을 통해 FetchType이 추가적인 쿼리 발생은 막을 수 있지만 처음 한번 날리는 쿼리가 반드시 최적화된 쿼리는 아니라는 것을 알게되었습니다.

\[해결 방법\]  
다음 해결방법으로 JOIN FETCH를 적용해 보았습니다.

CommentRepository의 findByTodoIdWithUser() 메서드에서 날리는 쿼리에 JOIN FETCH를 적용한 결과 N+1문제를 해결할 수 있었습니다.

---

#### 7\. N+1 - 2) @entitygraph 사용

\[요구 사항\]

-   `CommentController` 클래스의 `getComments()` API를 호출할 때 N+1 문제가 발생하고 있어요. N+1 문제란, 데이터베이스 쿼리 성능 저하를 일으키는 대표적인 문제 중 하나로, 특히 연관된 엔티티를 조회할 때 발생해요.
-   해당 문제가 발생하지 않도록 코드를 수정해주세요.

\[N+1 문제란?\]  
N+1문제는 Lazy Loading이 설정된 다대일 또는 일대다 관계에서 발생할 수 있는 성능 문제로  
1개의 부모 엔티티를 조회할 때, N개의 자식 엔티티를 개별적으로 조회하는 추가적인 쿼리가 발생하여 총 N+1번의 쿼리가 실행되는 현상입니다.

\[두번째 해결 방법\]  
다음 해결방법으로 @entitygraph를 적용해 보았습니다.

@entitygraph는 Spring Data JPA에서 특정 엔티티 필드(보통 연관 엔티티들)를 즉시로딩 하도록 지시하는 어노테이션입니다.  
이를 사용하면 쿼리 실행 시 연관된 엔티티들을 함께 로딩하여, 반복적인 지연 로딩으로 발생하는 N+1문제를 해결할 수 있습니다.  
attributePaths를 사용하여 즉시 로딩할 연관 엔티티를 지정합니다.

CommentRepository의 findByTodoIdWithUser() 메서드에 @entitygraph(attributePaths = {"user"}) 설정을 해 주어  
Comment의 연관 엔티티 중 User 엔티티를 즉시 로딩해서 문제를 해결하였습니다.

---

#### 8.QueryDSL - solution(1)

\[요구 사항\]

-   JPQL로 작성된 `findByIdWithUser` 를 QueryDSL로 변경합니다.
-   7번과 마찬가지로 N+1 문제가 발생하지 않도록 유의해 주세요!

QueryDSL을 처음 사용해 보아서  
특강때 튜터님이 Q객체 생성하실 때처럼 entity 폴더 안에 Q객체가 생기는 것인 줄 알고 설정을 잘못한 줄 알고 혼자 한참을 헤매며 gradle 설정 변경하고 이것저것 해보다  
나중에야 Q객체가 프로젝트 root 경로 build 파일 안에 생긴 것을 알았습니다.

하지만 여전히 QueryDSL 코드가 실행되지 않는 문제가 해결되지 않아  
결국 튜터님의 도움으로 querydsl 관련 의존성을 버전업 하고서야 queryDSL 코드를 실행할 수 있었습니다.

```
  'jakarta.persistence:jakarta.persistence-api:3.0.0' -> 'jakarta.persistence:jakarta.persistence-api:3.1.0'
```

튜터님께서 QueryDSL은 업그레이드가 되지 않아 버전 충돌 문제가 쉽게 발생할 수 있으니 조심해서 사용해야 한다고 하셨습니다.

(그런데 그냥 처음부터 깔끔하게 하고 싶어서 이전 커밋 내용으로 reset하고 다시 작성하였습니다.)

\[ 변경 사항 \]

-   build.gradle 의존성 추가
    -   querydsl 의존성 추가
-   QueryDslConfig.java 추가
    -   JPAQueryFactory Bean 추가
-   TodoRepository.java
    -   extends TodoQueryRepository 추가
    -   findByIdWithUser() JPQL 메서드 삭제
-   TodoQueryRepository 인터페이스 추가
    -   findByIdWithUser() 메서드 추가
-   TodoQueryRepositoryImpl.java
    -   TodoQueryRepository를 implements
    -   todoIdEq() 메서드 추가
        -   모의 면접 때, QueryDSL에서 동적 쿼리 작성 방법에서 처음 알게된 BooleanExpression을 사용해 보고 싶어서 만들어 보았습니다.
    -   findByIdWithUser() 메서드 오버라이드
        -   Todo Entity와 이와 연관관계를 가진 User Entity를 조회해 오기 위해 fetchJoin() 사용
        -   where()에 todoIdEq() 메서드 호출
        -   fetchOne()을 했을 때, 2개의 Entity가조회되면 에러가 발생할 수 있다고 해서 fetchFirst() 사용.
    -   TodoService.java
        -   findByIdWithUser() 메서드가 Todo 객체를 반환하므로 orElseThrow() 코드 삭제
        -   Todo가 null인 경우 EntityNotFoundException()을 throw

\[날아간 쿼리 확인\]

```
select
        todo
    from
        Todo todo
    inner join

    fetch
        todo.user as user
    where
        todo.id = ?1 */ select
            t1_0.id,
            t1_0.contents,
            t1_0.created_at,
            t1_0.modified_at,
            t1_0.title,
            t1_0.user_id,
            u1_0.id,
            u1_0.created_at,
            u1_0.email,
            u1_0.modified_at,
            u1_0.nickname,
            u1_0.password,
            u1_0.user_role,
            t1_0.weather
        from
            todos t1_0
        join
            users u1_0
                on u1_0.id=t1_0.user_id
        where
            t1_0.id=?
        limit
            ?
```

(쿼리가 1개만 날아간 것은 좋으나 User Entity에서 불필요한 필드도 로드해 오는 부분에서 쿼리 최적화가 조금 더 필요해 보이는 것 같습니다.)

---

#### 8.QueryDSL - solution(2) Projection.constructor() 사용

\[요구 사항\]

-   JPQL로 작성된 `findByIdWithUser` 를 QueryDSL로 변경합니다.
-   7번과 마찬가지로 N+1 문제가 발생하지 않도록 유의해 주세요!

\*\* POINT! \*\*

-   Level2. 8.QueryDSL - solution(1)에서 불필요한 필드까지 로드해 오는 부분을 수정
-   Projections.constructor()을 사용해 보고 싶었습니다.

\[ 변경 사항 \]

-   TodoQueryRepositoryImpl.java
    
    -   반환 객체를 TodoResponse로 변경
        -   select()에서 Projections.constructor() 사용
        -   fetch join()을 사용하려 했으나
    
    ```
      "org.hibernate.query.SemanticException: Query specified join fetching, but the owner of the fetched association was not present in the select list [SqmSingularJoin(org.example.expert.domain.todo.entity.Todo(todo).user(user) : user)]"
    ```
    
    에러 발생  
    fetchJoin()을 사용하지 않고 join()으로 user Entity의 필드를 가져옴.
-   TodoQueryRepository.java
    -   반환객체를 TodoResponse로 변경
-   TodoService.java
    -   repository에서 TodoResponse 객체를 반환하므로 null 여부만 체크하고 null이 아닌 경우 그대로 반환\[ 날아간 쿼리 \]
-   `Hibernate: /* select todo.id, todo.title, todo.contents, todo.weather, todo.user.id, todo.user.email, todo.user.nickname, todo.createdAt, todo.modifiedAt from Todo todo inner join todo.user as user where todo.id = ?1 */ select t1_0.id, t1_0.title, t1_0.contents, t1_0.weather, t1_0.user_id, u1_0.email, u1_0.nickname, t1_0.created_at, t1_0.modified_at from todos t1_0 join users u1_0 on u1_0.id=t1_0.user_id where t1_0.id=? limit ?`

---

#### 8.QueryDSL - solution(3) Tuple 객체 사용

\[요구 사항\]

-   JPQL로 작성된 `findByIdWithUser` 를 QueryDSL로 변경합니다.
-   7번과 마찬가지로 N+1 문제가 발생하지 않도록 유의해 주세요!

\*\* POINT! \*\*

-   Level2. 8.QueryDSL - solution(1)에서 불필요한 필드까지 로드해 오는 부분을 수정
-   모의면접 준비하를 하며 QueryDSL로 동적 쿼리 작성하는 방법에 있던 Tuple 객체를 사용해 보고 싶었다.

\[ 변경 사항 \]

-   TodoQueryRepositoryImpl.java
    
    -   select()에서 TodoResponse 객체를 생성하는 데 필요한 필드만 지정
        -   todo
        -   user.id
        -   user.email
        -   user.nickname
    -   fetchjoin()은 User Entity 전체를 로드해 오기 때문에 필요한 필드만 가져오고 싶어서 그냥 join() 사용
    -   TodoService에서 Q객체를 사용하는 것이 나을까? Repository에서 TodoResponse로 반환하는게 나을지 고민하다 그냥 Repository에서 TodoResponse로 반환하였습니다.
    
    \[ 날아간 쿼리 \]\[참고 : join과 fetchjoin 차이점\]
    
    -   Join
        -   조인된 엔티티는 필요할 때, 추가 쿼리리 로드(지연로딩)
        -   기본적으로 N+1문제가 발생할 수 있습니다.
        -   Lazy Loading 설정 적용
    -   Fetch Join
        -   즉시 로딩 : 연관된 엔티티가 즉시 함께 로딩됩니다.
        -   추가 쿼리가 발생하지 않습니다.
        -   Lazy Loading 설정이 무시됩니다.
        -   성능 상 이점이 있을 수 있지만 불필요하게 많은 데이터를 로드하면 성능 저하가 발생할 수 있습니다.
    
    (todoId로 todo 단건 조회의 경우,  
    Todo Entity와 User Entity가 @manytoone 관계이고  
    select 절에서 연관엔티티의 필드가 지정되어 있으므로 즉시 로드처럼 동작하여 N+1문제가 발생하지 않을 것이라 판단하고 코드를 작성하였습니다.  
    그런데 개인적으로는 Tuple 객체를 사용한 방법보다는 Projections.constructor()를 사용한 방법이 TodoResponse 객체로 반환하기 더 좋았던 것 같습니다.)
-   `Hibernate: /* select todo, user.id, user.email, user.nickname from Todo todo inner join todo.user as user where todo.id = ?1 */ select t1_0.id, t1_0.contents, t1_0.created_at, t1_0.modified_at, t1_0.title, t1_0.user_id, t1_0.weather, u1_0.email, u1_0.nickname from todos t1_0 join users u1_0 on u1_0.id=t1_0.user_id where t1_0.id=? limit ?`

---

#### 9.Spring Security

\[요구 사항\]

-   기존 `Filter`와 `Argument Resolver`를 사용하던 코드들을 Spring Security로 변경해주세요.
    -   접근 권한 및 유저 권한 기능은 그대로 유지해주세요.
    -   권한은 Spring Security의 기능을 사용해주세요.
-   토큰 기반 인증 방식은 유지할 거예요. JWT는 그대로 사용해주세요.

\[변경 사항\]

-   @Auth Annotation을 사용하지 않으므로 관련 클래스 삭제
    -   Auth.java 삭제
    -   AuthUserArgumentResolver.java 삭제
-   Spring Security 사용으로 이전 Filter 및 Config 클래스 삭제
    -   JwtFilter.java 삭제
    -   WebConfig.java 삭제
-   build.gradle에 Spring Security 관련 의존성 추가
-   JwtUtil.java 코드 추가
    -   JwtUtil.AUTHORIZATION\_HEADER 필드 추가
    -   validateToken() 메서드 추가  
        : token 유효성 검사 메서드
    -   getTokenFromRequest() 메서드 추가  
        : HttpServletRequest를 매개변수로 받아 JWT 토큰값을 반환하는 메서드
-   JwtSecurityFilter.java 추가  
    : 기존의 Filter를 대신할 클래스
-   JwtAuthenticationToken.java 추가  
    : 기존의 AuthUserArgumentResolver.java를 대신할 클래스
-   AuthUser.java 코드 추가
    -   getAuthorities() 메서드 추가  
        : UserDetailsImpl 객체로 부터 Authorities를 반환할 메서드
-   UserDetailsServiceImpl.java 추가
    -   UserDetailsService를 구현
    -   loadUserByUsername()메서드 Override  
        : User email로 부터 유효한 User인지 확인 후, UserDetailsImpl 객체 반환
-   UserDetailsImpl.java 추가
    -   UserDetails를 구현
    -   getAuthoritiest() 메서드 Override  
        : 인가 User의 인가 정보를 반환
    -   getPassword() 메서드는 필수적으로 구현해야해서 Override 메서드를 만들어 놓기는 했는데 JWT토큰에 비밀번호 정보는 필요하지 않기 때문에 따로 구현하지는 않았는데 이럴 경우, 그냥 null을 반환해도 되는지 아니면 더 좋은 방법이 있는지 아직 잘 모르겠습니다.
    -   getUsername() 메서드 Override  
        : AuthUser의 email을 반환
-   UserRole.java 코드 추가
    -   authority 필드 추가
    -   static 클래스 Authority 추가
-   SecurityConfig.java 추가
    -   Spring Security 설정
        -   csrf : disable
        -   JWT 토큰 방식이므로 sessionCreationPolicy를 STATELESS(무상태)로 설정
        -   Filter 순서 설정
        -   기본 로그인 폼 : disable
        -   회원가입, 로그인 요청시 인증, 인가 없이 통과하도록 설정
        -   test 요청시 ADMIN 권한 필요하도록 설정
        -   그외의 요청 URL은 모두 인증, 인가를 하도록 설정
-   UserRepository.java 코드 추가
    -   findAuthUserByEmail() 쿼리 메서드 추가  
        : 유저의 Email로 Optional 객체를 반환
-   기존 @Auth Annotation으로 인증 유저 정보를 받던 것을 @AuthenticationPrincipal로 변경
    -   CommentController.java
        -   saveComment()
    -   ManagerController.java
        -   saveManager()
        -   deleteManager()
    -   TodoController.java
        -   saveTodo()
    -   UserController.java
        -   changePassword()

## 💭 **이렇게 한 것이 맞을까?**

-   Spring Security 특강을 참고하여 9번 문제를 해결하였는데  
    UserDetailsImpl.java 추가하는 부분에서 UserDetails를 implements해서 getPassword() 메서드는 필수적으로 구현해야해서 Override 메서드를 만들어 놓기는 했는데 JWT토큰에 비밀번호 정보는 필요하지 않기 때문에 따로 구현하지는 않았는데 이럴 경우, 그냥 null을 반환해도 되는지 아니면 더 좋은 방법이 있는지 아직 잘 모르겠습니다.
-   JwtAuthenticationToken클래스에서 AuthUser 객체를 받아 JwtAuthenticationToken객체를 생성하는 과정에서  
    super(authUser.getAuthorities()); 부분이 있는데  
    getAuthorities() 메서드를 구현하는 방법은 따로 없어서  
    UserDetailsImpl객체를 authUser 객체로 생성하여 UserDetailsImpl 객체의 getAuthorities() 메서드를 호출하여 반환하였는데 이 방법이 맞는지 잘 모르겠습니다.
