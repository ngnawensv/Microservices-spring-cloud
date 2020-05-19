/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.belrose.Inventoryservice.rest;

import cm.belrose.Inventoryservice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author PC-NGNAWEN
 */
@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Long>{
    
}
