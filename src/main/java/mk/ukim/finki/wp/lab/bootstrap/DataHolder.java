package mk.ukim.finki.wp.lab.bootstrap; // Го дефинираме пакетот

// Импортираме ги нашите model класи
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;

// Импортираме потребните Java класи за работа со листи
import java.util.ArrayList;
import java.util.List;

/**
 * DataHolder класа - чува статички податоци во меморија
 * Оваа класа е bootstrap класа - се извршува на почеток и ги иницијализира податоците
 * Податоците се чуваат во статички листи (static) - значи се споделени за целата апликација
 *
 * Во реални апликации, овие податоци би биле во база на податоци
 * Овде користиме in-memory листи за едноставност
 */
public class DataHolder {

    /**
     * Статична листа на готвачи
     * static - значи дека постои само една копија за целата апликација
     * public - достапна од секаде
     * List<Chef> - листа што содржи објекти од типот Chef
     */
    public static List<Chef> chefs = new ArrayList<>();

    /**
     * Статична листа на јадења
     * static - значи дека постои само една копија за целата апликација
     * public - достапна од секаде
     * List<Dish> - листа што содржи објекти од типот Dish
     */
    public static List<Dish> dishes = new ArrayList<>();

    /**
     * Статички блок - се извршува автоматски кога класата се вчитува во меморија
     * Овде ги иницијализираме почетните податоци
     * Се извршува само еднаш, пред сè друго
     */
    static {
        // ============================================
        // Иницијализација на јадења (5 различни јадења)
        // ============================================

        // Креираме 5 јадења со различни кујни и времиња за подготовка
        // new Dish(dishId, name, cuisine, preparationTime)

        dishes.add(new Dish(
                "1", // dishId - уникатен идентификатор
                "Pasta Carbonara", // name - име на јадењето
                "Italian", // cuisine - италијанска кујна
                30 // preparationTime - 30 минути за подготовка
        ));

        dishes.add(new Dish(
                "2",
                "Beef Wellington", // Класично британско јадење
                "British",
                45 // 45 минути за подготовка
        ));

        dishes.add(new Dish(
                "3",
                "Chicken Tikka Masala", // Популарно индиско јадење
                "Indian",
                40
        ));

        dishes.add(new Dish(
                "4",
                "Sushi Platter", // Јапонско јадење
                "Japanese",
                50
        ));

        dishes.add(new Dish(
                "5",
                "Coq au Vin", // Француско јадење (пилешко со вино)
                "French",
                60
        ));

        // ============================================
        // Иницијализација на готвачи (5 различни готвачи)
        // ============================================

        // Креираме 5 готвачи со нивните информации
        // new Chef(id, firstName, lastName, bio)

        chefs.add(new Chef(
                1L, // id - Long вредност (L на крајот значи Long, не int)
                "Gordon", // firstName - име
                "Ramsay", // lastName - презиме
                "World-renowned chef with 16 Michelin stars" // bio - биографија
        ));

        chefs.add(new Chef(
                2L,
                "Jamie",
                "Oliver",
                "British celebrity chef known for his Italian cuisine"
                // Британски познат готвач познат по италијанска кујна
        ));

        chefs.add(new Chef(
                3L,
                "Marco",
                "Pierre White",
                "First British chef to be awarded three Michelin stars"
                // Прв британски готвач со три Мишлен ѕвезди
        ));

        chefs.add(new Chef(
                4L,
                "Heston",
                "Blumenthal",
                "Pioneer of multi-sensory cooking and molecular gastronomy"
                // Пионер на мулти-сензорно готвење и молекуларна гастрономија
        ));

        chefs.add(new Chef(
                5L,
                "Massimo",
                "Bottura",
                "Italian chef with three Michelin stars, owner of Osteria Francescana"
                // Италијански готвач со три Мишлен ѕвезди
        ));
    }
}
