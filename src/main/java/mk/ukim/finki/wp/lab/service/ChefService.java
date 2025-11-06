package mk.ukim.finki.wp.lab.service;

import mk.ukim.finki.wp.lab.model.Chef;

import java.util.List;

public interface ChefService {
    List<Chef> listChefs();
    Chef findById(Long id);
    Chef addDishToChef(Long chefId, String dishId);
}
