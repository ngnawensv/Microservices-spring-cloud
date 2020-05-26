package cm.belrose.Billingservice;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication implements Serializable {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BillRepository billRepository, ProductItemRepository productItemRepository) {
        return agrs -> {
            Bill bill1 = billRepository.save(new Bill(null, new Date(), 1L, null, null));
            productItemRepository.save(new ProductItem(null, 1L, null, 80, 500, bill1));
            productItemRepository.save(new ProductItem(null, 2L, null, 90, 900, bill1));
            productItemRepository.save(new ProductItem(null, 3L, null, 100, 200, bill1));
        };
    }

}

@RepositoryRestResource
interface BillRepository extends JpaRepository<Bill, Long> {
}

@RepositoryRestResource
interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    List<ProductItem> findByBillId(Long billID);
}
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
class Bill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date billingDate;
    private Long customerID;
    @Transient
    private Customer customer;
    @OneToMany(mappedBy = "bill")
    private Collection<ProductItem> productitems;
}
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
class ProductItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productID;
    @Transient
    private Product product;
    private double quantity;
    private double price;
    @ManyToOne
    private Bill bill;
}

@Data
class Customer {

    private Long id;
    private String name;
    private String email;
}

@Data
class Product {

    private Long id;
    private String name;
    private double price;
}

@FeignClient(name = "customer-service")
interface CustomerServiceClient {

    @GetMapping("/customers/{id}?projection=fullCustomer")
    Customer findCustomerById(@PathVariable("id") Long id);
}

@FeignClient(name = "inventory-service")
interface InventoryServiceClient {

    @GetMapping("/products/{id}?projection=fullCustomer")
    Product findProductById(@PathVariable("id") Long id);

    @GetMapping("/products?projection=fullCustomer")
    PagedModel<Product> findAll();
}

@RestController
class BillRestController {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private CustomerServiceClient customerServiceClient;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private InventoryServiceClient inventoryServiceClient;

    @GetMapping("/bills/full/{id}")
    Bill getBill(@PathVariable("id") Long id) {
        Bill bill = billRepository.findById(id).get();
        bill.setCustomer(customerServiceClient.findCustomerById(bill.getCustomerID()));
        bill.setProductitems(productItemRepository.findByBillId(id));
        bill.getProductitems().forEach(pi->{
            pi.setProduct(inventoryServiceClient.findProductById(pi.getProductID()));
        }
        );
        return bill;
    }
}
