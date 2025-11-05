package mk.ukim.finki.wp.lab.web; // Го дефинираме пакетот за web слојот

// Импортираме ги потребните класи за сервлети
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Импортираме ги нашите класи
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.service.ChefService;
import mk.ukim.finki.wp.lab.service.DishService;

// Импортираме Thymeleaf класи
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.List;

/**
 * DishServlet класа
 * Ова е вториот сервлет во flow-от на апликацијата
 * После што корисникот избере готвач, се прикажува оваа страница
 * Овде корисникот избира јадење за да го додаде во листата на избраниот готвач
 *
 * Ажурирано да користи Thymeleaf template (dishesList.html) наместо PrintWriter
 */
@WebServlet(name = "DishServlet", urlPatterns = "/dish")
public class DishServlet extends HttpServlet {

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
     * Dependency на SpringTemplateEngine
     * Ни треба за да ги render-ираме Thymeleaf template фајловите
     */
    private final SpringTemplateEngine templateEngine;

    /**
     * Конструктор со параметри
     * Spring автоматски ги инјектира трите bean-ови
     *
     * @param chefService - service за готвачи (автоматски инјектиран)
     * @param dishService - service за јадења (автоматски инјектиран)
     * @param templateEngine - Thymeleaf engine (автоматски инјектиран)
     */
    public DishServlet(ChefService chefService, DishService dishService, SpringTemplateEngine templateEngine) {
        this.chefService = chefService;
        this.dishService = dishService;
        this.templateEngine = templateEngine;
    }

    /**
     * doPost() метод
     * Се извршува кога корисникот испрати POST барање на /dish
     * Формата од ChefListServlet користи method="POST", затоа се повикува doPost()
     *
     * @param req - HttpServletRequest објект што ги содржи податоците од барањето
     * @param resp - HttpServletResponse објект за испраќање на одговор
     * @throws ServletException - се фрла ако има проблем во сервлетот
     * @throws IOException - се фрла ако има проблем со I/O операции
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Чекор 1: Го читаме избраното chefId од формата
        // req.getParameter("chefId") - го добива вредноста на полето со име "chefId"
        String chefIdParam = req.getParameter("chefId");

        // Чекор 2: Го конвертираме String во Long
        // Long.parseLong() - конвертира String во Long број
        Long chefId = Long.parseLong(chefIdParam);

        // Чекор 3: Го наоѓаме избраниот готвач од service
        // chefService.findById(chefId) - го враќа готвачот со соодветното ID
        Chef selectedChef = this.chefService.findById(chefId);

        // Чекор 4: Ги добиваме сите достапни јадења
        // dishService.listDishes() - враќа List<Dish> со сите јадења
        List<Dish> dishes = this.dishService.listDishes();

        // Чекор 5: Го поставуваме content type на одговорот
        resp.setContentType("text/html; charset=UTF-8");

        // Чекор 6: Креираме Thymeleaf WebContext
        // IWebExchange - Thymeleaf wrapper за request/response
        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext()) // Го креира Thymeleaf application од servlet контекстот
                .buildExchange(req, resp); // Го креира web exchange од request и response

        // WebContext - контекст за Thymeleaf template rendering
        WebContext context = new WebContext(webExchange);

        // Чекор 7: Ги додаваме променливите во контекстот
        // Овие променливи ќе бидат достапни во dishesList.html template-от

        // "selectedChef" - избраниот готвач (${selectedChef} во template-от)
        context.setVariable("selectedChef", selectedChef);

        // "dishes" - листата на сите јадења (${dishes} во template-от)
        context.setVariable("dishes", dishes);

        // Чекор 8: Го render-ираме template-от
        // templateEngine.process() - го процесира Thymeleaf template-от
        // "dishesList" - името на template фајлот (dishesList.html)
        // context - WebContext со променливите (selectedChef, dishes)
        // resp.getWriter() - Writer за испишување на резултатот
        templateEngine.process("dishesList", context, resp.getWriter());

        // Процесот:
        // 1. Thymeleaf го чита dishesList.html фајлот
        // 2. Го наоѓа <input type="hidden" th:value="${selectedChef.id}">
        //    и го поставува value на ID-то на избраниот готвач
        // 3. Го наоѓа th:each="dish : ${dishes}" и креира radio button за секое јадење
        // 4. Ги заменува ${dish.name}, ${dish.cuisine}, итн. со вистински вредности
        // 5. Го наоѓа ${selectedChef.id} и ${selectedChef.firstName} во таблицата
        //    и ги заменува со вистинските вредности
        // 6. Го испраќа финалниот HTML до клиентот
    }
}
