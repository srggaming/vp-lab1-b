package mk.ukim.finki.wp.lab.repository; // Го дефинираме пакетот

// Импортираме ја нашата Dish класа
import mk.ukim.finki.wp.lab.model.Dish;

import java.util.List; // Интерфејс за листа

/**
 * DishRepository интерфејс
 * Repository слојот за работа со јадења
 * Дефинира операции за пристап до податоците за јадења
 *
 * Забелешка: За разлика од ChefRepository, овде нема save() метод
 * Јадењата се само за читање (read-only) во оваа вежба
 */
public interface DishRepository {

    /**
     * findAll() - Ги враќа сите јадења
     * Не прима параметри
     * Враќа List<Dish> - листа со сите јадења достапни во системот
     *
     * @return листа на сите јадења
     */
    List<Dish> findAll();

    /**
     * findByDishId(String dishId) - Го наоѓа јадењето по неговото ID
     * Прима String dishId како параметар
     * Враќа Dish објект или null ако јадењето не постои
     *
     * Забелешка: Овде не користиме Optional (за разлика од ChefRepository)
     * Во реална апликација, подобро е да се користи Optional за consistency
     *
     * @param dishId - идентификатор на јадењето што го бараме
     * @return јадењето ако постои, инаку null
     */
    Dish findByDishId(String dishId);
}
