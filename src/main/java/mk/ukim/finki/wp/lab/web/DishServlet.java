package mk.ukim.finki.wp.lab.web; // Го дефинираме пакетот за web слојот

// Импортираме ги потребните класи за сервлети
import jakarta.servlet.ServletException; // Исклучок за грешки во сервлети
import jakarta.servlet.annotation.WebServlet; // Анотација за мапирање на сервлет
import jakarta.servlet.http.HttpServlet; // Базна класа за HTTP сервлети
import jakarta.servlet.http.HttpServletRequest; // Објект што ги содржи податоците од HTTP барањето
import jakarta.servlet.http.HttpServletResponse; // Објект за испраќање на HTTP одговор

// Импортираме ги нашите класи
import mk.ukim.finki.wp.lab.model.Chef; // Нашата Chef класа
import mk.ukim.finki.wp.lab.model.Dish; // Нашата Dish класа
import mk.ukim.finki.wp.lab.service.ChefService; // Service за готвачи
import mk.ukim.finki.wp.lab.service.DishService; // Service за јадења

import java.io.IOException; // Исклучок за I/O грешки
import java.io.PrintWriter; // Класа за запишување на текст во response
import java.util.List; // Интерфејс за листа

/**
 * DishServlet класа
 * Ова е вториот сервлет во flow-от на апликацијата
 * После што корисникот избере готвач, се прикажува оваа страница
 * Овде корисникот избира јадење за да го додаде во листата на избраниот готвач
 */
@WebServlet(name = "DishServlet", urlPatterns = "/dish")
// @WebServlet - анотација за регистрирање на сервлет
// urlPatterns = "/dish" - сервлетот е достапен на http://localhost:8080/dish
// Овој сервлет се повикува од формата во ChefListServlet (action="/dish")
public class DishServlet extends HttpServlet {
    // extends HttpServlet - ја наследуваме HttpServlet базната класа

    /**
     * Dependency на ChefService
     * Ни треба за да го најдеме избраниот готвач по ID
     */
    private final ChefService chefService;

    /**
     * Dependency на DishService
     * Ни треба за да ги добиеме сите достапни јадења
     */
    private final DishService dishService;

    /**
     * Конструктор со параметри
     * Spring автоматски ги инјектира двата service bean-ови
     * Ова е constructor injection - најдобрата практика за dependency injection
     *
     * @param chefService - service за готвачи (автоматски инјектиран)
     * @param dishService - service за јадења (автоматски инјектиран)
     */
    public DishServlet(ChefService chefService, DishService dishService) {
        // Ги иницијализираме dependency полињата
        this.chefService = chefService;
        this.dishService = dishService;
    }

    /**
     * doPost() метод
     * Се извршува кога корисникот испрати POST барање на /dish
     * POST барања се користат за испраќање на податоци (формуларски податоци)
     * Формата од ChefListServlet користи method="POST", затоа се повикува doPost()
     *
     * @param req - HttpServletRequest објект што ги содржи податоците од барањето
     * @param resp - HttpServletResponse објект за испраќање на одговор
     * @throws ServletException - се фрла ако има проблем во сервлетот
     * @throws IOException - се фрла ако има проблем со I/O операции
     */
    @Override // Override-уваме метод од HttpServlet класата
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Чекор 1: Го читаме избраното chefId од формата
        // req.getParameter("chefId") - го добива вредноста на полето со име "chefId"
        // Враќа String (затоа што сите HTTP параметри се string-ови)
        String chefIdParam = req.getParameter("chefId");

        // Чекор 2: Го конвертираме String во Long
        // Long.parseLong() - конвертира String во Long број
        // Пример: "1" -> 1L
        Long chefId = Long.parseLong(chefIdParam);

        // Чекор 3: Го наоѓаме избраниот готвач од service
        // chefService.findById(chefId) - го враќа готвачот со соодветното ID
        Chef selectedChef = this.chefService.findById(chefId);

        // Чекор 4: Ги добиваме сите достапни јадења
        // dishService.listDishes() - враќа List<Dish> со сите јадења
        List<Dish> dishes = this.dishService.listDishes();

