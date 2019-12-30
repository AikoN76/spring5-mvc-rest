package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;

    public Bootstrap(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Category fruitCategory = new Category();
        fruitCategory.setName("Fruits");
        categoryRepository.save(fruitCategory);

        Category driedCategory = new Category();
        driedCategory.setName("Dried");
        categoryRepository.save(driedCategory);

        Category freshCategory = new Category();
        freshCategory.setName("Fresh");
        categoryRepository.save(freshCategory);

        Category exoticCategory = new Category();
        exoticCategory.setName("Exotic");
        categoryRepository.save(exoticCategory);

        Category nutsCategory = new Category();
        nutsCategory.setName("Nuts");
        categoryRepository.save(nutsCategory);

        log.debug("Data Loaded: " + categoryRepository.count());
    }
}
