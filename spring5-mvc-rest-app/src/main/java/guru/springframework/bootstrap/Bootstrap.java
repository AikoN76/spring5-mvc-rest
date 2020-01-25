package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Customer;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.CustomerRepository;
import guru.springframework.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;

    private CustomerRepository customerRepository;

    private VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadCategories();
        loadCustomers();
        loadVendors();
    }

    private void loadCustomers() {
        Customer customerOne = new Customer();
        customerOne.setFirstname("John");
        customerOne.setLastname("Doe");
        customerRepository.save(customerOne);

        Customer customerTwo = new Customer();
        customerTwo.setFirstname("Anne");
        customerTwo.setLastname("Green");
        customerRepository.save(customerTwo);

        Customer customerThree = new Customer();
        customerThree.setFirstname("Julia");
        customerThree.setLastname("Smith");
        customerRepository.save(customerThree);

        Customer customerFour = new Customer();
        customerFour.setFirstname("Andy");
        customerFour.setLastname("Brown");
        customerRepository.save(customerFour);

        Customer customerFive = new Customer();
        customerFive.setFirstname("Thomas");
        customerFive.setLastname("White");
        customerRepository.save(customerFive);

        Customer customerSix = new Customer();
        customerSix.setFirstname("Laura");
        customerSix.setLastname("Black");
        customerRepository.save(customerSix);

        log.debug("Data Customer Loaded: " + customerRepository.count());
    }

    private void loadCategories() {
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

        log.debug("Data Category Loaded: " + categoryRepository.count());
    }

    private void saveVendor(String name){
        Vendor vendor = new Vendor();
        vendor.setName(name);
        vendorRepository.save(vendor);
    }

    private void loadVendors() {
        List<String> vendorNames = Arrays.asList("Western Tasty Fruits Ltd.", "Exotic Fruits Company", "Home Fruits", "Fun Fresh Fruits Ltd.", "Nuts for Nuts Company", "Marche Gare", "Marche Gare 2", "Marche Gare 3", "Marche Gare 4");
        for(String name : vendorNames){
            saveVendor(name);
        }
        log.debug("Data Vendor Loaded: " + vendorRepository.count());
    }


}
