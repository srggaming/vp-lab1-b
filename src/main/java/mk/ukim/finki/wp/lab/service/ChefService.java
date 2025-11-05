package mk.ukim.finki.wp.lab.service; // Го дефинираме пакетот

// Импортираме ја нашата Chef класа
import mk.ukim.finki.wp.lab.model.Chef;

import java.util.List; // Интерфејс за листа

/**
 * ChefService интерфејс
 * Service слојот е деловната логика на апликацијата (Business Logic Layer)
 * Се наоѓа помеѓу Web слојот (контролери/сервлети) и Repository слојот (пристап до податоци)
 *
 * Зошто Service слој?
 * - Ја содржи деловната логика (бизнис правила)
 * - Ги координира повеќе repository-ја ако е потребно
 * - Web слојот не комуницира директно со Repository, туку преку Service
 * - Полесно тестирање и одржување на кодот
 */
public interface ChefService {

    /**
     * listChefs() - Ги враќа сите готвачи
     * Слично на findAll() од repository, но може да содржи дополнителна логика
     * (на пример, филтрирање, сортирање, итн.)
     *
     * @return листа на сите готвачи
     */
    List<Chef> listChefs();

    /**
     * findById(Long id) - Го наоѓа готвачот по ID
     * Прима id како параметар
     * Враќа Chef објект (не Optional, за разлика од repository)
     *
     * @param id - идентификатор на готвачот
     * @return готвачот со соодветното ID
     */
    Chef findById(Long id);

    /**
     * addDishToChef(Long chefId, String dishId) - Додава јадење во листата на готвачот
     * Ова е деловна логика специфична за оваа апликација
     * Процес:
     * 1. Го наоѓа готвачот по chefId
     * 2. Го наоѓа јадењето по dishId
     * 3. Го додава јадењето во листата на јадења на готвачот
     * 4. Го зачувува ажурираниот готвач
     *
     * @param chefId - идентификатор на готвачот
     * @param dishId - идентификатор на јадењето
     * @return ажурираниот готвач со новото јадење
     */
    Chef addDishToChef(Long chefId, String dishId);
}
