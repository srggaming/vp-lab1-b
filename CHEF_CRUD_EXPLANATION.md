# CRUD Функционалност за Chef - Детално објаснување

## 📋 Што додадов?

Имплементирав **комплетна CRUD функционалност за Chef (готвачи)** користејќи **Spring MVC Controllers**, исто како што е направено за Dish.

---

## 🎯 Што е CRUD?

**CRUD** е акроним за:
- **C**reate - Креирање нови записи
- **R**ead - Читање/преглед на записи
- **U**pdate - Ажурирање постоечки записи
- **D**elete - Бришење записи

---

## 📁 Кои файлови ги додадов/ажурирав?

### 1. Repository Layer (Слој за податоци)

#### ChefRepository.java (додаден метод)
```java
void deleteById(Long id);
```

#### InMemoryChefRepository.java (имплементација)
```java
@Override
public void deleteById(Long id) {
    DataHolder.chefs.removeIf(chef -> chef.getId().equals(id));
}
```

**Објаснување:**
- `deleteById()` - брише готвач од in-memory листата
- Користи `removeIf()` за да ги филтрира сите готвачи со даден ID

---

### 2. Service Layer (Бизнис логика)

#### ChefService.java (додадени методи)
```java
Chef create(String firstName, String lastName, String bio);
Chef update(Long id, String firstName, String lastName, String bio);
void delete(Long id);
```

#### ChefServiceImpl.java (имплементација)

##### Метод 1: create() - Креирање нов готвач
```java
@Override
public Chef create(String firstName, String lastName, String bio) {
    // 1. Најди најголем ID од сите готвачи и додади 1
    Long newId = this.chefRepository.findAll().stream()
            .mapToLong(Chef::getId)
            .max()
            .orElse(0L) + 1;

    // 2. Креирај нов Chef објект со генериран ID
    Chef chef = new Chef(newId, firstName, lastName, bio);

    // 3. Зачувај го во repository
    return this.chefRepository.save(chef);
}
```

**Објаснување:**
1. **Генерирање ID**: Го наоѓа најголемиот ID во листата и додава 1
   - `stream()` - креира stream од сите готвачи
   - `mapToLong(Chef::getId)` - ги мапира готвачите во нивните ID-ја
   - `max()` - го наоѓа најголемиот ID
   - `orElse(0L)` - ако нема готвачи, врати 0
   - `+ 1` - додади 1 за да добиеш нов уникатен ID

2. **Креирање објект**: Креира нов Chef со генериран ID и внесените податоци

3. **Зачувување**: Го чува во repository (додава во in-memory листата)

##### Метод 2: update() - Ажурирање готвач
```java
@Override
public Chef update(Long id, String firstName, String lastName, String bio) {
    // 1. Најди го постоечкиот готвач (или фрли exception ако не постои)
    Chef chef = this.findById(id);

    // 2. Ажурирај ги податоците
    chef.setFirstName(firstName);
    chef.setLastName(lastName);
    chef.setBio(bio);

    // 3. Зачувај ги промените
    return this.chefRepository.save(chef);
}
```

**Објаснување:**
1. **Наоѓање**: Го наоѓа готвачот со даден ID
   - Ако не постои, `findById()` фрла `RuntimeException`

2. **Ажурирање**: Ги менува вредностите на полињата
   - Се користат setter методи од Lombok

3. **Зачувување**: `save()` методот го брише стариот и го додава ажурираниот

##### Метод 3: delete() - Бришење готвач
```java
@Override
public void delete(Long id) {
    this.chefRepository.deleteById(id);
}
```

**Објаснување:**
- Едноставно повикување на repository метод за бришење
- Service layer може да додаде дополнителна логика (пр. логирање, валидација)

---

### 3. Web Layer - Controller (Spring MVC)

#### ChefController.java (НОВ ФАЙЛ)

Ова е **Spring MVC Controller** - замена за Servlet со поедноставен синтакс.

