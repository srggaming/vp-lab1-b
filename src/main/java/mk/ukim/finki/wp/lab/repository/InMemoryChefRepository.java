package mk.ukim.finki.wp.lab.repository; // Го дефинираме пакетот

// Импортираме ги потребните класи
import mk.ukim.finki.wp.lab.bootstrap.DataHolder; // За пристап до статичките листи
import mk.ukim.finki.wp.lab.model.Chef; // Нашата Chef класа
import org.springframework.stereotype.Repository; // Spring анотација за repository компонента

import java.util.List; // Интерфејс за листа
import java.util.Optional; // Wrapper за опционални вредности

/**
 * InMemoryChefRepository класа
 * Конкретна имплементација на ChefRepository интерфејсот
 * "InMemory" значи дека податоците се чуваат во меморијата (RAM), не во база
 * Кога апликацијата се рестартира, промените се губат
 */
@Repository // Spring анотација - ја регистрира класата како Repository компонента во Spring контејнерот
            // Spring автоматски ќе креира инстанца (bean) од оваа класа
            // Оваа инстанца може да се инјектира во други класи преку Dependency Injection
public class InMemoryChefRepository implements ChefRepository {
    // implements ChefRepository - значи дека оваа класа ја имплементира договореноста од интерфејсот
    // Мора да ги имплементира сите методи дефинирани во интерфејсот

    /**
     * Имплементација на findAll() методот
     * Ја враќа листата на готвачи од DataHolder
     *
     * @return листа на сите готвачи
     */
    @Override // Анотација што укажува дека го override-уваме методот од интерфејсот
              // Ова е безбедносна проверка - ако методот не постои во интерфејсот, ќе добиеме грешка
    public List<Chef> findAll() {
        // DataHolder.chefs е статичката листа што ја иницијализиравме во DataHolder класата
        // Едноставно ја враќаме без никакви промени
        return DataHolder.chefs;
    }

    /**
     * Имплементација на findById(Long id) методот
     * Го бара готвачот со соодветното ID во листата
     *
     * @param id - идентификатор на готвачот
     * @return Optional со готвачот (ако е пронајден) или празен Optional (ако не е пронајден)
     */
    @Override
    public Optional<Chef> findById(Long id) {
        // DataHolder.chefs.stream() - го претвораме листата во stream (поток на податоци)
        // Stream API овозможува функционално програмирање во Java
        return DataHolder.chefs.stream()
                // .filter() - го филтрира stream-от според условот
                // Условот: chef -> chef.getId().equals(id)
                // chef - е секој елемент од листата (lambda параметар)
                // -> - lambda оператор (arrow operator)
                // chef.getId().equals(id) - проверуваме дали ID-то на готвачот е еднакво на бараното ID
                // .equals() го користиме наместо == за споредба на објекти (бидејќи Long е објект, не примитив)
                .filter(chef -> chef.getId().equals(id))
                // .findFirst() - го враќа првиот елемент што го задоволува условот
                // Враќа Optional<Chef> - ако има резултат, Optional го содржи, ако нема - Optional е празен
                .findFirst();
    }

    /**
     * Имплементација на save(Chef chef) методот
     * Го зачувува или ажурира готвачот во листата
     *
     * @param chef - готвачот за зачувување/ажурирање
     * @return зачуваниот готвач
     */
    @Override
    public Chef save(Chef chef) {
        // Прво, го бараме готвачот со исто ID во листата
        // findById() враќа Optional<Chef>
        Optional<Chef> existingChef = this.findById(chef.getId());

        // Ако готвачот постои, го бришеме од листата
        // .ifPresent() е метод на Optional - се извршува ако Optional содржи вредност
        existingChef.ifPresent(c -> DataHolder.chefs.remove(c));

        // Го додаваме (новиот или ажурираниот) готвач во листата
        DataHolder.chefs.add(chef);

        // Го враќаме зачуваниот готвач
        return chef;
    }
}
