package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ingredientRepository")
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByPartyId(Long partyId);
}
