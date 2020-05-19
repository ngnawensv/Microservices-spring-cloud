package cm.belrose.Customerservice;

import cm.belrose.Customerservice.Rest.CustomerRepository;
import cm.belrose.Customerservice.entities.Customer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    /**
     * Au lieu d'utiliser la méthode qui retourne un CommandLineRunner on pourrait passer par l'implementation de 
     * l'interface CommandLineRunner qui permet de redefinir la méthode run
     * @param customerRepository
     * @return 
     */
    @Bean
    CommandLineRunner start(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(new Customer(null, "Dan", "dan@gmail.com"));
            customerRepository.save(new Customer(null, "Aaron", "aaron@gmail.com"));
            customerRepository.save(new Customer(null, "Sainte", "sainte@gmail.com"));
            customerRepository.save(new Customer(null, "Ndichout", "ndichout@gmail.com"));
            customerRepository.findAll().forEach(System.out::println);
        };
    }

}
