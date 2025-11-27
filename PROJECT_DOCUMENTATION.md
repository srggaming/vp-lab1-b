# Spring Boot Lab Project - Restaurant Management System
## Документација за проектот

---

## 📋 Содржина
1. [Преглед на проектот](#преглед-на-проектот)
2. [Архитектура](#архитектура)
3. [Структура на проектот](#структура-на-проектот)
4. [Модели (Models)](#модели-models)
5. [Repository слој](#repository-слој)
6. [Service слој](#service-слој)
7. [Web слој - Servlets и Controllers](#web-слој)
8. [Погледи (Views)](#погледи-views)
9. [Flow на податоци](#flow-на-податоци)
10. [Функционалности](#функционалности)
11. [Како да се тестира](#како-да-се-тестира)

---

## 🎯 Преглед на проектот

Ова е **Spring Boot** апликација за управување со ресторан која дозволува:
- Управување со готвачи (Chefs) и јадења (Dishes)
- Доделување на јадења на готвачи
- CRUD операции за јадења
- Преглед на најпопуларен готвач (со најмногу јадења)
- Сортирање на готвачи по број на јадења

### Технологии:
- **Spring Boot 3.1.5**
- **Thymeleaf** (template engine)
- **Jakarta Servlets** за Lab 1
- **Spring MVC Controllers** за Lab 2
- **Lombok** за намалување на boilerplate код
- **In-memory storage** (без база на податоци)

---

## 🏗️ Архитектура

Проектот следи **Multi-Tier Architecture (Layered Architecture)**:

```
┌─────────────────────────────────────┐
│     Presentation Layer (Web)        │
│   - Servlets (Lab 1)                │
│   - Controllers (Lab 2)             │
│   - Thymeleaf Templates (Views)     │
└──────────┬──────────────────────────┘
           │
           ↓
┌─────────────────────────────────────┐
│      Business Logic Layer           │
│    - Service Interfaces             │
│    - Service Implementations        │
└──────────┬──────────────────────────┘
           │
           ↓
┌─────────────────────────────────────┐
│      Data Access Layer              │
│    - Repository Interfaces          │
│    - InMemory Implementations       │
└──────────┬──────────────────────────┘
           │
           ↓
┌─────────────────────────────────────┐
│         Data Layer                  │
│    - DataHolder (static lists)      │
│    - Model classes (Chef, Dish)     │
└─────────────────────────────────────┘
```

### Зошто оваа архитектура?
- **Separation of Concerns**: Секој слој има своја одговорност
- **Loose Coupling**: Слоевите зависат од апстракции (интерфејси)
- **Testability**: Лесно за тестирање на секој слој посебно
- **Maintainability**: Лесно за ажурирање и проширување

---

## 📁 Структура на проектот

```
src/main/java/mk/ukim/finki/wp/lab/
│
├── model/                          # Модели (Domain entities)
│   ├── Chef.java                   # Chef ентитет
│   └── Dish.java                   # Dish ентитет
│
├── repository/                     # Data Access слој
│   ├── ChefRepository.java         # Interface
│   ├── DishRepository.java         # Interface
│   ├── InMemoryChefRepository.java # Имплементација
│   └── InMemoryDishRepository.java # Имплементација
│
├── service/                        # Business Logic слој
│   ├── ChefService.java            # Interface
│   ├── DishService.java            # Interface
│   └── impl/
│       ├── ChefServiceImpl.java    # Имплементација
│       └── DishServiceImpl.java    # Имплементација
│
├── web/                            # Presentation слој
│   ├── controller/                 # Spring MVC Controllers (Lab 2)
│   │   └── DishController.java
│   ├── ChefListServlet.java        # Servlets (Lab 1)
│   ├── DishServlet.java
│   └── ChefDetailsServlet.java
│
├── bootstrap/                      # Иницијални податоци
│   └── DataHolder.java             # Static lists со податоци
│
└── config/                         # Конфигурација
    └── ThymeleafConfig.java        # Thymeleaf setup

src/main/resources/
└── templates/                      # Thymeleaf HTML шаблони
    ├── listChefs.html              # Листа на готвачи
    ├── dishesList.html             # Избор на јадење
    ├── chefDetails.html            # Детали за готвач
    ├── listDishes.html             # CRUD листа на јадења
    └── dish-form.html              # Форма за додавање/уредување јадење
```

---

## 🎭 Модели (Models)

### Chef.java
```java
@Data
@NoArgsConstructor
public class Chef {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private List<Dish> dishes = new ArrayList<>();
}
```

**Својства:**
- `id` - уникатен идентификатор
- `firstName` - име
- `lastName` - презиме
- `bio` - биографија/опис
- `dishes` - листа на јадења што готвачот ги подготвува

### Dish.java
```java
@Data
@NoArgsConstructor
public class Dish {
    private static Long counter = 1L;  // Статички counter за автоматско ID

    private Long id;                   // Long ID (Lab 2)
    private String dishId;             // String ID (Lab 1)
    private String name;
    private String cuisine;
    private int preparationTime;

    public Dish(String dishId, String name, String cuisine, int preparationTime) {
        this.id = counter++;  // Автоматско генерирање на ID
        this.dishId = dishId;
        this.name = name;
        this.cuisine = cuisine;
        this.preparationTime = preparationTime;
    }
}
```

**Својства:**
- `id` - уникатен Long идентификатор (автоматски генериран)
- `dishId` - String идентификатор за компатибилност
- `name` - име на јадењето
- `cuisine` - тип на кујна (Italian, French, итн.)
- `preparationTime` - време за подготовка во минути

**Генерирање на ID:**
- Користи статички counter кој се инкрементира за секој нов Dish
- При креирање на објект со конструктор `Dish(String, String, String, int)`, автоматски се генерира ID

---

## 💾 Repository слој

### ChefRepository.java
```java
public interface ChefRepository {
    List<Chef> findAll();
    Optional<Chef> findById(Long id);
    Chef save(Chef chef);
}
```

### DishRepository.java
```java
public interface DishRepository {
    List<Dish> findAll();
    Dish findByDishId(String dishId);
    Optional<Dish> findById(Long id);
    Dish save(Dish dish);
    void deleteById(Long id);
}
```

### InMemoryDishRepository.java - Имплементација
```java
@Repository
public class InMemoryDishRepository implements DishRepository {
    @Override
    public Dish save(Dish dish) {
        // Ако постои dish со исто ID, замени го
        Optional<Dish> existingDish = this.findById(dish.getId());
        existingDish.ifPresent(d -> DataHolder.dishes.remove(d));
        DataHolder.dishes.add(dish);
        return dish;
    }

    @Override
    public void deleteById(Long id) {
        DataHolder.dishes.removeIf(dish -> dish.getId().equals(id));
    }
}
```

**Како работи:**
- Сите податоци се чуваат во `DataHolder.dishes` (static List)
- `save()` - ако ID постои → ажурирање, инаку → додавање
- `deleteById()` - брише од листата со `removeIf()`

---

## 🔧 Service слој

### ChefService.java
```java
public interface ChefService {
    List<Chef> listChefs();
    Chef findById(Long id);
    Chef addDishToChef(Long chefId, String dishId);
    Optional<Chef> findMostPopularChef();
}
```

### DishService.java
```java
public interface DishService {
    List<Dish> listDishes();
    Dish findByDishId(String dishId);
    Dish findById(Long id);
    Dish create(String dishId, String name, String cuisine, int preparationTime);
    Dish update(Long id, String dishId, String name, String cuisine, int preparationTime);
    void delete(Long id);
}
```

### ChefServiceImpl.java - Важни методи

#### 1. addDishToChef()
```java
@Override
public Chef addDishToChef(Long chefId, String dishId) {
    if (dishId == null || dishId.trim().isEmpty()) {
        throw new RuntimeException("Dish ID cannot be empty");
    }
    Chef chef = this.findById(chefId);
    Dish dish = this.dishRepository.findByDishId(dishId);
    if (dish == null) {
        throw new RuntimeException("Dish not found with id: " + dishId);
    }
    chef.getDishes().add(dish);
    return this.chefRepository.save(chef);
}
```
**Што прави:**
1. Валидира дека dishId не е празен
2. Наоѓа готвач по chefId
3. Наоѓа јадење по dishId
4. Го додава јадењето во листата на готвачот
5. Го зачувува ажурираниот готвач

#### 2. findMostPopularChef()
```java
@Override
public Optional<Chef> findMostPopularChef() {
    return this.chefRepository.findAll().stream()
            .max(Comparator.comparingInt(chef -> chef.getDishes().size()));
}
```
**Што прави:**
- Го наоѓа готвачот со најмногу јадења (најпопуларен)
- Користи Stream API и `max()` со Comparator

### DishServiceImpl.java - CRUD операции

```java
@Override
public Dish create(String dishId, String name, String cuisine, int preparationTime) {
    Dish dish = new Dish(dishId, name, cuisine, preparationTime);
    return this.dishRepository.save(dish);
}

@Override
public Dish update(Long id, String dishId, String name, String cuisine, int preparationTime) {
    Dish dish = this.findById(id);
    dish.setDishId(dishId);
    dish.setName(name);
    dish.setCuisine(cuisine);
    dish.setPreparationTime(preparationTime);
    return this.dishRepository.save(dish);
}

@Override
public void delete(Long id) {
    this.dishRepository.deleteById(id);
}
```

---

## 🌐 Web слој

### Lab 1: Servlets

#### ChefListServlet.java - `/listChefs`
```java
@WebServlet(name = "ChefListServlet", urlPatterns = "/listChefs")
public class ChefListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // 1. Земи ги сите готвачи и сортирај по број на јадења (descending)
        List<Chef> chefs = this.chefService.listChefs().stream()
                .sorted(Comparator.comparingInt((Chef chef) ->
                        chef.getDishes().size()).reversed())
                .collect(Collectors.toList());

        // 2. Најди најпопуларен готвач
        Chef mostPopularChef = this.chefService.findMostPopularChef().orElse(null);

        // 3. Постави ги во контекстот
        context.setVariable("chefs", chefs);
        context.setVariable("mostPopularChef", mostPopularChef);

        // 4. Прикажи го listChefs.html
        templateEngine.process("listChefs", context, resp.getWriter());
    }
}
```

#### DishServlet.java - `/dish`
```java
@WebServlet(name = "DishServlet", urlPatterns = "/dish")
public class DishServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        // 1. Земи chefId од request
        Long chefId = Long.parseLong(req.getParameter("chefId"));

        // 2. Најди го готвачот
        Chef selectedChef = this.chefService.findById(chefId);

        // 3. Земи ги сите јадења
        List<Dish> dishes = this.dishService.listDishes();

        // 4. Прикажи dishesList.html со изберен готвач и сите јадења
        context.setVariable("selectedChef", selectedChef);
        context.setVariable("dishes", dishes);
        templateEngine.process("dishesList", context, resp.getWriter());
    }
}
```

#### ChefDetailsServlet.java - `/chefDetails`
```java
@WebServlet(name = "ChefDetailsServlet", urlPatterns = "/chefDetails")
public class ChefDetailsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        // 1. Земи chefId и dishId од request
        Long chefId = Long.parseLong(req.getParameter("chefId"));
        String dishId = req.getParameter("dishId");

        // 2. Додај јадење на готвачот (преку service)
        Chef chef = this.chefService.addDishToChef(chefId, dishId);

        // 3. Прикажи chefDetails.html со ажуриран готвач
        context.setVariable("chef", chef);
        templateEngine.process("chefDetails", context, resp.getWriter());
    }
}
```

### Lab 2: Spring MVC Controllers

#### DishController.java - `/dishes`
```java
@Controller
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;

    // 1. Листа на јадења
    @GetMapping
    public String getDishesPage(@RequestParam(required = false) String error, Model model) {
        List<Dish> dishes = this.dishService.listDishes();
        model.addAttribute("dishes", dishes);
        model.addAttribute("error", error);
        return "listDishes";
    }

    // 2. Форма за додавање ново јадење
    @GetMapping("/dish-form")
    public String getAddDishPage(Model model) {
        model.addAttribute("dish", null);
        return "dish-form";
    }

    // 3. Форма за уредување јадење
    @GetMapping("/dish-form/{id}")
    public String getEditDishForm(@PathVariable Long id, Model model) {
        try {
            Dish dish = this.dishService.findById(id);
            model.addAttribute("dish", dish);
            return "dish-form";
        } catch (RuntimeException e) {
            return "redirect:/dishes?error=DishNotFound";
        }
    }

    // 4. Додавање ново јадење
    @PostMapping("/add")
    public String saveDish(@RequestParam String dishId,
                           @RequestParam String name,
                           @RequestParam String cuisine,
                           @RequestParam int preparationTime) {
        this.dishService.create(dishId, name, cuisine, preparationTime);
        return "redirect:/dishes";
    }

    // 5. Ажурирање јадење
    @PostMapping("/edit/{id}")
    public String editDish(@PathVariable Long id,
                           @RequestParam String dishId,
                           @RequestParam String name,
                           @RequestParam String cuisine,
                           @RequestParam int preparationTime) {
        this.dishService.update(id, dishId, name, cuisine, preparationTime);
        return "redirect:/dishes";
    }

    // 6. Бришење јадење
    @GetMapping("/delete/{id}")
    public String deleteDish(@PathVariable Long id) {
        this.dishService.delete(id);
        return "redirect:/dishes";
    }
}
```

**Разлика помеѓу Servlets и Controllers:**

| Servlet | Spring MVC Controller |
|---------|----------------------|
| `@WebServlet` | `@Controller` |
| `doGet()`, `doPost()` | `@GetMapping`, `@PostMapping` |
| `HttpServletRequest`, `HttpServletResponse` | `Model`, `@RequestParam`, `@PathVariable` |
| `templateEngine.process()` | `return "viewName"` |
| Мануелно управување со request/response | Spring автоматски управува |

---

## 👁️ Погледи (Views)

### listChefs.html - Листа на готвачи
**URL:** `/listChefs`

**Што прикажува:**
- Листа на готвачи (сортирани по број на јадења)
- Најпопуларен готвач (highlight со ⭐)
- Статистика за секој готвач (број на јадења)
- Форма за избор на готвач

```html
<!-- Најпопуларен готвач (ако постои) -->
<div th:if="${mostPopularChef != null}">
    <strong>Most Popular Chef:</strong>
    <span th:text="${mostPopularChef.firstName} + ' ' +
                    ${mostPopularChef.lastName} + ' with ' +
                    ${mostPopularChef.dishes.size()} + ' dish(es)'"></span>
</div>

<!-- Листа на готвачи -->
<form action="/dish" method="POST">
    <div th:each="chef : ${chefs}">
        <input type="radio" name="chefId" th:value="${chef.id}">
        <span th:text="${chef.firstName} + ' ' + ${chef.lastName}"></span>
        <!-- Badge за најпопуларен -->
        <span th:if="${chef.id == mostPopularChef.id}">⭐ MOST POPULAR</span>
        <br/>
        <!-- Статистика -->
        <small>Statistics: <span th:text="${chef.dishes.size()} + ' dish(es)'"></span></small>
    </div>
    <input type="submit" value="Submit">
</form>
```

### dishesList.html - Избор на јадење
**URL:** `/dish` (POST од listChefs.html)

**Што прикажува:**
- Избран готвач (ID, име)
- Листа на достапни јадења
- Форма за избор на јадење

```html
<form action="/chefDetails" method="POST">
    <input type="hidden" name="chefId" th:value="${selectedChef.id}">
    <div th:each="dish : ${dishes}">
        <input type="radio" name="dishId" th:value="${dish.dishId}">
        <span th:text="${dish.name} + ' (' + ${dish.cuisine} + ')'"></span>
    </div>
    <input type="submit" value="Add dish">
</form>

<!-- JavaScript валидација -->
<script>
    function validateDishSelection() {
        var dishId = document.querySelector('input[name="dishId"]:checked');
        if (!dishId) {
            alert('Please select a dish before submitting!');
            return false;
        }
        return true;
    }
</script>
```

### chefDetails.html - Детали за готвач
**URL:** `/chefDetails` (POST од dishesList.html)

**Што прикажува:**
- Име на готвач
- Биографија
- Листа на јадења што ги подготвува
- Копче за враќање назад

```html
<h1 th:text="'Chef: ' + ${chef.firstName} + ' ' + ${chef.lastName}"></h1>
<h2 th:text="'Bio: ' + ${chef.bio}"></h2>

<h2>Dishes prepared by this chef:</h2>
<ul th:unless="${chef.dishes == null or chef.dishes.isEmpty()}">
    <!-- Филтрирање на null dishes -->
    <li th:each="dish : ${chef.dishes}" th:if="${dish != null}">
        <span th:text="${dish.name} + ' (' + ${dish.cuisine} + ')'"></span>
    </li>
</ul>
```

### listDishes.html - CRUD листа
**URL:** `/dishes`

**Што прикажува:**
- Табела со сите јадења
- Копче за додавање ново јадење
- Копчиња за уредување и бришење

```html
<a th:href="@{/dishes/dish-form}" class="add-button">➕ Add New Dish</a>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Dish ID</th>
            <th>Name</th>
            <th>Cuisine</th>
            <th>Preparation Time</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="dish : ${dishes}">
            <td th:text="${dish.id}"></td>
            <td th:text="${dish.dishId}"></td>
            <td th:text="${dish.name}"></td>
            <td th:text="${dish.cuisine}"></td>
            <td th:text="${dish.preparationTime}"></td>
            <td>
                <a th:href="@{/dishes/dish-form/{id}(id=${dish.id})}">✏️ Edit</a>
                <a th:href="@{/dishes/delete/{id}(id=${dish.id})}"
                   onclick="return confirm('Are you sure?')">🗑️ Delete</a>
            </td>
        </tr>
    </tbody>
</table>
```

### dish-form.html - Форма за додавање/уредување
**URL:** `/dishes/dish-form` (ново) или `/dishes/dish-form/{id}` (уредување)

**Што прикажува:**
- Форма со полиња: dishId, name, cuisine, preparationTime
- Се користи иста форма за додавање и уредување

```html
<h1 th:text="${dish != null ? 'Edit Dish' : 'Add New Dish'}"></h1>

<form th:action="${dish != null ? '/dishes/edit/' + dish.id : '/dishes/add'}"
      method="POST">
    <input type="text" name="dishId" th:value="${dish != null ? dish.dishId : ''}" required>
    <input type="text" name="name" th:value="${dish != null ? dish.name : ''}" required>
    <input type="text" name="cuisine" th:value="${dish != null ? dish.cuisine : ''}" required>
    <input type="number" name="preparationTime"
           th:value="${dish != null ? dish.preparationTime : ''}" required>

    <button type="submit" th:text="${dish != null ? 'Update' : 'Add'}"></button>
    <a th:href="@{/dishes}">Cancel</a>
</form>
```

---

## 🔄 Flow на податоци

### Flow 1: Преглед и избор на готвач → јадење

```
1. Корисник → GET /listChefs
   ↓
2. ChefListServlet
   - Повикува chefService.listChefs()
   - Сортира по број на јадења (descending)
   - Наоѓа најпопуларен готвач
   ↓
3. Прикажува listChefs.html
   - Листа на готвачи
   - Highlight на најпопуларен
   - Статистика
   ↓
4. Корисник избира готвач → POST /dish
   ↓
5. DishServlet
   - Повикува chefService.findById(chefId)
   - Повикува dishService.listDishes()
   ↓
6. Прикажува dishesList.html
   - Избран готвач
   - Листа на јадења
   ↓
7. Корисник избира јадење → POST /chefDetails
   ↓
8. ChefDetailsServlet
   - Повикува chefService.addDishToChef(chefId, dishId)
   - Service повикува repository.save()
   ↓
9. Прикажува chefDetails.html
   - Детали за готвач
   - Листа на негови јадења (вклучувајќи го новото)
```

### Flow 2: CRUD операции за јадења

```
1. Корисник → GET /dishes
   ↓
2. DishController.getDishesPage()
   - Повикува dishService.listDishes()
   ↓
3. Прикажува listDishes.html
   ↓

ДОДАВАЊЕ:
4a. Корисник кликнува "Add New Dish" → GET /dishes/dish-form
    ↓
5a. DishController.getAddDishPage()
    - model.addAttribute("dish", null)
    ↓
6a. Прикажува dish-form.html (празна форма)
    ↓
7a. Корисник внесува податоци → POST /dishes/add
    ↓
8a. DishController.saveDish()
    - Повикува dishService.create()
    - Repository зачувува во DataHolder.dishes
    ↓
9a. Редирект → /dishes

УРЕДУВАЊЕ:
4b. Корисник кликнува "Edit" → GET /dishes/dish-form/{id}
    ↓
5b. DishController.getEditDishForm(id)
    - Повикува dishService.findById(id)
    ↓
6b. Прикажува dish-form.html (пополнета со податоци)
    ↓
7b. Корисник ги менува податоците → POST /dishes/edit/{id}
    ↓
8b. DishController.editDish(id)
    - Повикува dishService.update(id, ...)
    - Repository ажурира во DataHolder.dishes
    ↓
9b. Редирект → /dishes

БРИШЕЊЕ:
4c. Корисник кликнува "Delete" → GET /dishes/delete/{id}
    ↓
5c. DishController.deleteDish(id)
    - Повикува dishService.delete(id)
    - Repository брише од DataHolder.dishes
    ↓
6c. Редирект → /dishes
```

---

## ⚙️ Функционалности

### Lab 1 Б (Servlets)

#### 1. Преглед на готвачи
- **URL:** `/listChefs`
- **Функција:** Прикажува листа на готвачи сортирани по број на јадења
- **Детали:**
  - Најпопуларен готвач е истакнат со badge ⭐
  - Статистика за секој готвач (број на јадења)
  - Форма за избор на готвач

#### 2. Избор на јадење
- **URL:** `/dish` (POST)
- **Функција:** Прикажува јадења за избран готвач
- **Детали:**
  - Приказ на избраниот готвач
  - Листа на достапни јадења
  - JavaScript валидација (мора да се избере јадење)

#### 3. Детали за готвач
- **URL:** `/chefDetails` (POST)
- **Функција:** Додава јадење на готвач и прикажува детали
- **Детали:**
  - Додавање јадење преку `chefService.addDishToChef()`
  - Приказ на сите јадења на готвачот
  - Филтрирање на null dishes

#### 4. Најпопуларен готвач
- **Функција:** Пронаоѓа и истакнува готвач со најмногу јадења
- **Имплементација:**
  ```java
  Optional<Chef> findMostPopularChef() {
      return chefRepository.findAll().stream()
              .max(Comparator.comparingInt(chef -> chef.getDishes().size()));
  }
  ```

### Lab 2 Б (Controllers + CRUD)

#### 1. Листа на јадења
- **URL:** `/dishes`
- **Функција:** Прикажува табела со сите јадења
- **Детали:**
  - Приказ на ID, dishId, name, cuisine, preparationTime
  - Копчиња за уредување и бришење
  - Копче за додавање ново јадење

#### 2. Додавање јадење
- **URL:** `/dishes/dish-form` (GET), `/dishes/add` (POST)
- **Функција:** Форма за креирање ново јадење
- **Детали:**
  - Празна форма со полиња за внесување
  - При submit → `dishService.create()`
  - Автоматско генерирање на Long ID

#### 3. Уредување јадење
- **URL:** `/dishes/dish-form/{id}` (GET), `/dishes/edit/{id}` (POST)
- **Функција:** Форма за ажурирање јадење
- **Детали:**
  - Пополнета форма со постоечки податоци
  - При submit → `dishService.update(id, ...)`
  - Ако не постои → redirect со error

#### 4. Бришење јадење
- **URL:** `/dishes/delete/{id}` (GET)
- **Функција:** Брише јадење
- **Детали:**
  - JavaScript confirm() за потврда
  - Повикува `dishService.delete(id)`
  - Го брише од in-memory листата

---

## 🧪 Како да се тестира

### Prerequisite:
```bash
# Стартување на апликацијата
mvn spring-boot:run

# Апликацијата работи на:
http://localhost:8080
```

### Test Scenario 1: Chef & Dish Flow (Lab 1)

1. **Отвори листа на готвачи:**
   ```
   GET http://localhost:8080/listChefs
   ```
   - Треба да видиш 5 готвачи
   - Сите имаат 0 јадења иницијално
   - Нема истакнат најпопуларен готвач (сите се еднакви)

2. **Избери готвач:**
   - Кликни на radio button за готвач (пр. Gordon Ramsay)
   - Кликни "Submit"
   - URL: `POST /dish`

3. **Избери јадење:**
   - Треба да видиш листа на 5 јадења
   - Избери едно јадење (пр. Pasta Carbonara)
   - Кликни "Add dish"
   - URL: `POST /chefDetails`

4. **Провери детали:**
   - Треба да го видиш Gordon Ramsay
   - Неговата биографија
   - Листа со 1 јадење: "Pasta Carbonara"

5. **Назад на листа:**
   - Кликни "Back to Chef List"
   - Сега Gordon Ramsay има 1 dish(es) во статистиката
   - Gordon Ramsay е најпопуларен (⭐ MOST POPULAR)

6. **Додај уште јадења:**
   - Додај 2 јадења на Jamie Oliver
   - Додај 1 јадење на Marco Pierre White
   - Провери дека редоследот е:
     1. Jamie Oliver - 2 dish(es) ⭐
     2. Gordon Ramsay - 1 dish(es)
     3. Marco Pierre White - 1 dish(es)
     4. Останатите со 0 dish(es)

### Test Scenario 2: CRUD Operations (Lab 2)

1. **Отвори CRUD листа:**
   ```
   GET http://localhost:8080/dishes
   ```
   - Треба да видиш табела со 5 јадења
   - Секое јадење има ID, dishId, name, cuisine, preparationTime
   - Копчиња за Edit и Delete

2. **Додај ново јадење:**
   - Кликни "➕ Add New Dish"
   - URL: `GET /dishes/dish-form`
   - Внеси:
     - dishId: "6"
     - name: "Moussaka"
     - cuisine: "Greek"
     - preparationTime: 90
   - Кликни "Add Dish"
   - URL: `POST /dishes/add`
   - Треба да се вратиш на `/dishes` со 6 јадења

3. **Уреди јадење:**
   - Кликни "✏️ Edit" на Moussaka
   - URL: `GET /dishes/dish-form/6`
   - Промени preparationTime на 120
   - Кликни "Update Dish"
   - URL: `POST /dishes/edit/6`
   - Провери дека preparationTime е 120

4. **Избриши јадење:**
   - Кликни "🗑️ Delete" на Moussaka
   - Ќе се појави confirm dialog
   - Кликни "OK"
   - URL: `GET /dishes/delete/6`
   - Треба да се вратиш на `/dishes` со 5 јадења

5. **Провери интеграција со Chef:**
   - Оди на `/listChefs`
   - Избери готвач и јадење
   - Провери дека додавањето сè уште работи

### Test Scenario 3: Edge Cases

1. **Празна селекција на готвач:**
   - Оди на `/listChefs`
   - Не избирај готвач
   - Кликни "Submit"
   - HTML5 validation треба да прикаже грешка (required field)

2. **Празна селекција на јадење:**
   - Избери готвач
   - Не избирај јадење
   - Кликни "Add dish"
   - JavaScript alert: "Please select a dish before submitting!"

3. **Уредување на непостоечко јадење:**
   ```
   GET http://localhost:8080/dishes/dish-form/999
   ```
   - Треба redirect на `/dishes?error=DishNotFound`
   - Прикажува error message: "Dish not found"

4. **Бришење јадење:**
   - Избриши јадење што е додадено на готвач
   - Оди на `/chefDetails` за тој готвач
   - Јадењето е филтрирано (th:if="${dish != null}")

### Expected Results:

✅ **Lab 1 функционалности:**
- Листа на готвачи работи
- Сортирање по број на јадења работи
- Најпопуларен готвач е истакнат
- Статистика е точна
- Додавање јадење на готвач работи

✅ **Lab 2 функционалности:**
- Листа на јадења работи
- Додавање ново јадење работи
- Уредување јадење работи
- Бришење јадење работи
- Форми работат за додавање и уредување

✅ **Интеграција:**
- Chef и Dish модулите работат заедно
- Податоците се конзистентни
- Валидациите работат

---

## 🔍 Важни концепти за презентација

### 1. Dependency Injection
```java
@Controller
public class DishController {
    private final DishService dishService;

    // Constructor Injection
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }
}
```
- Spring автоматски креира инстанца на DishServiceImpl
- Controller зависи од интерфејс (DishService), не од имплементација
- Loose coupling → лесно за тестирање и замена

### 2. Repository Pattern
```java
public interface DishRepository {
    List<Dish> findAll();
    Dish save(Dish dish);
}
```
- Апстракција на data access логиката
- Може лесно да се замени (in-memory → database)
- Single Responsibility Principle

### 3. Service Layer Pattern
```java
@Service
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;

    @Override
    public Dish create(...) {
        Dish dish = new Dish(...);
        return dishRepository.save(dish);
    }
}
```
- Бизнис логика е одвоена од контролери
- Може да ги комбинира повеќе repositories
- Транзакциона логика

### 4. MVC Pattern
```
Model (Dish, Chef) ←→ Controller (DishController) ←→ View (Thymeleaf)
       ↑                      ↑
       └──── Service ─────────┘
```
- Separation of concerns
- Model: податоци
- View: презентација
- Controller: логика за request/response

### 5. Thymeleaf Template Engine
```html
<span th:text="${dish.name}">Default Text</span>
<a th:href="@{/dishes/edit/{id}(id=${dish.id})}">Edit</a>
<div th:if="${error}">Error!</div>
```
- Server-side rendering
- Natural templates (може да се отворат директно во browser)
- Spring интеграција

---

## 📊 Архитектурна дијаграма

```
┌─────────────────────────────────────────────────────────┐
│                      Browser (User)                     │
└────────────┬──────────────────────────────┬─────────────┘
             │                              │
             │ HTTP Request                 │ HTTP Response
             │                              │
┌────────────▼──────────────────────────────▼─────────────┐
│                    Web Layer                            │
│  ┌─────────────────────┐  ┌─────────────────────┐      │
│  │   ChefListServlet   │  │   DishController    │      │
│  │   DishServlet       │  │   (Spring MVC)      │      │
│  │   ChefDetailsServlet│  │                     │      │
│  └──────────┬──────────┘  └──────────┬──────────┘      │
└─────────────┼──────────────────────────┼────────────────┘
              │                          │
              │ Calls                    │ Calls
              │                          │
┌─────────────▼──────────────────────────▼────────────────┐
│                   Service Layer                         │
│  ┌─────────────────────┐  ┌─────────────────────┐      │
│  │  ChefServiceImpl    │  │  DishServiceImpl    │      │
│  │                     │  │                     │      │
│  └──────────┬──────────┘  └──────────┬──────────┘      │
└─────────────┼──────────────────────────┼────────────────┘
              │                          │
              │ Uses                     │ Uses
              │                          │
┌─────────────▼──────────────────────────▼────────────────┐
│                Repository Layer                         │
│  ┌─────────────────────────────────────────────┐       │
│  │  InMemoryChefRepository                     │       │
│  │  InMemoryDishRepository                     │       │
│  └──────────┬───────────────────────┬──────────┘       │
└─────────────┼───────────────────────┼──────────────────┘
              │                       │
              │ Access                │ Access
              │                       │
┌─────────────▼───────────────────────▼──────────────────┐
│                     Data Layer                          │
│  ┌─────────────────────────────────────────────┐       │
│  │  DataHolder                                 │       │
│  │  - static List<Chef> chefs                  │       │
│  │  - static List<Dish> dishes                 │       │
│  └─────────────────────────────────────────────┘       │
└─────────────────────────────────────────────────────────┘
```

---

## 🎓 Заклучок

Овој проект демонстрира:
- **Multi-tier architecture** со чиста separacija на слоеви
- **Dependency Injection** со Spring Boot
- **Repository Pattern** за data access
- **Service Layer Pattern** за бизнис логика
- **MVC Pattern** со Servlets и Spring MVC Controllers
- **Template Engine (Thymeleaf)** за server-side rendering
- **CRUD операции** со in-memory storage
- **RESTful routing** со Spring MVC
- **Валидација** (server-side и client-side)

Проектот е добра основа за проширување со:
- JPA/Hibernate за database persistence
- REST API endpoints
- Security (Spring Security)
- Unit и Integration тестови
- Frontend framework (React, Angular, Vue)

---

**Автор:** Spring Boot Lab Project
**Верзија:** 2.0 (Lab 1Б + Lab 2Б)
**Датум:** 2025
