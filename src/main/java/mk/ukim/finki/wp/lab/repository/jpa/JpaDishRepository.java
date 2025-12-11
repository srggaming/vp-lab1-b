package mk.ukim.finki.wp.lab.repository.jpa;

import mk.ukim.finki.wp.lab.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaDishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findAllByChef_Id(Long chefId);
    Dish findByDishId(String dishId);
}
