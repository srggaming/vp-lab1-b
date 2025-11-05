package mk.ukim.finki.wp.lab.model; // Го дефинираме пакетот каде се наоѓа класата

// Импортираме Lombok анотации за автоматско генерирање на код
import lombok.AllArgsConstructor; // Генерира конструктор со сите полиња
import lombok.Data; // Генерира getters, setters, toString, equals, hashCode
import lombok.NoArgsConstructor; // Генерира празен конструктор

/**
 * Dish класа - претставува јадење во ресторанот
 * Оваа класа ги чува информациите за едно јадење
 */
@Data // Lombok анотација - автоматски генерира getters, setters, toString, equals и hashCode методи
@NoArgsConstructor // Lombok анотација - генерира празен конструктор Dish()
@AllArgsConstructor // Lombok анотација - генерира конструктор со сите полиња Dish(dishId, name, cuisine, preparationTime)
public class Dish {

    /**
     * dishId - уникатен идентификатор на јадењето
     * Овој ID е String (текст) и треба да биде уникатен за секое јадење
     */
    private String dishId;

    /**
     * name - име на јадењето
     * Пример: "Pasta Carbonara", "Beef Wellington"
     */
    private String name;

    /**
     * cuisine - тип на кујна/кухиња
     * Пример: "Italian", "British", "Indian"
     */
    private String cuisine;

    /**
     * preparationTime - време потребно за подготовка на јадењето во минути
     * Пример: 30, 45, 60
     */
    private int preparationTime;
}
