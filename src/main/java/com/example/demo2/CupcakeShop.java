package com.example.demo2;

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
    CommandLineRunner initData(ClientRepository clientRepository) {
        return args -> {
            final Client alice = clientRepository.save(new Client("Alice"));
            final Client bob = clientRepository.save(new Client("Bob"));
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

    private Client() {
    }

    public Client(String name) {
        this.name = name;
    }
}

@RepositoryRestResource
interface ClientRepository extends CrudRepository<Client, Long> {

    Client findByName(@Param("q") String name);

}