```java
@Controller
@RequestMapping("/chefs")
public class ChefController {
    private final ChefService chefService;

    public ChefController(ChefService chefService) {
        this.chefService = chefService;
    }

    // Методи...
}
```

**Анотации:**
- `@Controller` - го означува класата како Spring MVC Controller
- `@RequestMapping("/chefs")` - сите рути во овој controller започнуваат со `/chefs`

---

#### Метод 1: Листа на готвачи (Read)

```java
@GetMapping
public String getChefsPage(@RequestParam(required = false) String error, Model model) {
    // 1. Земи ги сите готвачи од service
    List<Chef> chefs = this.chefService.listChefs();

    // 2. Додади ги во model (за да се пратат до view)
    model.addAttribute("chefs", chefs);
    model.addAttribute("error", error);

    // 3. Врати име на view (Thymeleaf template)
    return "listChefsManagement";
}
```

**Што прави:**
- **URL:** `GET /chefs`
- **Објаснување:**
  1. Повикува `chefService.listChefs()` за да ги земе сите готвачи
  2. Ги става во `Model` објект - ова е како контекст во Servlet
  3. Враќа `"listChefsManagement"` - Spring автоматски го наоѓа `listChefsManagement.html`

**Model во Spring MVC:**
- Замена за `WebContext` во Servlet
- `model.addAttribute("key", value)` е еквивалент на `context.setVariable("key", value)`

---

#### Метод 2: Форма за додавање нов готвач (Create - GET)

```java
@GetMapping("/chef-form")
public String getAddChefPage(Model model) {
    // Постави chef на null за да знаеме дека е "додавање" а не "уредување"
    model.addAttribute("chef", null);
    return "chef-form";
}
```

**Што прави:**
- **URL:** `GET /chefs/chef-form`
- **Објаснување:**
  - Прикажува празна форма за додавање нов готвач
  - `chef` е `null` - ова во template го користиме да проверим дали е "Add" или "Edit" режим

---

#### Метод 3: Форма за уредување готвач (Update - GET)

```java
@GetMapping("/chef-form/{id}")
public String getEditChefForm(@PathVariable Long id, Model model) {
    try {
        // 1. Земи го готвачот по ID
        Chef chef = this.chefService.findById(id);

        // 2. Додади го во model
        model.addAttribute("chef", chef);

        // 3. Врати иста форма како за додавање
        return "chef-form";
    } catch (RuntimeException e) {
        // Ако не постои - редирект со error
        return "redirect:/chefs?error=ChefNotFound";
    }
}
```

**Што прави:**
- **URL:** `GET /chefs/chef-form/{id}` (пример: `/chefs/chef-form/3`)
- **Објаснување:**
  1. `@PathVariable Long id` - го земаме ID од URL-то
  2. Го наоѓаме готвачот со тој ID
  3. Ако постои - прикажуваме форма со пополнети полиња
  4. Ако не постои - редирект кон листа со error параметар

**@PathVariable vs @RequestParam:**
- `@PathVariable` - дел од URL патеката (`/chefs/chef-form/3` → id=3)
- `@RequestParam` - query параметар (`/chefs?error=test` → error="test")

---

#### Метод 4: Зачувување нов готвач (Create - POST)

```java
@PostMapping("/add")
public String saveChef(@RequestParam String firstName,
                       @RequestParam String lastName,
                       @RequestParam String bio) {
    // 1. Повикај create метод од service
    this.chefService.create(firstName, lastName, bio);

    // 2. Редирект кон листа
    return "redirect:/chefs";
}
```

**Што прави:**
- **URL:** `POST /chefs/add`
- **Објаснување:**
  1. `@RequestParam` - ги зема параметрите од POST формата
  2. Повикува `chefService.create()` кој креира нов готвач
  3. `redirect:/chefs` - redirect кон GET /chefs за да се прикаже ажурираната листа

