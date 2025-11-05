package mk.ukim.finki.wp.lab.service.impl; // Го дефинираме пакетот за имплементации

// Импортираме ги потребните класи
import mk.ukim.finki.wp.lab.model.Chef; // Нашата Chef класа
import mk.ukim.finki.wp.lab.model.Dish; // Нашата Dish класа
import mk.ukim.finki.wp.lab.repository.ChefRepository; // Repository за готвачи
import mk.ukim.finki.wp.lab.repository.DishRepository; // Repository за јадења
import mk.ukim.finki.wp.lab.service.ChefService; // Service интерфејсот што го имплементираме
import org.springframework.stereotype.Service; // Spring анотација за сервис

import java.util.List; // Интерфејс за листа

/**
 * ChefServiceImpl класа
 * Конкретна имплементација на ChefService интерфејсот
 * Содржи деловна логика за операции со готвачи
 */
@Service // Spring анотација - ја регистрира класата како Service bean во Spring контејнерот
         // Service beans содржат деловна логика на апликацијата
         // Spring автоматски креира инстанца и ја прави достапна за dependency injection
public class ChefServiceImpl implements ChefService {
    // implements ChefService - оваа класа ја имплементира договореноста од ChefService интерфејсот

    /**
     * Dependency на ChefRepository
     * private final - полето е приватно и не може да се промени по иницијализација (immutable)
     * final е добра практика за dependency-ја - не сакаме да семенуваат по креирањето
     */
    private final ChefRepository chefRepository;

    /**
     * Dependency на DishRepository
     * Ни треба за да можеме да ги најдеме јадењата кога додаваме јадење на готвач
     */
    private final DishRepository dishRepository;

    /**
     * Конструктор со параметри
     * Spring го користи овој конструктор за dependency injection
     * Кога Spring креира инстанца од ChefServiceImpl, автоматски ќе ги најде
     * и инјектира ChefRepository и DishRepository bean-овите
     *
     * Ова е Constructor Injection - препорачаниот начин за dependency injection
     * Предности:
     * - Полињата може да бидат final (immutable)
     * - Полесно тестирање (може да се инјектираат mock објекти)
     * - Експлицитно се гледаат сите зависности
     *
     * @param chefRepository - repository за готвачи (автоматски инјектиран од Spring)
     * @param dishRepository - repository за јадења (автоматски инјектиран од Spring)
     */
    public ChefServiceImpl(ChefRepository chefRepository, DishRepository dishRepository) {
        // this.chefRepository - полето на класата
        // chefRepository - параметарот од конструкторот
        this.chefRepository = chefRepository;
        this.dishRepository = dishRepository;
    }

    /**
     * Имплементација на listChefs() методот
     * Ги враќа сите готвачи од repository
     *
     * @return листа на сите готвачи
     */
    @Override // Означува дека го override-уваме методот од интерфејсот
    public List<Chef> listChefs() {
        // Едноставно го повикуваме findAll() методот од repository
        // Во поедноставени случаи, service може само да прослови барања до repository
        // Но во покомплексни случаи, тука би имале дополнителна деловна логика
        return this.chefRepository.findAll();
        // this.chefRepository - го користиме repository dependency-то што е инјектирано
    }

    /**
     * Имплементација на findById(Long id) методот
     * Го наоѓа готвачот по ID
     *
     * @param id - идентификатор на готвачот
     * @return готвачот со соодветното ID
     */
    @Override
    public Chef findById(Long id) {
        // chefRepository.findById(id) враќа Optional<Chef>
        // .orElseThrow() - ако Optional е празен (готвачот не е пронајден), фрли исклучок
        // () -> - lambda expression за креирање на исклучокот
        // new RuntimeException("Chef not found") - креираме нов RuntimeException со порака
        return this.chefRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chef not found with id: " + id));
        // Ако готвачот не постои, апликацијата ќе фрли грешка со јасна порака
    }

    /**
     * Имплементација на addDishToChef(Long chefId, String dishId) методот
     * Ова е клучниот метод што ја имплементира деловната логика
     * Процес: Наоѓање на готвач и јадење, додавање на јадење, зачувување
     *
     * @param chefId - идентификатор на готвачот
     * @param dishId - идентификатор на јадењето
     * @return ажурираниот готвач
     */
    @Override
    public Chef addDishToChef(Long chefId, String dishId) {
        // Чекор 1: Го наоѓаме готвачот по неговото ID
        // Користиме findById() метод што веќе го имплементиравме погоре
        Chef chef = this.findById(chefId);
        // Ако готвачот не постои, findById() ќе фрли RuntimeException

        // Чекор 2: Го наоѓаме јадењето по неговото dishId
        Dish dish = this.dishRepository.findByDishId(dishId);
        // Ако dish е null, продолжува (во продакција би требало да провериме за null)

        // Чекор 3: Го додаваме јадењето во листата на јадења на готвачот
        // chef.getDishes() - ја добиваме листата на јадења на готвачот
        // .add(dish) - го додаваме новото јадење во листата
        chef.getDishes().add(dish);

        // Чекор 4: Го зачувуваме ажурираниот готвач во repository
        // save() методот ќе го ажурира готвачот во DataHolder листата
        return this.chefRepository.save(chef);
        // Го враќаме ажурираниот готвач
    }
}
