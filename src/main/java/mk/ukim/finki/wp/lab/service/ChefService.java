package mk.ukim.finki.wp.lab.service;

import mk.ukim.finki.wp.lab.model.Chef;

import java.util.List;
import java.util.Optional;

public interface ChefService {
    List<Chef> listChefs();
    Chef findById(Long id);
    Chef addDishToChef(Long chefId, String dishId);
    Optional<Chef> findMostPopularChef();
    Chef create(String firstName, String lastName, String bio);
    Chef update(Long id, String firstName, String lastName, String bio);
    void delete(Long id);
}
