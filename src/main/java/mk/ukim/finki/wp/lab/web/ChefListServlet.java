package mk.ukim.finki.wp.lab.web; // Го дефинираме пакетот за web слојот

// Импортираме ги потребните класи за сервлети
import jakarta.servlet.ServletException; // Исклучок за грешки во сервлети
import jakarta.servlet.annotation.WebServlet; // Анотација за мапирање на сервлет
import jakarta.servlet.http.HttpServlet; // Базна класа за HTTP сервлети
import jakarta.servlet.http.HttpServletRequest; // Објект што ги содржи податоците од HTTP барањето
import jakarta.servlet.http.HttpServletResponse; // Објект за испраќање на HTTP одговор

// Импортираме ги нашите класи
import mk.ukim.finki.wp.lab.model.Chef; // Нашата Chef класа
import mk.ukim.finki.wp.lab.service.ChefService; // Service за работа со готвачи

// Импортираме Thymeleaf класи
import org.thymeleaf.context.WebContext; // Контекст за Thymeleaf со web податоци
import org.thymeleaf.spring6.SpringTemplateEngine; // Thymeleaf template engine
import org.thymeleaf.web.IWebExchange; // Интерфејс за web размена (request/response)
import org.thymeleaf.web.servlet.JakartaServletWebApplication; // Adapter за Jakarta Servlets

import java.io.IOException; // Исклучок за I/O грешки
import java.util.List; // Интерфејс за листа

/**
 * ChefListServlet класа
 * Сервлетот е одговорен за прикажување на листа на готвачи
 * Овој сервлет е првата страница што корисникот ја гледа
 * Корисникот избира готвач од листата
 *
 * Ажурирано да користи Thymeleaf template (listChefs.html) наместо PrintWriter
 */
@WebServlet(name = "ChefListServlet", urlPatterns = "/listChefs")
// @WebServlet - Spring Boot анотација за регистрирање на сервлет
// urlPatterns = "/listChefs" - URL патеката на која е достапен сервлетот
public class ChefListServlet extends HttpServlet {

    /**
     * Dependency на ChefService
     * Ни треба за да ги добиеме сите готвачи
     */
    private final ChefService chefService;

    /**
     * Dependency на SpringTemplateEngine
     * Ни треба за да ги render-ираме Thymeleaf template фајловите
     * Spring автоматски го инјектира bean-от што го креиравме во ThymeleafConfig
     */
    private final SpringTemplateEngine templateEngine;

    /**
     * Конструктор со параметри
     * Spring автоматски ги инјектира двата bean-ови (ChefService и SpringTemplateEngine)
     *
     * @param chefService - service за готвачи (автоматски инјектиран од Spring)
     * @param templateEngine - Thymeleaf engine (автоматски инјектиран од Spring)
     */
    public ChefListServlet(ChefService chefService, SpringTemplateEngine templateEngine) {
        this.chefService = chefService;
        this.templateEngine = templateEngine;
    }

    /**
     * doGet() метод
     * Се извршува кога корисникот испрати GET барање на /listChefs
     *
     * @param req - HttpServletRequest објект што ги содржи податоците од барањето
     * @param resp - HttpServletResponse објект за испраќање на одговор до клиентот
     * @throws ServletException - се фрла ако има проблем во сервлетот
     * @throws IOException - се фрла ако има проблем со I/O операции
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Чекор 1: Ги добиваме сите готвачи од service слојот
        // chefService.listChefs() - враќа List<Chef>
        List<Chef> chefs = this.chefService.listChefs();

        // Чекор 2: Го поставуваме content type на одговорот
        // "text/html; charset=UTF-8" - одговорот е HTML со UTF-8 енкодинг
        resp.setContentType("text/html; charset=UTF-8");

        // Чекор 3: Креираме Thymeleaf WebContext
        // WebContext го содржи контекстот за template rendering (променливи, request, response, итн.)

        // Прво, креираме IWebExchange од request и response
        // JakartaServletWebApplication е adapter што го претвора Jakarta Servlet API во Thymeleaf API
        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext()) // getServletContext() - го добива servlet контекстот
                .buildExchange(req, resp); // buildExchange() - го креира web exchange од request и response

        // Креираме WebContext од web exchange
        // WebContext е специјален Thymeleaf контекст за web апликации
        // Содржи информации за request, response, session, итн.
        WebContext context = new WebContext(webExchange);

        // Чекор 4: Ги додаваме променливите во контекстот
        // context.setVariable(name, value) - додава променлива што може да се користи во template-от
        // "chefs" - името на променливата (во template-от ќе биде ${chefs})
        // chefs - листата на готвачи што ја добивме од service-от
        context.setVariable("chefs", chefs);

        // Чекор 5: Го render-ираме template-от
        // templateEngine.process() - го процесира Thymeleaf template-от
        // Параметри:
        // 1. "listChefs" - името на template фајлот (без .html суфикс)
        //    Thymeleaf автоматски ќе го најде src/main/resources/templates/listChefs.html
        // 2. context - WebContext со променливите
        // 3. resp.getWriter() - Writer за испишување на резултатот (rendered HTML)
        templateEngine.process("listChefs", context, resp.getWriter());

        // Процесот:
        // 1. Thymeleaf го чита listChefs.html фајлот
        // 2. Го наоѓа th:each="chef : ${chefs}" атрибутот
        // 3. За секој chef од chefs листата, креира radio button
        // 4. Ги заменува ${chef.id}, ${chef.firstName}, итн. со вистински вредности
        // 5. Го испраќа финалниот HTML до клиентот преку resp.getWriter()
    }
}
