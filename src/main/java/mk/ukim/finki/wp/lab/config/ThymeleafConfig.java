package mk.ukim.finki.wp.lab.config; // Го дефинираме пакетот за конфигурација

// Импортираме ги потребните Thymeleaf класи
import org.springframework.context.ApplicationContext; // Spring контекст
import org.springframework.context.annotation.Bean; // Анотација за дефинирање на bean
import org.springframework.context.annotation.Configuration; // Анотација за конфигурациска класа
import org.thymeleaf.spring6.SpringTemplateEngine; // Thymeleaf template engine
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver; // Resolver за наоѓање на template-и
import org.thymeleaf.templatemode.TemplateMode; // Режим на template (HTML, XML, итн.)

/**
 * ThymeleafConfig класа
 * Конфигурациска класа за Thymeleaf template engine
 * Овде го конфигурираме како Thymeleaf ќе ги наоѓа и process-ира HTML template фајловите
 */
@Configuration // Оваа анотација кажува на Spring дека ова е конфигурациска класа
               // Spring ќе ги извика методите означени со @Bean и ќе ги регистрира резултатите како bean-ови
public class ThymeleafConfig {

    /**
     * ApplicationContext dependency
     * ApplicationContext е Spring контејнерот што ги содржи сите bean-ови
     * Ни треба за да креираме SpringResourceTemplateResolver
     */
    private final ApplicationContext applicationContext;

    /**
     * Конструктор со параметри
     * Spring автоматски го инјектира ApplicationContext bean-от
     *
     * @param applicationContext - Spring контекст (автоматски инјектиран)
     */
    public ThymeleafConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * templateResolver() - креира bean за наоѓање на Thymeleaf template фајлови
     * Template Resolver е одговорен за локација и читање на template фајловите
     *
     * @return SpringResourceTemplateResolver конфигуриран за наша апликација
     */
    @Bean // Оваа анотација кажува на Spring да го регистрира резултатот како bean
          // Други bean-ови можат да го инјектираат овој templateResolver
    public SpringResourceTemplateResolver templateResolver() {
        // Креираме нова инстанца на SpringResourceTemplateResolver
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();

        // Го поставуваме ApplicationContext
        // Template resolver го користи за да ги најде фајловите преку Spring's resource loading
        templateResolver.setApplicationContext(this.applicationContext);

        // Го поставуваме prefix-от (префиксот) за патеката до template фајловите
        // "classpath:/templates/" значи:
        // - classpath: - во src/main/resources/ фолдерот (тоа е на classpath)
        // - /templates/ - во templates/ под-фолдерот
        // Значи, template фајловите се во: src/main/resources/templates/
        templateResolver.setPrefix("classpath:/templates/");

        // Го поставуваме suffix-от (суфиксот) за template фајловите
        // ".html" значи дека сите template фајлови завршуваат со .html
        // Пример: ако бараме "listChefs", ќе го најде "listChefs.html"
        templateResolver.setSuffix(".html");

        // Го поставуваме режимот на template-от
        // TemplateMode.HTML значи дека template-ите се HTML5 фајлови
        // Други опции: XML, TEXT, JAVASCRIPT, CSS, итн.
        templateResolver.setTemplateMode(TemplateMode.HTML);

        // Го поставуваме character encoding-от
        // "UTF-8" е важно за поддршка на кирилични карактери
        templateResolver.setCharacterEncoding("UTF-8");

        // Cacheable = false значи дека template-ите НЕ се кешираат
        // Ова е корисно за развој - промените во HTML фајловите веднаш се гледаат
        // Во продакција би било true за подобри перформанси
        templateResolver.setCacheable(false);

        // Го враќаме конфигурираниот template resolver
        return templateResolver;
    }

    /**
     * templateEngine() - креира bean за Thymeleaf template engine
     * Template Engine е главниот компонент што ги process-ира template фајловите
     * Го зема template фајлот, ги заменува th:* атрибутите со вистински податоци, и враќа HTML
     *
     * @return SpringTemplateEngine конфигуриран со наш templateResolver
     */
    @Bean // Spring го регистрира ова како bean
    public SpringTemplateEngine templateEngine() {
        // Креираме нова инстанца на SpringTemplateEngine
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        // Го поставуваме template resolver-ot
        // templateResolver() методот погоре го креира resolver bean-от
        // Template engine го користи resolver-от за да ги најде template фајловите
        templateEngine.setTemplateResolver(templateResolver());

        // Enableспринг EL compiler = false
        // EL (Expression Language) компајлерот може да ги компајлира Thymeleaf изразите за подобри перформанси
        // false = не користи компајлер (подобро за развој и дебагирање)
        templateEngine.setEnableSpringELCompiler(false);

        // Го враќаме конфигурираниот template engine
        return templateEngine;
    }
}
