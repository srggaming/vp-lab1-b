package mk.ukim.finki.wp.lab; // Го дефинираме пакетот - root package на апликацијата

// Импортираме ги потребните Spring Boot класи
import org.springframework.boot.SpringApplication; // Главната класа за стартување на Spring Boot апликација
import org.springframework.boot.autoconfigure.SpringBootApplication; // Главна Spring Boot анотација
import org.springframework.boot.web.servlet.ServletComponentScan; // Анотација за скенирање на сервлети

/**
 * LabApplication класа
 * Ова е главната класа на Spring Boot апликацијата
 * Од оваа класа се стартува целата апликација
 * Оваа класа е entry point - точката од која почнува извршувањето на програмата
 */
@SpringBootApplication
// @SpringBootApplication - Композитна анотација што ги комбинира:
// - @Configuration: Ја означува класата како извор на bean дефиниции
// - @EnableAutoConfiguration: Овозможува автоматска конфигурација на Spring
// - @ComponentScan: Автоматски ги скенира сите класи со @Component, @Service, @Repository, итн.
//   во овој пакет и сите под-пакети (mk.ukim.finki.wp.lab.*)

@ServletComponentScan
// @ServletComponentScan - ВАЖНА анотација за сервлети!
// Овозможува Spring Boot да ги скенира и регистрира класите означени со @WebServlet
// Без оваа анотација, нашите ChefListServlet, DishServlet и ChefDetailsServlet нема да работат!
// Spring Boot ќе ги најде сите класи во пакетот mk.ukim.finki.wp.lab.web со @WebServlet
// и автоматски ќе ги регистрира во embedded Tomcat серверот
public class LabApplication {

    /**
     * main метод
     * Ова е entry point на Java апликацијата
     * Кога ја стартуваме апликацијата, овој метод се извршува прв
     *
     * @param args - аргументи од command line (не ги користиме во овој случај)
     */
    public static void main(String[] args) {
        // SpringApplication.run() - го стартува Spring Boot апликацијата
        // Параметри:
        // 1. LabApplication.class - класата што ја користи Spring за конфигурација
        // 2. args - command line аргументи (се проследуваат до Spring)

        SpringApplication.run(LabApplication.class, args);

        // Што прави SpringApplication.run()?
        // 1. Го креира Spring ApplicationContext (контејнер за bean-ови)
        // 2. Ги скенира сите класи за @Component, @Service, @Repository, итн.
        // 3. Ги креира bean-ови (инстанци) од тие класи
        // 4. Ги решава dependency-јата (dependency injection)
        // 5. Го стартува embedded Tomcat серверот (на port 8080 по default)
        // 6. Ги регистрира сервлетите (заради @ServletComponentScan)
        // 7. Апликацијата е подготвена за прием на HTTP барања

        // Кога апликацијата ќе стартува, ќе видите log порака:
        // "Tomcat started on port(s): 8080"
        // Тогаш можете да пристапите на http://localhost:8080/listChefs
    }
}