**Зошто redirect?**
- **PRG Pattern** (Post-Redirect-Get):
  - После POST барање (креирање/ажурирање), прави redirect кон GET
  - Ако корисникот кликне F5 (refresh), нема да се испрати повторно POST
  - Спречува дупликатни записи

---

#### Метод 5: Ажурирање готвач (Update - POST)

```java
@PostMapping("/edit/{id}")
public String editChef(@PathVariable Long id,
                       @RequestParam String firstName,
                       @RequestParam String lastName,
                       @RequestParam String bio) {
    // 1. Повикај update метод од service
    this.chefService.update(id, firstName, lastName, bio);

    // 2. Редирект кон листа
    return "redirect:/chefs";
}
```

**Што прави:**
- **URL:** `POST /chefs/edit/{id}` (пример: `/chefs/edit/3`)
- **Објаснување:**
  1. Го зема ID од URL (`@PathVariable`)
  2. Ги зема новите вредности од формата (`@RequestParam`)
  3. Повикува `chefService.update()` за ажурирање
  4. Redirect кон листа

---

#### Метод 6: Бришење готвач (Delete - GET)

```java
@GetMapping("/delete/{id}")
public String deleteChef(@PathVariable Long id) {
    // 1. Избриши го готвачот
    this.chefService.delete(id);

    // 2. Редирект кон листа
    return "redirect:/chefs";
}
```

**Што прави:**
- **URL:** `GET /chefs/delete/{id}` (пример: `/chefs/delete/3`)
- **Објаснување:**
  1. Го зема ID од URL
  2. Повикува `chefService.delete(id)`
  3. Redirect кон листа

**Забележка:** Правилно би било `@DeleteMapping`, но бидејќи HTML формите поддржуваат само GET/POST, користиме GET за едноставност.

---

### 4. View Layer - HTML Templates

#### chef-form.html (НОВ ФАЙЛ)

Ова е **унифицирана форма** за додавање И уредување.

```html
<h1 th:text="${chef != null ? 'Edit Chef' : 'Add New Chef'}">Chef Form</h1>

<form th:action="${chef != null ? '/chefs/edit/' + chef.id : '/chefs/add'}"
      method="POST">

    <!-- First Name поле -->
    <input type="text" name="firstName"
           th:value="${chef != null ? chef.firstName : ''}" required>

    <!-- Last Name поле -->
    <input type="text" name="lastName"
           th:value="${chef != null ? chef.lastName : ''}" required>

    <!-- Bio поле (textarea) -->
    <textarea name="bio" required
              th:text="${chef != null ? chef.bio : ''}"></textarea>

    <!-- Submit копче -->
    <button type="submit" th:text="${chef != null ? 'Update Chef' : 'Add Chef'}">
        Submit
    </button>

    <a th:href="@{/chefs}">Cancel</a>
</form>
```

**Како работи:**

1. **Условен текст:**
   ```html
   th:text="${chef != null ? 'Edit Chef' : 'Add New Chef'}"
   ```
   - Ако `chef` не е null → "Edit Chef"
   - Ако `chef` е null → "Add New Chef"

2. **Условна action:**
   ```html
   th:action="${chef != null ? '/chefs/edit/' + chef.id : '/chefs/add'}"
   ```
   - Ако уредување → POST /chefs/edit/3
   - Ако додавање → POST /chefs/add

3. **Пополнување на полиња:**
   ```html
   th:value="${chef != null ? chef.firstName : ''}"
   ```
   - Ако уредување → прикажи постоечка вредност
   - Ако додавање → празно поле

