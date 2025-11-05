# Web Programming Lab 1 - Spring Boot со Servlets

## Опис на проектот

Ова е лабораториска вежба за предметот Web Programming на ФИНКИ.
Проектот е Spring Boot апликација што користи **Servlets** за web слојот.

### Функционалност

Апликацијата овозможува:
1. Избор на готвач од листа
2. Избор на јадење од листа
3. Додавање на јадење во листата на јадења што ги подготвува избраниот готвач
4. Приказ на детали за готвачот и сите негови јадења

## Архитектура на апликацијата

Проектот следи **multi-tier архитектура**:

```
┌─────────────────────────────────────┐
│     Web Layer (Presentation)       │  ← Servlets (ChefListServlet, DishServlet, ChefDetailsServlet)
├─────────────────────────────────────┤
│     Service Layer (Business Logic) │  ← ChefService, DishService
├─────────────────────────────────────┤
│     Repository Layer (Data Access) │  ← ChefRepository, DishRepository
├─────────────────────────────────────┤
│     Model Layer (Domain Objects)   │  ← Chef, Dish
├─────────────────────────────────────┤
│     Data Storage (In-Memory)       │  ← DataHolder (static lists)
└─────────────────────────────────────┘
```

## Структура на проектот

```
src/
├── main/
│   ├── java/mk/ukim/finki/wp/lab/
│   │   ├── LabApplication.java          # Главна Spring Boot класа
│   │   ├── config/                      # Конфигурација
│   │   │   └── ThymeleafConfig.java     # Thymeleaf конфигурација
│   │   ├── model/                       # Model слој
│   │   │   ├── Chef.java                # Готвач (id, firstName, lastName, bio, dishes)
│   │   │   └── Dish.java                # Јадење (dishId, name, cuisine, preparationTime)
│   │   ├── bootstrap/                   # Bootstrap/иницијализација
│   │   │   └── DataHolder.java          # Статички податоци (5 готвачи, 5 јадења)
│   │   ├── repository/                  # Repository слој
│   │   │   ├── ChefRepository.java      # Интерфејс за готвачи
│   │   │   ├── InMemoryChefRepository.java  # In-memory имплементација
│   │   │   ├── DishRepository.java      # Интерфејс за јадења
│   │   │   └── InMemoryDishRepository.java  # In-memory имплементација
│   │   ├── service/                     # Service слој
│   │   │   ├── ChefService.java         # Интерфејс за готвачи
│   │   │   ├── DishService.java         # Интерфејс за јадења
│   │   │   └── impl/                    # Имплементации
│   │   │       ├── ChefServiceImpl.java
│   │   │       └── DishServiceImpl.java
│   │   └── web/                         # Web слој (Servlets)
│   │       ├── ChefListServlet.java     # Приказ на листа на готвачи
│   │       ├── DishServlet.java         # Приказ на листа на јадења
│   │       └── ChefDetailsServlet.java  # Приказ на детали за готвач
│   └── resources/
│       ├── templates/                   # Thymeleaf HTML template фајлови
│       │   ├── listChefs.html           # Template за листа на готвачи
│       │   ├── dishesList.html          # Template за листа на јадења
│       │   └── chefDetails.html         # Template за детали за готвач
│       └── application.properties       # Конфигурација на апликацијата
├── TEST_INSTRUCTIONS.md                 # Детални инструкции за тестирање
└── pom.xml                              # Maven конфигурација
```

## Технологии

- **Java 17** - Програмски јазик
- **Spring Boot 3.1.5** - Framework за апликацијата
- **Maven** - Build tool и dependency management
- **Jakarta Servlets** - Web слој (наместо Spring MVC)
- **Thymeleaf** - Template engine за динамичко генерирање на HTML
- **Lombok** - За автоматско генерирање на getters/setters/constructors

## Flow на апликацијата