        // Чекор 5: Го поставуваме content type на одговорот
        // "text/html; charset=UTF-8" - HTML со UTF-8 енкодинг
        resp.setContentType("text/html; charset=UTF-8");

        // Чекор 6: Го добиваме PrintWriter објектот за запишување на одговор
        PrintWriter out = resp.getWriter();

        // Чекор 7: Пишуваме HTML код
        // Почеток на HTML документот
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset=\"utf-8\">");
        out.println("    <title>Add Dish to Chef</title>");

        // CSS стилови
        out.println("    <style type=\"text/css\">");
        out.println("        body {");
        out.println("            width: 800px;");
        out.println("            margin: auto;");
        out.println("            font-family: Arial, sans-serif;");
        out.println("        }");
        out.println("        table {");
        out.println("            width: 100%;");
        out.println("            margin-top: 20px;");
        out.println("            border-collapse: collapse;"); // Без празно место меѓу border-ите
        out.println("        }");
        out.println("        table, td, th {");
        out.println("            border: 1px solid black;"); // Црна граница
        out.println("            padding: 10px;");
        out.println("        }");
        out.println("        th {");
        out.println("            background-color: #4CAF50;"); // Зелена позадина за header
        out.println("            color: white;"); // Бел текст
        out.println("        }");
        out.println("        section {");
        out.println("            float: left;"); // Лево порамнување
        out.println("            margin: 0 1.5%;");
        out.println("            width: 63%;");
        out.println("        }");
        out.println("        aside {");
        out.println("            float: right;"); // Десно порамнување
        out.println("            margin: 0 1.5%;");
        out.println("            width: 30%;");
        out.println("        }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");

        // Header
        out.println("    <header>");
        out.println("        <h1>Select the Dish to add to the Chef</h1>");
        out.println("    </header>");

        // Section - лева страна со јадењата
        out.println("    <section>");
        out.println("        <h2>Select dish:</h2>");

        // Формата за избор на јадење
        // action="/chefDetails" - испраќа до ChefDetailsServlet
        // method="POST" - користиме POST метод
        out.println("        <form action=\"/chefDetails\" method=\"POST\">");

        // Hidden input - го испраќа chefId до следниот сервлет
        // type="hidden" - не се гледа на страницата, но се испраќа со формата
        // Ова е важно бидејќи следниот сервлет треба да знае кој готвач е избран
        out.println("            <input type=\"hidden\" name=\"chefId\" value=\"" + chefId + "\">");

        // Динамичко креирање на radio buttons за секое јадење
        for (Dish dish : dishes) {
            // for (Dish dish : dishes) - итерираме низ листата на јадења
            // За секое јадење креираме radio button

            out.println("            <input type=\"radio\" " +
                       "name=\"dishId\" " + // Името на полето
                       "value=\"" + dish.getDishId() + "\"> " + // Вредноста е dishId
                       // Текстот што се прикажува:
                       dish.getName() + " (" + dish.getCuisine() + ", " +
                       dish.getPreparationTime() + " min)" +
                       "<br/>");
            // Пример: Pasta Carbonara (Italian, 30 min)
        }

        // Submit копче
        out.println("            <br/>");
        out.println("            <input type=\"submit\" value=\"Add dish\">");
        out.println("        </form>");
        out.println("    </section>");

        // Aside - десна страна со информации за готвачот
        out.println("    <aside>");
        out.println("        <table>");
        out.println("            <tr>");
        out.println("                <td><b>Chef ID</b></td>");
        // Прикажуваме го ID-то на избраниот готвач
        out.println("                <td>" + selectedChef.getId() + "</td>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td><b>Chef Name</b></td>");
        // Прикажуваме го целото име на готвачот (име + презиме)
        out.println("                <td>" + selectedChef.getFirstName() + " " +
                   selectedChef.getLastName() + "</td>");
        out.println("            </tr>");
        out.println("        </table>");
        out.println("    </aside>");

        out.println("</body>");
        out.println("</html>");
    }
}
