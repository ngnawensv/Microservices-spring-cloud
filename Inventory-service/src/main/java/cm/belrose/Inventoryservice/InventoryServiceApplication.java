package cm.belrose.Inventoryservice;

import cm.belrose.Inventoryservice.entities.Product;
import cm.belrose.Inventoryservice.rest.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryServiceApplication implements CommandLineRunner{
    
    @Autowired
    ProductRepository produitsRepository;

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        produitsRepository.save(new Product(null,"Tomate",Math.random()*900));
        produitsRepository.save(new Product(null,"Patate",Math.random()*900));
        produitsRepository.save(new Product(null,"Igname",Math.random()*900));
        produitsRepository.findAll().forEach(System.out::println);
    }

}
