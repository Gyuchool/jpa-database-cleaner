[![](https://jitpack.io/v/Gyuchool/jpa-database-cleaner.svg)](https://jitpack.io/#Gyuchool/jpa-database-cleaner)

# database-cleaner

만든 계기는 스프링 테스트에서 @Transactional을 통한 롤백이 비즈니스 로직 검증에 영향을 미치기에 오직 롤백을 위한 데이터베이스 클리너를 만들게 되었다.

데이터베이스 클리너는 JPA를 사용할때 데이터베이스를 롤백하기 위한 전략중 하나이다. EntityManager를 통해 엔티티에서 Table Annotation의 이름을 읽고 truncate명령어를 사용하여 롤백한다.

### 사용법
테스트 클래스 상위에 @ExtendWith(RollbackExtension.class)를 적용하면 된다.

### 주의사항
엔티티 상위에 @Table 어노테이션이 있어야만 한다.


The reason for making this was because in Spring testing, the rollback through @Transactional affected the verification of business logic, so a database cleaner was created solely for rollback.

The database cleaner is one of the strategies for rolling back the database when using JPA. It reads the name of the Table Annotation from the entity through the EntityManager and rolls back using the truncate command.

### Usage
Apply @ExtendWith(RollbackExtension.class) at the top of the test class.

### Precaution
There must be a @Table annotation at the top of the entity.
