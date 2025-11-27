package mk.ukim.finki.wp.lab.service.impl;

import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.repository.ChefRepository;
import mk.ukim.finki.wp.lab.repository.DishRepository;
import mk.ukim.finki.wp.lab.service.ChefService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ChefServiceImpl implements ChefService {
    private final ChefRepository chefRepository;
    private final DishRepository dishRepository;

    public ChefServiceImpl(ChefRepository chefRepository, DishRepository dishRepository) {
        this.chefRepository = chefRepository;
        this.dishRepository = dishRepository;
    }

    @Override
    public List<Chef> listChefs() {
        return this.chefRepository.findAll();
    }

    @Override
    public Chef findById(Long id) {
        return this.chefRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chef not found with id: " + id));
    }

    @Override
    public Chef addDishToChef(Long chefId, String dishId) {
        if (dishId == null || dishId.trim().isEmpty()) {
            throw new RuntimeException("Dish ID cannot be empty");
        }
        Chef chef = this.findById(chefId);
        Dish dish = this.dishRepository.findByDishId(dishId);
        if (dish == null) {
            throw new RuntimeException("Dish not found with id: " + dishId);
        }
        chef.getDishes().add(dish);
        return this.chefRepository.save(chef);
    }

    @Override
    public Optional<Chef> findMostPopularChef() {
        return this.chefRepository.findAll().stream()
                .max(Comparator.comparingInt(chef -> chef.getDishes().size()));
    }

    @Override
    public Chef create(String firstName, String lastName, String bio) {
        Long newId = this.chefRepository.findAll().stream()
                .mapToLong(Chef::getId)
                .max()
                .orElse(0L) + 1;
        Chef chef = new Chef(newId, firstName, lastName, bio);
        return this.chefRepository.save(chef);
    }

    @Override
    public Chef update(Long id, String firstName, String lastName, String bio) {
        Chef chef = this.findById(id);
        chef.setFirstName(firstName);
        chef.setLastName(lastName);
        chef.setBio(bio);
        return this.chefRepository.save(chef);
    }

    @Override
    public void delete(Long id) {
        this.chefRepository.deleteById(id);
    }
}
