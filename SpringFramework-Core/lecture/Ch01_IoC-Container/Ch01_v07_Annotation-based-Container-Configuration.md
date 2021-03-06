# Ch01. IoC Container

# v07. Annotation-based Container Configuration

- Java 파일에서 Annotation을 추가함으로써 Spring Container 설정을 추가하는 방법에 대해 학습

<br>

## 7.1 Spring 설정 시 Annotation 방식이 XML 방식 보다 좋은가?

- 상황에 따라 다르다

<br>

### 7.1.1 XML 방식

- 장점
  - 소스 코드 외에 XML 만 보고 Spring의 설정을 볼 수 있다.
  - 설정과 소스 코드의 분리
  - POJOs
- 단점
  - XML에 모든 설정을 넣다보니 XML의 덩어리가 너무 커진다. (XML Hell)
  - XML의 관리에 대한 부담감이 커짐

<br>

### 7.1.2 Annotation 방식

- 장점
  - `@Required`, `@Autowired` 등의 Annotation이 붙은 Java Annotation figure 을 통해 설정
- 단점
  - 소스 코드 안에 설정과 관련된 Java 코드가 추가됨
  - 설정과 소스 코드 분리의 한계
  - 완벽하게 POJOs 라고 볼 수 없음

<br>

## 7.2 Annotation 설정 방식

### 7.2.1 `<beans>` 에 `context` 설정

- XML 설정 방식의 `<beans>` 속성 중에는 `context` 라는 namespace가  존재하지 않는다.

- 아래와 같이 `context` 가 포함된 namespace로 이루어진 `<beans>` 사용

  ```xml
  <beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:context="http://www.springframework.org/schema/context"
      xsi:schemaLocation="http://www.springframework.org/schema/beans
          https://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/context
          https://www.springframework.org/schema/context/spring-context.xsd">
  ```

- 다음 코드를 xml 파일에 추가하면 annotation 방식의 spring 설정이 가능해진다.

  ```xml
  <context:annotation-config/>
  ```

<br>

### 7.2.2 `@Required`

- 자주 사용되지 않으므로 생략

<br>

### 7.2.3 `@Autowired`

- bean과 bean간의 관계에서 spring container가 자동으로 관계를 연결시켜주는 기능을 한다.
  (JSR 330의 `@Inject` 와 유사한 기능을 하는 annotation)
- 관계를 연결시켜주는 방법
  - bean의 이름(`name`)을 사용
  - bean의 유형(`type`)을 사용
- 다음 3가지 level에서 해당 annotation 사용 가능
  - 생성자 (Constructor)
  - Setter 메서드
  - Field
- `annotation` 패키지 하위의 소스 코드 참고

<br>

**`@Autowired (required = false)`**

- `@Autowired` annotation을 지정한 필드에 대한 `<bean>` 이 없는 경우 에러가 발생함
- 에러 발생 시 Container가 종료됨
- 이 때, `(required = false)` 를 지정하면 Container가 종료되지 않음

<br>

**`@Autowired` 를 통한 `Aware` 구현**

- `Aware` 인터페이스를 구현하지 않고 `@Autowired` annotation을 활용하여 `ApplicationContext`를 주입할 수 있다.

<br>

### 7.2.4 `@Primary`

- Autowired를 할 때 보통 이름(`name`) 또는 타입(`type`)을 통해 bean 사이의 관계를 설정한다.
- 이름이 존재하지 않을 때 **같은 타입**의 bean이 존재 할 경우 어떤 bean을 Autowired할 지 알지 못해 Spring이 에러를 발생시킨다.
- 이 때, 먼저 실행하고자 하는 bean에 `@Primary`  annotation을 지정하게 되면 `@Primary`가 붙은 bean부터 실행하게 된다.

<br>

### 7.2.5 `@Qualifier`

- 같은 타입의 bean을 만들 때 XML 방식에서 bean의 속성으로 `qualifier` 를 지정할 수 있다.
- `@Qualifier` annotation의 매개변수로 관계를 설정할 bean의 XML 설정 중 `qualifier` 의 `value`로 지정된 `id` 값을 넣어주게 되면 같은 타입의 bean들 중에 `id`가 일치하는 bean을 선택하여 관계를 설정하게 된다.

<br>

### 7.2.6 `@Resource`

- JSR-250에 있는 annotation (`javax.annotation.Resource`)
- `@Resource` 를 통해서도 `@Autowired`와 똑같은 효과를 볼 수 있다.
- `@Bean` annotation을 통해 bean을 생성하게 되면 Java 코드에서 함수의 이름이 bean의 이름(`name`)으로 지정된다.

- 다음 3가지 level에서 해당 annotation 사용 가능
  - 생성자 (Constructor)
  - Setter 메서드
  - Field

<br>

### 7.2.7 `@Value`

- 어플리케이션에서 XML 이외에도 `application.properties` 라는 설정 파일을 만들게 된다.
- 그 안에 있는 `properties` 파일을 읽어들인다.
- key-value 형태로 `properties`의 값들을 놓고, Spring 프로그램에서 사용하게 된다.
- 주로 다음과 같은 값들을 `properties` 파일로 별도로 빼서 사용하곤 한다.
  - JDBC의 `driverClass`, `url`, `user` 등
- `@PropertySource`로  `properties` 파일을 읽어온 뒤 `@Value` annotation을 이용해 파일 안의 내용을 사용할 수 있다.
- 사용 시, lombok의 `@Value`와 혼동하지 않도록 주의해야 한다.
- `properties` 파일에 없는 Key를 사용하면 null이 아닌 문자열이 할당된다.  

<br>

**`${}` vs `#{}`**

- `${}`
  - `@Value` annotation의 매개변수로서 `properties` 파일 내의 Key 값을 감싸는데 사용
- `#{}`
  - SpEL
  - `@Value` 와 함께 사용되어 `systemProperties` 를 확인할 수 있음
  - intellij에서 `Edit Configuration` 메뉴의 `VM options`에 값을 지정하여 `systemProperties` 를 수동으로 추가할 수 있다.
    - `-Dhello=world` : `hello` 라는 Key에 `world` 라는 값을 갖는 시스템 변수 추가 

<br>

### 7.2.8 `@PropertySource`

- `properties` 파일을 읽기 위해 해당 annotation 사용

- 해당 annotation의 매개변수로 `properties` 파일 지정

  ```java
  @PropertySource("classpath:application.properties")
  ```

  - `classpath`는 `main`인 경우에는 `java`와 `resources` 에 잡힌다.

<br>

### 7.2.9 `@PostConstruct` and `@PreDestroy`

- JSR-250 Lifecycle과 관련된 annotation이다.
- `@PostConstruct` : 생성자가 만들어진 이후에 동작
- `@PreDestroy` : bean이 destroy되기 전에 동작