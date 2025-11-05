package mk.ukim.finki.wp.lab.service.impl; // Го дефинираме пакетот за имплементации

// Импортираме ги потребните класи
import mk.ukim.finki.wp.lab.model.Dish; // Нашата Dish класа
import mk.ukim.finki.wp.lab.repository.DishRepository; // Repository за јадења
import mk.ukim.finki.wp.lab.service.DishService; // Service интерфејсот што го имплементираме
import org.springframework.stereotype.Service; // Spring анотација за сервис

import java.util.List; // Интерфејс за листа

/**
 * DishServiceImpl класа
 * Конкретна имплементација на DishService интерфејсот
 * Содржи деловна логика за операции со јадења
 */
@Service // Spring анотација - ја регистрира класата како Service bean во Spring контејнерот
         // Spring автоматски креира инстанца и ја прави достапна за dependency injection
public class DishServiceImpl implements DishService {
    // implements DishService - оваа класа ја имплементира договореноста од DishService интерфејсот

    /**
     * Dependency на DishRepository
     * private final - полето е приватно и immutable (не може да се промени)
     * final е добра практика - не сакаме dependency-то да се менува
     */
    private final DishRepository dishRepository;

    /**
     * Конструктор со параметри
     * Spring го користи овој конструктор за dependency injection
     * Кога Spring креира DishServiceImpl bean, автоматски ќе го најде и инјектира
     * DishRepository bean-от (InMemoryDishRepository што го креиравме претходно)
     *
     * Ова е Constructor Injection - најдобриот начин за dependency injection
     *
     * @param dishRepository - repository за јадења (автоматски инјектиран од Spring)
     */
    public DishServiceImpl(DishRepository dishRepository) {
        // this.dishRepository - полето на класата
        // dishRepository - параметарот од конструкторот
        // Го доделуваме параметарот на полето
        this.dishRepository = dishRepository;
    }

    /**
     * Имплементација на listDishes() методот
     * Ги враќа сите јадења од repository
     *
     * @return листа на сите јадења
     */
    @Override // Означува дека го override-уваме методот од интерфејсот
    public List<Dish> listDishes() {
        // Едноставно го повикуваме findAll() методот од repository
        // this.dishRepository - го користиме инјектираното repository
        // .findAll() - методот што враќа листа на сите јадења
        return this.dishRepository.findAll();
    }

    /**
     * Имплементација на findByDishId(String dishId) методот
     * Го наоѓа јадењето по неговото ID
     *
     * @param dishId - идентификатор на јадењето
     * @return јадењето со соодветното dishId
     */
    @Override
    public Dish findByDishId(String dishId) {
        // Едноставно го повикуваме findByDishId() методот од repository
        // this.dishRepository - го користиме инјектираното repository
        // .findByDishId(dishId) - методот што го бара јадењето по ID
        return this.dishRepository.findByDishId(dishId);
        // Враќа Dish објект или null ако не постои
    }
}