```
1. Корисникот оди на: http://localhost:8080/listChefs
   ↓
   ChefListServlet прикажува листа на готвачи со radio buttons
   ↓
2. Корисникот избира готвач и кликнува Submit
   ↓
   POST барање до /dish со chefId
   ↓
   DishServlet прикажува листа на јадења + информации за избраниот готвач
   ↓
3. Корисникот избира јадење и кликнува "Add dish"
   ↓
   POST барање до /chefDetails со chefId и dishId
   ↓
   ChefDetailsServlet:
   - Го додава јадењето во листата на готвачот (преку chefService.addDishToChef())
   - Прикажува детали за готвачот и сите негови јадења
```

## Како да се стартува апликацијата?

### 1. Преку Maven (од командна линија)

```bash
# Компајлирање и стартување
mvn spring-boot:run
```

### 2. Преку IDE (IntelliJ IDEA / Eclipse)

1. Отвори го проектот во IDE
2. Најди ја `LabApplication.java` класата
3. Кликни на `Run` / `Debug`

### 3. Преку JAR фајл

```bash
# Компајлирање
mvn clean package

# Стартување
java -jar target/lab-0.0.1-SNAPSHOT.jar
```

### Пристап до апликацијата

Откако ќе се стартува апликацијата, отвори browser и оди на:

```
http://localhost:8080/listChefs
```

## Клучни концепти

### 1. Dependency Injection (DI)

Spring автоматски ги инјектира зависностите преку конструктори:

```java
public ChefListServlet(ChefService chefService) {
    this.chefService = chefService;  // Spring го инјектира bean-от
}
```

### 2. Servlets

- `@WebServlet` - анотација за мапирање на сервлет на URL патека
- `doGet()` - за GET барања (читање)
- `doPost()` - за POST барања (креирање/ажурирање)

### 3. Repository Pattern

Одвојување на логиката за пристап до податоци од деловната логика.

### 4. Service Layer

Содржи деловна логика и ги координира repository-јата.

### 5. In-Memory Storage

Податоците се чуваат во меморијата (DataHolder статички листи).
При рестарт на апликацијата, промените се губат.

## Важни забелешки

1. **@ServletComponentScan** анотацијата во `LabApplication` е ЗАДОЛЖИТЕЛНА за да работат сервлетите!

2. **Секој сервлет има dependency injection** - Spring ги инјектира потребните service-и и SpringTemplateEngine.

3. **UTF-8 encoding** е конфигуриран за поддршка на кирилични карактери.

4. **Thymeleaf се користи за генерирање на HTML** - сервлетите користат Thymeleaf template engine за rendering на HTML фајловите.

5. **HTML template фајлови** се наоѓаат во `src/main/resources/templates/` и користат Thymeleaf синтакса (th:each, th:text, th:value, итн.)

6. **ThymeleafConfig** класа го конфигурира Thymeleaf template engine со SpringResourceTemplateResolver.

7. **Hidden inputs** се користат за пренос на податоци меѓу формите (chefId во DishServlet).

## Thymeleaf основни концепти

### Динамичко генерирање на content

```html
<!-- th:each - loop низ листа -->
<div th:each="chef : ${chefs}">
    <!-- th:value - динамички поставува вредност -->
    <input type="radio" th:value="${chef.id}">
    <!-- th:text - динамички поставува текст -->
    <span th:text="${chef.firstName} + ' ' + ${chef.lastName}"></span>
</div>
```

### Условно прикажување

```html
<!-- th:if - прикажи ако условот е точен -->
<p th:if="${chef.dishes.isEmpty()}">No dishes yet.</p>

<!-- th:unless - прикажи ако условот е неточен -->
<ul th:unless="${chef.dishes.isEmpty()}">
    <li th:each="dish : ${chef.dishes}" th:text="${dish.name}"></li>
</ul>
```

## Можни подобрувања

- [ ] Додавање на валидација на податоци
- [ ] Додавање на error handling
- [ ] Користење на база на податоци наместо in-memory листи
- [ ] Додавање на можност за бришење на јадење од готвач
- [ ] Додавање на CSS framework (Bootstrap)
- [ ] Додавање на unit тестови за service слојот
- [ ] Додавање на integration тестови за сервлетите

## Автор

Лабораториска вежба 1 - ФИНКИ Web Programming
