package mk.ukim.finki.wp.lab.bootstrap;

import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.repository.jpa.JpaChefRepository;
import mk.ukim.finki.wp.lab.repository.jpa.JpaDishRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final JpaChefRepository chefRepository;
    private final JpaDishRepository dishRepository;

    public DataInitializer(JpaChefRepository chefRepository, JpaDishRepository dishRepository) {
        this.chefRepository = chefRepository;
        this.dishRepository = dishRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only initialize data if the database is empty
        if (chefRepository.count() == 0) {
            // Create chefs
            Chef chef1 = new Chef("Gordon", "Ramsay", "World-renowned chef with 16 Michelin stars");
            Chef chef2 = new Chef("Jamie", "Oliver", "British celebrity chef known for his Italian cuisine");
            Chef chef3 = new Chef("Marco", "Pierre White", "First British chef to be awarded three Michelin stars");
            Chef chef4 = new Chef("Heston", "Blumenthal", "Pioneer of multi-sensory cooking and molecular gastronomy");
            Chef chef5 = new Chef("Massimo", "Bottura", "Italian chef with three Michelin stars, owner of Osteria Francescana");

            chefRepository.save(chef1);
            chefRepository.save(chef2);
            chefRepository.save(chef3);
            chefRepository.save(chef4);
            chefRepository.save(chef5);

            // Create dishes with ratings (1-5 stars)
            Dish dish1 = new Dish("1", "Pasta Carbonara", "Italian", 30, 5);
            Dish dish2 = new Dish("2", "Beef Wellington", "British", 45, 5);
            Dish dish3 = new Dish("3", "Chicken Tikka Masala", "Indian", 40, 4);
            Dish dish4 = new Dish("4", "Sushi Platter", "Japanese", 50, 4);
            Dish dish5 = new Dish("5", "Coq au Vin", "French", 60, 5);

            // Optionally assign chefs to dishes
            dish1.setChef(chef2); // Jamie Oliver - Italian cuisine
            dish2.setChef(chef1); // Gordon Ramsay - British dish
            dish5.setChef(chef1); // Gordon Ramsay - French cuisine

            dishRepository.save(dish1);
            dishRepository.save(dish2);
            dishRepository.save(dish3);
            dishRepository.save(dish4);
            dishRepository.save(dish5);
        }
    }
}
