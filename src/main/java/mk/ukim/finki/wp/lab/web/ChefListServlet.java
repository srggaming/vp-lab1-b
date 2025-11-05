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

import java.io.IOException; // Исклучок за I/O грешки
import java.io.PrintWriter; // Класа за запишување на текст во response
import java.util.List; // Интерфејс за листа

/**
 * ChefListServlet класа
 * Сервлетот е одговорен за прикажување на листа на готвачи
 * Овој сервлет е првата страница што корисникот ја гледа
 * Корисникот избира готвач од листата
 */
@WebServlet(name = "ChefListServlet", urlPatterns = "/listChefs")
// @WebServlet - Spring Boot анотација за регистрирање на сервлет
// name = "ChefListServlet" - името на сервлетот (користено за internal reference)
// urlPatterns = "/listChefs" - URL патеката на која е достапен сервлетот
// Кога корисникот оди на http://localhost:8080/listChefs, овој сервлет се активира
public class ChefListServlet extends HttpServlet {
    // extends HttpServlet - ја наследуваме HttpServlet класата
    // Со тоа добиваме сè што е потребно за обработка на HTTP барања

    /**
     * Dependency на ChefService
     * Ни треба за да ги добиеме сите готвачи
     * private final - immutable dependency
     */
    private final ChefService chefService;

    /**
     * Конструктор со параметри
     * Spring автоматски го повикува овој конструктор и го инјектира ChefService bean-от
     * Ова е constructor injection - Spring го наоѓа ChefServiceImpl bean-от и го инјектира
     *
     * @param chefService - service за готвачи (автоматски инјектиран од Spring)
     */
    public ChefListServlet(ChefService chefService) {
        // this.chefService - полето на класата
        // chefService - параметарот од конструкторот
        this.chefService = chefService;
    }

    /**
     * doGet() метод
     * Се извршува кога корисникот испрати GET барање на /listChefs
     * GET барања се користат за читање на податоци (не менуваат ништо на серверот)
     * Пример: Кога корисникот пишува URL во browser или кликне на link
     *
     * @param req - HttpServletRequest објект што ги содржи податоците од барањето
     * @param resp - HttpServletResponse објект за испраќање на одговор до клиентот
     * @throws ServletException - се фрла ако има проблем во сервлетот
     * @throws IOException - се фрла ако има проблем со I/O операции
     */
    @Override // Override-уваме метод од HttpServlet класата
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Чекор 1: Ги добиваме сите готвачи од service слојот
        // chefService.listChefs() - враќа List<Chef>
        List<Chef> chefs = this.chefService.listChefs();

        // Чекор 2: Го поставуваме content type на одговорот
        // "text/html; charset=UTF-8" - одговорот е HTML со UTF-8 енкодинг
        // UTF-8 е важно за кирилични карактери (македонски текст)
        resp.setContentType("text/html; charset=UTF-8");

        // Чекор 3: Го добиваме PrintWriter објектот за запишување на одговор
        // PrintWriter ни овозможува да испраќаме текст (HTML) до клиентот
        PrintWriter out = resp.getWriter();

        // Чекор 4: Пишуваме HTML код
        // Почеток на HTML документот
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset=\"utf-8\">"); // За кирилични карактери
        out.println("    <title>Restaurant Chefs - Welcome to Our Restaurant</title>");

        // CSS стилови за изглед
        out.println("    <style type=\"text/css\">");
        out.println("        body {");
        out.println("            width: 800px;"); // Ширина на страницата
        out.println("            margin: auto;"); // Центрирање на страницата
        out.println("            font-family: Arial, sans-serif;"); // Фонт
        out.println("        }");
        out.println("        h1 {");
        out.println("            color: #333;"); // Боја на насловот
        out.println("        }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");

        // Header со наслов
        out.println("    <header>");
        out.println("        <h1>Welcome to Our Restaurant</h1>");
        out.println("        <p>Please select a chef from the list below:</p>");
        out.println("    </header>");

        // Main content
        out.println("    <main>");
        out.println("        <h2>Choose a chef:</h2>");

        // Формата за избор на готвач
        // action="/dish" - кога се submitува формата, се испраќа до /dish URL-то
        // method="POST" - користиме POST метод (за разлика од GET)
        out.println("        <form action=\"/dish\" method=\"POST\">");

        // Динамичко креирање на radio buttons за секој готвач
        // Итерираме низ листата на готвачи
        for (Chef chef : chefs) {
            // for (Chef chef : chefs) - enhanced for loop
            // Chef chef - променливата што го претставува тековниот готвач
            // : - separator
            // chefs - листата низ која итерираме

            // За секој готвач, креираме radio button
            out.println("            <input type=\"radio\" " +
                       "name=\"chefId\" " + // Името на полето (ќе се испрати до серверот)
                       "value=\"" + chef.getId() + "\"> " + // Вредноста е ID-то на готвачот
                       // Текстот што се прикажува до radio button:
                       "Name: " + chef.getFirstName() + " " + chef.getLastName() +
                       ", Bio: " + chef.getBio() +
                       "<br/>"); // Line break
        }

        // Submit копче
        out.println("            <br/>");
        out.println("            <input type=\"submit\" value=\"Submit\">");
        out.println("        </form>");
        out.println("    </main>");
        out.println("</body>");
        out.println("</html>");

        // Важно: PrintWriter автоматски се затвара, не е потребно експлицитно close()
    }
}
