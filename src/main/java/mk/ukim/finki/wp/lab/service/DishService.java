package mk.ukim.finki.wp.lab.service; // Го дефинираме пакетот

// Импортираме ја нашата Dish класа
import mk.ukim.finki.wp.lab.model.Dish;

import java.util.List; // Интерфејс за листа

/**
 * DishService интерфејс
 * Service слојот за работа со јадења
 * Содржи деловна логика за операции со јадења
 */
public interface DishService {

    /**
     * listDishes() - Ги враќа сите јадења
     * Може да содржи дополнителна логика (филтрирање, сортирање, итн.)
     *
     * @return листа на сите јадења
     */
    List<Dish> listDishes();

    /**
     * findByDishId(String dishId) - Го наоѓа јадењето по неговото ID
     * Прима dishId како параметар
     * Враќа Dish објект
     *
     * @param dishId - идентификатор на јадењето
     * @return јадењето со соодветното dishId
     */
    Dish findByDishId(String dishId);
}
