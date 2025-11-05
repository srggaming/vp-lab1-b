package mk.ukim.finki.wp.lab.model; // Го дефинираме пакетот каде се наоѓа класата

// Импортираме потребните класи
import lombok.Data; // Lombok анотација за автоматско генерирање на getters/setters
import lombok.NoArgsConstructor; // Lombok анотација за празен конструктор

import java.util.ArrayList; // Класа за динамична листа
import java.util.List; // Интерфејс за листа

/**
 * Chef класа - претставува готвач во ресторанот
 * Оваа класа ги чува информациите за еден готвач
 * Секој готвач може да подготвува повеќе јадења (релација 1-to-many)
 */
@Data // Lombok анотација - автоматски генерира getters, setters, toString, equals и hashCode методи
@NoArgsConstructor // Lombok анотација - генерира празен конструктор Chef()
public class Chef {

    /**
     * id - уникатен идентификатор на готвачот
     * Long е wrapper класа за long (може да биде null, за разлика од примитивниот тип long)
     */
    private Long id;

    /**
     * firstName - име на готвачот
     * Пример: "Gordon", "Jamie", "Marco"
     */
    private String firstName;

    /**
     * lastName - презиме на готвачот
     * Пример: "Ramsay", "Oliver", "Pierre White"
     */
    private String lastName;

    /**
     * bio - биографија на готвачот (кратко описание)
     * Пример: "World-renowned chef with 16 Michelin stars"
     */
    private String bio;

    /**
     * dishes - листа од јадења што ги подготвува овој готвач
     * Иницијализирана со празна ArrayList за да избегнеме NullPointerException
     * List е интерфејс, ArrayList е конкретна имплементација
     */
    private List<Dish> dishes = new ArrayList<>();

    /**
     * Конструктор со параметри (без листата на јадења)
     * Овој конструктор го користиме кога иницијално креираме готвач без јадења
     * Листата dishes автоматски ќе биде иницијализирана како празна ArrayList
     *
     * @param id - идентификатор на готвачот
     * @param firstName - име на готвачот
     * @param lastName - презиме на готвачот
     * @param bio - биографија на готвачот
     */
    public Chef(Long id, String firstName, String lastName, String bio) {
        this.id = id; // this.id е полето на класата, id е параметарот
        this.firstName = firstName; // Го поставуваме името
        this.lastName = lastName; // Го поставуваме презимето
        this.bio = bio; // Ја поставуваме биографијата
        this.dishes = new ArrayList<>(); // Иницијализираме празна листа на јадења
    }

    /**
     * Целосен конструктор со сите полиња (вклучувајќи ја и листата на јадења)
     * Го користиме кога сакаме да креираме готвач со веќе постоечка листа на јадења
     *
     * @param id - идентификатор на готвачот
     * @param firstName - име на готвачот
     * @param lastName - презиме на готвачот
     * @param bio - биографија на готвачот
     * @param dishes - листа на јадења што ги подготвува готвачот
     */
    public Chef(Long id, String firstName, String lastName, String bio, List<Dish> dishes) {
        this.id = id; // Го поставуваме ID-то
        this.firstName = firstName; // Го поставуваме името
        this.lastName = lastName; // Го поставуваме презимето
        this.bio = bio; // Ја поставуваме биографијата
        this.dishes = dishes != null ? dishes : new ArrayList<>(); // Ако dishes е null, креираме нова празна листа
        // ? : е тернарен оператор (condition ? valueIfTrue : valueIfFalse)
    }
}
