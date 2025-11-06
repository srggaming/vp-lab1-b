package mk.ukim.finki.wp.lab.repository;

import mk.ukim.finki.wp.lab.bootstrap.DataHolder;
import mk.ukim.finki.wp.lab.model.Chef;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryChefRepository implements ChefRepository {

    @Override
    public List<Chef> findAll() {
        return DataHolder.chefs;
    }

    @Override
    public Optional<Chef> findById(Long id) {
        return DataHolder.chefs.stream()
                .filter(chef -> chef.getId().equals(id))
                .findFirst();
    }

    @Override
    public Chef save(Chef chef) {
        Optional<Chef> existingChef = this.findById(chef.getId());
        existingChef.ifPresent(c -> DataHolder.chefs.remove(c));
        DataHolder.chefs.add(chef);
        return chef;
    }
}