**Предности на оваа форма:**
- **DRY Principle** (Don't Repeat Yourself) - една форма за двете операции
- Помалку код за одржување
- Конзистентен UI

---

#### listChefsManagement.html (НОВ ФАЙЛ)

Ова е **CRUD листа на готвачи** - различна од `listChefs.html` (која се користи за Lab 1).

```html
<h1>Chefs Management</h1>

<!-- Копче за додавање нов готвач -->
<a th:href="@{/chefs/chef-form}">➕ Add New Chef</a>

<!-- Табела со готвачи -->
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Biography</th>
            <th>Number of Dishes</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <!-- За секој готвач во листата -->
        <tr th:each="chef : ${chefs}">
            <td th:text="${chef.id}">1</td>
            <td th:text="${chef.firstName}">Gordon</td>
            <td th:text="${chef.lastName}">Ramsay</td>
            <td th:text="${chef.bio}">Biography...</td>
            <td th:text="${chef.dishes.size()}">0</td>
            <td>
                <!-- Edit копче -->
                <a th:href="@{/chefs/chef-form/{id}(id=${chef.id})}">✏️ Edit</a>

                <!-- Delete копче со confirm -->
                <a th:href="@{/chefs/delete/{id}(id=${chef.id})}"
                   onclick="return confirm('Are you sure?')">🗑️ Delete</a>
            </td>
        </tr>
    </tbody>
</table>
```

**Клучни елементи:**

1. **Thymeleaf URL изрази:**
   ```html
   th:href="@{/chefs/chef-form/{id}(id=${chef.id})}"
   ```
   - `@{...}` - Thymeleaf URL израз
   - `{id}` - placeholder во URL
   - `(id=${chef.id})` - ја заменува `{id}` со вредност од `chef.id`
   - Резултат: `/chefs/chef-form/3`

2. **JavaScript confirm:**
   ```html
   onclick="return confirm('Are you sure?')"
   ```
   - Прикажува confirm dialog пред бришење
   - Ако корисникот кликне "Cancel" - не се извршува бришењето

3. **Приказ на број на јадења:**
   ```html
   th:text="${chef.dishes.size()}"
   ```
   - Го повикува `getDishes().size()` методот
   - Прикажува колку јадења има готвачот

---

## 🔄 Целосен Flow на CRUD операциите

### 1. CREATE (Додавање нов готвач)

```
Корисник → GET /chefs/chef-form
    ↓
ChefController.getAddChefPage()
    - model.addAttribute("chef", null)
    ↓
chef-form.html (празна форма)
    ↓
Корисник внесува: firstName="Jamie", lastName="Oliver", bio="..."
    ↓
POST /chefs/add
    ↓
ChefController.saveChef(firstName, lastName, bio)
    ↓
chefService.create("Jamie", "Oliver", "...")
    ↓
ChefServiceImpl.create()
    1. Генерира ID (пр. 6)
    2. Креира: new Chef(6L, "Jamie", "Oliver", "...")
    3. Зачувува: chefRepository.save(chef)
    ↓
InMemoryChefRepository.save(chef)
    1. Проверува дали постои (не постои за нов)
    2. Додава во DataHolder.chefs листа
    ↓
redirect:/chefs
    ↓
GET /chefs → Прикажува ажурирана листа со новиот готвач
```

---

### 2. READ (Преглед на листа)

```
Корисник → GET /chefs
    ↓
ChefController.getChefsPage(error, model)
    ↓
chefService.listChefs()
    ↓
chefRepository.findAll()
    → Враќа DataHolder.chefs (листа на сите готвачи)
    ↓
model.addAttribute("chefs", chefs)
    ↓
listChefsManagement.html
    - th:each="chef : ${chefs}" → Итерира низ листата
    - Прикажува табела со сите готвачи
```

---

### 3. UPDATE (Ажурирање готвач)

```
Корисник → Кликнува "Edit" на готвач со ID=3
    ↓
GET /chefs/chef-form/3
    ↓
ChefController.getEditChefForm(id=3, model)
    ↓
chefService.findById(3L)
    ↓
chefRepository.findById(3L)
    → Враќа Optional<Chef> со готвач ID=3
    ↓
model.addAttribute("chef", foundChef)
    ↓
chef-form.html (пополнета форма)
    - th:value="${chef.firstName}" → Прикажува "Marco"
    - th:action="/chefs/edit/3"
    ↓
Корисник менува firstName="Marco Pierre" и кликнува "Update"
    ↓
POST /chefs/edit/3
    ↓
ChefController.editChef(id=3, firstName="Marco Pierre", lastName=..., bio=...)
    ↓
chefService.update(3L, "Marco Pierre", "White", "...")
    ↓
ChefServiceImpl.update()
    1. findById(3L) → Наоѓа готвач
    2. chef.setFirstName("Marco Pierre")
    3. chef.setLastName("White")
    4. chef.setBio("...")
    5. chefRepository.save(chef)
    ↓
InMemoryChefRepository.save(chef)
    1. findById(3L) → Го наоѓа постоечкиот
    2. Го брише стариот: DataHolder.chefs.remove(existingChef)
    3. Го додава ажурираниот: DataHolder.chefs.add(chef)
    ↓
redirect:/chefs → Прикажува листа со ажуриран готвач
```

---

### 4. DELETE (Бришење готвач)

```
Корисник → Кликнува "Delete" на готвач со ID=5
    ↓
JavaScript confirm dialog: "Are you sure?"
    → Корисникот кликнува "OK"
    ↓
GET /chefs/delete/5
    ↓
ChefController.deleteChef(id=5)
    ↓
chefService.delete(5L)
    ↓
chefRepository.deleteById(5L)
    ↓
InMemoryChefRepository.deleteById(5L)
    → DataHolder.chefs.removeIf(chef -> chef.getId().equals(5L))
    ↓
redirect:/chefs → Прикажува листа без избришаниот готвач
```

---

## 🆚 Разлики: Servlet vs Spring MVC Controller

| Аспект | **Servlet** (Lab 1) | **Spring MVC Controller** (Lab 2) |
|--------|---------------------|-----------------------------------|
| **Анотација** | `@WebServlet(urlPatterns="/...")` | `@Controller` + `@RequestMapping` |
| **HTTP Метод** | `doGet()`, `doPost()` | `@GetMapping`, `@PostMapping` |
| **Request параметри** | `req.getParameter("name")` | `@RequestParam String name` |
| **URL параметри** | Мануелно парсирање | `@PathVariable Long id` |
| **Проследување податоци** | `WebContext` + `context.setVariable()` | `Model` + `model.addAttribute()` |
| **Rendering** | `templateEngine.process("view", context, writer)` | `return "viewName";` |
| **Redirect** | `resp.sendRedirect("/path")` | `return "redirect:/path";` |
| **Dependency Injection** | Constructor injection (мануелен) | Автоматски од Spring |
| **Complexity** | Повеќе boilerplate код | Помалку код, попросто |

### Пример споредба:

**Servlet (Lab 1):**
```java
@WebServlet(urlPatterns = "/listChefs")
public class ChefListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<Chef> chefs = chefService.listChefs();
        resp.setContentType("text/html; charset=UTF-8");

        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);
        context.setVariable("chefs", chefs);

        templateEngine.process("listChefs", context, resp.getWriter());
    }
}
```

**Spring MVC Controller (Lab 2):**
```java
@Controller
@RequestMapping("/chefs")
public class ChefController {
    @GetMapping
    public String getChefsPage(Model model) {
        List<Chef> chefs = chefService.listChefs();
        model.addAttribute("chefs", chefs);
        return "listChefsManagement";
    }
}
```

**Забележи:**
- Controller има ~5 линии наспроти ~15 линии за Servlet
- Controller е почист и полесен за читање
- Spring автоматски ги менаџира request/response објектите

---

## 📊 Архитектура на CRUD flow

```
         Browser (Корисник)
              ↓
   ┌──────────────────────────┐
   │   HTTP Request           │
   │   GET /chefs             │
   │   POST /chefs/add        │
   │   etc.                   │
   └──────────┬───────────────┘
              ↓
   ┌──────────────────────────┐
   │   ChefController         │
   │   @Controller            │
   │   - getChefsPage()       │
   │   - saveChef()           │
   │   - editChef()           │
   │   - deleteChef()         │
   └──────────┬───────────────┘
              ↓
   ┌──────────────────────────┐
   │   ChefService            │
   │   - listChefs()          │
   │   - create()             │
   │   - update()             │
   │   - delete()             │
   └──────────┬───────────────┘
              ↓
   ┌──────────────────────────┐
   │   ChefRepository         │
   │   - findAll()            │
   │   - save()               │
   │   - deleteById()         │
   └──────────┬───────────────┘
              ↓
   ┌──────────────────────────┐
   │   DataHolder             │
   │   static List<Chef>      │
   └──────────────────────────┘
```

---

## 🧪 Како да тестираш

### 1. Отвори CRUD страна:
```
http://localhost:8080/chefs
```

### 2. Додај нов готвач:
- Кликни "➕ Add New Chef"
- Внеси: firstName="Nikola", lastName="Trajkovski", bio="Macedonian chef..."
- Кликни "Add Chef"
- Провери дека се појавува во листата

### 3. Уреди готвач:
- Кликни "✏️ Edit" на Nikola Trajkovski
- Промени bio="Updated bio..."
- Кликни "Update Chef"
- Провери дека промените се зачувани

### 4. Избриши готвач:
- Кликни "🗑️ Delete" на Nikola Trajkovski
- Потврди во confirm dialog
- Провери дека е избришан од листата

---

## 🎯 Резиме: Што научи?

1. **CRUD Pattern** - Create, Read, Update, Delete операции
2. **Spring MVC Controllers** - замена за Servlets
3. **@GetMapping/@PostMapping** - HTTP метод mapping
4. **@PathVariable** - земање параметри од URL
5. **@RequestParam** - земање параметри од форма/query string
6. **Model** - проследување податоци до view
7. **Redirect** - PRG pattern за спречување дупликати
8. **Unified Forms** - една форма за Create и Update
9. **Repository Pattern** - апстракција на data access
10. **Service Layer** - бизнис логика одвоена од web layer

---

## 📖 Споредба со Dish CRUD

| Аспект | **Dish CRUD** | **Chef CRUD** |
|--------|---------------|---------------|
| **Controller** | `DishController` | `ChefController` |
| **Base URL** | `/dishes` | `/chefs` |
| **Form View** | `dish-form.html` | `chef-form.html` |
| **List View** | `listDishes.html` | `listChefsManagement.html` |
| **Полиња** | dishId, name, cuisine, preparationTime | firstName, lastName, bio |
| **ID Generation** | Static counter во Dish класа | Max ID + 1 во service |
| **Дополнително** | - | Број на јадења во листата |

---

## 🎓 За презентација

Кога презентираш, објасни:

1. **Зошто CRUD?**
   - Секоја апликација треба да менаџира податоци
   - CRUD е основен pattern за data management

2. **Зошто Spring MVC наместо Servlets?**
   - Помалку boilerplate код
   - Полесно за читање и одржување
   - Автоматски routing и parameter binding

3. **Како работи Controller?**
   - Прикажи аннотациите (`@Controller`, `@GetMapping`, итн.)
   - Објасни како се мапира URL на метод
   - Покажи како се проследуваат податоци до view

4. **Multi-tier Architecture:**
   - Controller → Service → Repository → Data
   - Секој слој има своја одговорност
   - Loose coupling преку интерфејси

5. **Демонстрирај:**
   - Отвори `/chefs` во browser
   - Додај нов готвач
   - Уреди постоечки
   - Избриши готвач
   - Покажи како работи интеграцијата со Lab 1

---

Успех со презентацијата! 🎉
