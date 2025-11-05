package mk.ukim.finki.wp.lab.web; // Го дефинираме пакетот за web слојот

// Импортираме ги потребните класи за сервлети
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Импортираме ги нашите класи
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.service.ChefService;

// Импортираме Thymeleaf класи
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

/**
 * ChefDetailsServlet класа
 * Ова е третиот и финален сервлет во flow-от на апликацијата
 * Процес:
 * 1. Прима chefId и dishId од формата на DishServlet
 * 2. Го додава избраното јадење во листата на готвачот
 * 3. Ги прикажува сите детали за готвачот и неговите јадења
 *
 * Ажурирано да користи Thymeleaf template (chefDetails.html) наместо PrintWriter
 */
@WebServlet(name = "ChefDetailsServlet", urlPatterns = "/chefDetails")
public class ChefDetailsServlet extends HttpServlet {

    /**
     * Dependency на ChefService
     * Ни треба за:
     * - Да го најдеме готвачот по ID
     * - Да го додадеме јадењето во листата на готвачот
     */
    private final ChefService chefService;

    /**
     * Dependency на SpringTemplateEngine
     * Ни треба за да ги render-ираме Thymeleaf template фајловите
     */
    private final SpringTemplateEngine templateEngine;

    /**
     * Конструктор со параметри
     * Spring автоматски ги инјектира двата bean-ови
     *
     * @param chefService - service за готвачи (автоматски инјектиран од Spring)
     * @param templateEngine - Thymeleaf engine (автоматски инјектиран од Spring)
     */
    public ChefDetailsServlet(ChefService chefService, SpringTemplateEngine templateEngine) {
        this.chefService = chefService;
        this.templateEngine = templateEngine;
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
    @Override
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

        // Чекор 4: Креираме Thymeleaf WebContext
        // IWebExchange - Thymeleaf wrapper за request/response
        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext()) // Го креира Thymeleaf application од servlet контекстот
                .buildExchange(req, resp); // Го креира web exchange од request и response

        // WebContext - контекст за Thymeleaf template rendering
        WebContext context = new WebContext(webExchange);

        // Чекор 5: Ги додаваме променливите во контекстот
        // "chef" - ажурираниот готвач со новото јадење (${chef} во template-от)
        context.setVariable("chef", chef);

        // Чекор 6: Го render-ираме template-от
        // templateEngine.process() - го процесира Thymeleaf template-от
        // "chefDetails" - името на template фајлот (chefDetails.html)
        // context - WebContext со променливите (chef)
        // resp.getWriter() - Writer за испишување на резултатот
        templateEngine.process("chefDetails", context, resp.getWriter());

        // Процесот:
        // 1. Thymeleaf го чита chefDetails.html фајлот
        // 2. Го наоѓа th:text="'Chef: ' + ${chef.firstName} + ' ' + ${chef.lastName}"
        //    и го поставува насловот на целото име на готвачот
        // 3. Го наоѓа th:text="'Bio: ' + ${chef.bio}" и ја прикажува биографијата
        // 4. Проверува дали ${chef.dishes} е празна листа
        // 5. Ако не е празна, го наоѓа th:each="dish : ${chef.dishes}"
        //    и креира <li> елемент за секое јадење
        // 6. За секое јадење, ги заменува ${dish.name}, ${dish.cuisine}, итн.
        //    со вистинските вредности
        // 7. Го испраќа финалниот HTML до клиентот
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

        // Креираме едноставен HTML одговор (без template)
        // Користиме getWriter() за испишување на текст
        resp.getWriter().println("<!DOCTYPE html>");
        resp.getWriter().println("<html>");
        resp.getWriter().println("<head>");
        resp.getWriter().println("    <meta charset=\"utf-8\">");
        resp.getWriter().println("    <title>Chef Details</title>");
        resp.getWriter().println("</head>");
        resp.getWriter().println("<body>");
        resp.getWriter().println("    <h1>Chef Details</h1>");
        resp.getWriter().println("    <p>Please select a chef and dish from the main page.</p>");
        resp.getWriter().println("    <a href=\"/listChefs\">Go to Chef List</a>");
        resp.getWriter().println("</body>");
        resp.getWriter().println("</html>");
    }
}
