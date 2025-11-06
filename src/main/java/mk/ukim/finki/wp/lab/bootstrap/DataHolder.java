package mk.ukim.finki.wp.lab.bootstrap;

import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    public static List<Chef> chefs = new ArrayList<>();
    public static List<Dish> dishes = new ArrayList<>();

    static {
        dishes.add(new Dish("1", "Pasta Carbonara", "Italian", 30));
        dishes.add(new Dish("2", "Beef Wellington", "British", 45));
        dishes.add(new Dish("3", "Chicken Tikka Masala", "Indian", 40));
        dishes.add(new Dish("4", "Sushi Platter", "Japanese", 50));
        dishes.add(new Dish("5", "Coq au Vin", "French", 60));

        chefs.add(new Chef(1L, "Gordon", "Ramsay", "World-renowned chef with 16 Michelin stars"));
        chefs.add(new Chef(2L, "Jamie", "Oliver", "British celebrity chef known for his Italian cuisine"));
        chefs.add(new Chef(3L, "Marco", "Pierre White", "First British chef to be awarded three Michelin stars"));
        chefs.add(new Chef(4L, "Heston", "Blumenthal", "Pioneer of multi-sensory cooking and molecular gastronomy"));
        chefs.add(new Chef(5L, "Massimo", "Bottura", "Italian chef with three Michelin stars, owner of Osteria Francescana"));
    }
}
