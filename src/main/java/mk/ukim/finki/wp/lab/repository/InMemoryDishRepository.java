package mk.ukim.finki.wp.lab.repository; // Го дефинираме пакетот

// Импортираме ги потребните класи
import mk.ukim.finki.wp.lab.bootstrap.DataHolder; // За пристап до статичките листи
import mk.ukim.finki.wp.lab.model.Dish; // Нашата Dish класа
import org.springframework.stereotype.Repository; // Spring анотација

import java.util.List; // Интерфејс за листа

/**
 * InMemoryDishRepository класа
 * Конкретна имплементација на DishRepository интерфејсот
 * Податоците се чуваат во меморијата (DataHolder листата)
 */
@Repository // Spring анотација - ја регистрира класата како Repository bean во Spring контејнерот
            // Spring автоматски креира инстанца од оваа класа и ја прави достапна за инјектирање
public class InMemoryDishRepository implements DishRepository {
    // implements DishRepository - оваа класа ја имплементира договореноста од DishRepository интерфејсот

    /**
     * Имплементација на findAll() методот
     * Ја враќа листата на јадења од DataHolder
     *
     * @return листа на сите јадења
     */
    @Override // Означува дека го override-уваме методот од интерфејсот
    public List<Dish> findAll() {
        // DataHolder.dishes е статичката листа иницијализирана во DataHolder класата
        // Едноставно ја враќаме целата листа
        return DataHolder.dishes;
    }

    /**
     * Имплементација на findByDishId(String dishId) методот
     * Го бара јадењето со соодветното dishId во листата
     *
     * @param dishId - идентификатор на јадењето
     * @return јадењето ако е пронајдено, инаку null
     */
    @Override
    public Dish findByDishId(String dishId) {
        // DataHolder.dishes.stream() - го претвораме листата во stream (поток на податоци)
        return DataHolder.dishes.stream()
                // .filter() - го филтрира stream-от според условот
                // Условот: dish -> dish.getDishId().equals(dishId)
                // dish - е секое јадење од листата (lambda параметар)
                // -> - lambda оператор (стрелка)
                // dish.getDishId() - го добиваме dishId-то на тековното јадење
                // .equals(dishId) - го споредуваме со бараното dishId
                // Користиме .equals() наместо == за споредба на String објекти
                .filter(dish -> dish.getDishId().equals(dishId))
                // .findFirst() - го враќа првиот елемент што го задоволува условот
                // Враќа Optional<Dish> - може да содржи јадење или да биде празен
                .findFirst()
                // .orElse(null) - ако Optional е празен (не е пронајдено јадењето), врати null
                // Ова го "распакува" Optional-от и враќа или Dish објект или null
                .orElse(null);
    }
}
