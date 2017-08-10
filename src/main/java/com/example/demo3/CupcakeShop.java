package com.example.demo3;

import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.*;
import java.util.List;

@SpringBootApplication
public class CupcakeShop {

    public static void main(String[] args) {
        SpringApplication.run(CupcakeShop.class, args);
    }

    @Bean
    CommandLineRunner initData(ClientRepository clientRepository,
                               CupcakeRepository cupcakeRepository) {
        return args -> {
            final Client alice = clientRepository.save(new Client("Alice"));
            final Client bob = clientRepository.save(new Client("Bob"));

            cupcakeRepository.save(new Cupcake("vanilla", "green", alice));
            cupcakeRepository.save(new Cupcake("chocolate", "green", alice));
            cupcakeRepository.save(new Cupcake("strawberry", "red", alice));

            cupcakeRepository.save(new Cupcake("vanilla", "red", bob));
            cupcakeRepository.save(new Cupcake("chocolate", "red", bob));
        };
    }
}

@Data
@Entity
class Client {
    @Id
    @GeneratedValue
    Long id;

    String name;

    @OneToMany
    List<Cupcake> cupcakes;

    private Client() {
    }

    public Client(String name) {
        this.name = name;
    }
}

@Data
@Entity
class Cupcake {
    @Id
    @GeneratedValue
    Long id;
    String flavor;
    String topping;

    @ManyToOne
    Client client;

    private Cupcake() {
    }

    public Cupcake(String flavor, String topping, Client client) {
        this.flavor = flavor;
        this.topping = topping;
        this.client = client;
    }
}

@RepositoryRestResource
interface CupcakeRepository extends CrudRepository<Cupcake, Long> {

    List<Cupcake> findByFlavor(@Param("q") String flavor);
    List<Cupcake> findByTopping(@Param("q") String topping);
    List<Cupcake> findByClientName(@Param("q") String name);

}

@RepositoryRestResource
interface ClientRepository extends CrudRepository<Client, Long> {

    Client findByName(@Param("q") String name);

}