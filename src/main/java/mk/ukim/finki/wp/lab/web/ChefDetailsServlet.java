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

import java.io.IOException; // Исклучок за I/O грешки
import java.io.PrintWriter; // Класа за запишување на текст во response

/**
 * ChefDetailsServlet класа
 * Ова е третиот и финален сервлет во flow-от на апликацијата
 * Процес:
 * 1. Прима chefId и dishId од формата на DishServlet
 * 2. Го додава избраното јадење во листата на готвачот
 * 3. Ги прикажува сите детали за готвачот и неговите јадења
 */
@WebServlet(name = "ChefDetailsServlet", urlPatterns = "/chefDetails")
// @WebServlet - анотација за регистрирање на сервлет
// urlPatterns = "/chefDetails" - сервлетот е достапен на http://localhost:8080/chefDetails
// Овој сервлет се повикува од формата во DishServlet (action="/chefDetails")
public class ChefDetailsServlet extends HttpServlet {
    // extends HttpServlet - ја наследуваме HttpServlet базната класа

    /**
     * Dependency на ChefService
     * Ни треба за:
     * - Да го најдеме готвачот по ID
     * - Да го додадеме јадењето во листата на готвачот
     */
    private final ChefService chefService;

    /**
     * Конструктор со параметри
     * Spring автоматски го инјектира ChefService bean-от
     *
     * @param chefService - service за готвачи (автоматски инјектиран од Spring)
     */
    public ChefDetailsServlet(ChefService chefService) {
        // Го иницијализираме dependency полето
        this.chefService = chefService;
    }

    /**
     * doPost() метод
     * Се извршува кога корисникот испрати POST барање на /chefDetails
     * Формата од DishServlet користи method="POST", затоа се повикува doPost()
     *
     * @param req - HttpServletRequest објект што ги содржи податоците од барањето
     * @param resp - HttpServletResponse објект за испраќање на одговор
     * @throws ServletException - се фрла ако има проблем во сервлетот
     * @throws IOException - се фрла ако има проблем со I/O операции
     */
    @Override // Override-уваме метод од HttpServlet класата
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Чекор 1: Ги читаме податоците од формата
        // req.getParameter() - ја добива вредноста на полето од формата

        // Го читаме chefId (hidden input од претходната форма)
        String chefIdParam = req.getParameter("chefId");
        // Го конвертираме String во Long
        Long chefId = Long.parseLong(chefIdParam);

        // Го читаме dishId (од избраниот radio button)
        String dishId = req.getParameter("dishId");

        // Чекор 2: Го додаваме јадењето во листата на готвачот
        // chefService.addDishToChef() - ја извршува деловната логика:
        // - Го наоѓа готвачот по chefId
        // - Го наоѓа јадењето по dishId
        // - Го додава јадењето во chef.dishes листата
        // - Го зачувува ажурираниот готвач
        // - Го враќа ажурираниот готвач
        Chef chef = this.chefService.addDishToChef(chefId, dishId);
        // Сега chef објектот содржи ново јадење во неговата dishes листа

        // Чекор 3: Го поставуваме content type на одговорот
        resp.setContentType("text/html; charset=UTF-8");

        // Чекор 4: Го добиваме PrintWriter објектот за запишување на одговор
        PrintWriter out = resp.getWriter();

        // Чекор 5: Пишуваме HTML код за прикажување на деталите
        // Почеток на HTML документот
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset=\"utf-8\">");
        out.println("    <title>Chef Details</title>");

        // CSS стилови
        out.println("    <style type=\"text/css\">");
        out.println("        body {");
        out.println("            width: 800px;");
        out.println("            margin: auto;"); // Центрирање
        out.println("            font-family: Arial, sans-serif;");
        out.println("        }");
        out.println("        h1 {");
        out.println("            color: #333;"); // Темна боја
        out.println("        }");
        out.println("        ul {");
        out.println("            list-style-type: none;"); // Без bullets
        out.println("            padding: 0;");
        out.println("        }");
        out.println("        li {");
        out.println("            padding: 5px;");
        out.println("            margin: 5px 0;");
        out.println("            background-color: #f5f5f5;"); // Светло сива позадина
        out.println("            border-radius: 3px;"); // Заоблени агли
        out.println("        }");
        out.println("        .back-link {");
        out.println("            display: inline-block;");
        out.println("            margin-top: 20px;");
        out.println("            padding: 10px 15px;");
        out.println("            background-color: #4CAF50;"); // Зелена боја
        out.println("            color: white;");
        out.println("            text-decoration: none;"); // Без подвлекување
        out.println("            border-radius: 3px;");
        out.println("        }");
        out.println("        .back-link:hover {");
        out.println("            background-color: #45a049;"); // Потемна зелена кога е hover
        out.println("        }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");

        // Header со целото име на готвачот
        out.println("    <header>");
        out.println("        <h1>Chef: " + chef.getFirstName() + " " + chef.getLastName() + "</h1>");
        out.println("    </header>");

        // Section со детали
        out.println("    <section>");
        // Биографија на готвачот
        out.println("        <h2>Bio: " + chef.getBio() + "</h2>");

        // Наслов за листата на јадења
        out.println("        <h2>Dishes prepared by this chef:</h2>");

        // Проверка дали готвачот има јадења
        if (chef.getDishes() == null || chef.getDishes().isEmpty()) {
            // Ако нема јадења, прикажи порака
            out.println("        <p>This chef has no dishes yet.</p>");
        } else {
            // Ако има јадења, прикажи ги во листа
            out.println("        <ul>");

            // Итерираме низ листата на јадења на готвачот
            for (Dish dish : chef.getDishes()) {
                // for (Dish dish : chef.getDishes()) - за секое јадење во листата

                // Креираме list item за секое јадење
                out.println("            <li>");
                // Прикажуваме: Име (Кујна, Време за подготовка min)
                // Пример: Beef Wellington (British, 45 min)
                out.println("                " + dish.getName() +
                           " (" + dish.getCuisine() + ", " +
                           dish.getPreparationTime() + " min)");
                out.println("            </li>");
            }

            out.println("        </ul>");
        }

        // Link за враќање назад на почетната страница
        out.println("        <a href=\"/listChefs\" class=\"back-link\">Back to Chef List</a>");
        out.println("    </section>");

        out.println("</body>");
        out.println("</html>");

        // Важно: PrintWriter автоматски се затвора, не е потребно експлицитно close()
    }

    /**
     * doGet() метод
     * Овозможува пристап до овој сервлет и преку GET барање
     * Ако некој директно оди на /chefDetails URL без да поминал низ формата,
     * или ако сака да refresh-не страница, ќе добие информативна порака
     *
     * @param req - HttpServletRequest објект
     * @param resp - HttpServletResponse објект
     * @throws ServletException - исклучок за грешки
     * @throws IOException - исклучок за I/O грешки
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Го поставуваме content type
        resp.setContentType("text/html; charset=UTF-8");

        // Го добиваме PrintWriter
        PrintWriter out = resp.getWriter();

        // Прикажуваме информативна порака
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset=\"utf-8\">");
        out.println("    <title>Chef Details</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <h1>Chef Details</h1>");
        out.println("    <p>Please select a chef and dish from the main page.</p>");
        out.println("    <a href=\"/listChefs\">Go to Chef List</a>");
        out.println("</body>");
        out.println("</html>");
    }
}
