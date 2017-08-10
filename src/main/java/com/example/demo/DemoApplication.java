package com.example.demo;

import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(BoxRepository boxRepository,
							   CupcakeRepository cupcakeRepository) {
		return args -> {
			final Box bob = boxRepository.save(new Box("Bob"));
			final Box alice = boxRepository.save(new Box("Alice"));

			cupcakeRepository.save(new Cupcake("chocolate","red", bob));
			cupcakeRepository.save(new Cupcake("chocolate", "rainbow", alice));
			cupcakeRepository.save(new Cupcake("vanilla", "red", bob));
			cupcakeRepository.save(new Cupcake("vanilla", "rainbow", alice));
			cupcakeRepository.save(new Cupcake("strawberry", "red", alice));
		};
	}
}

@Data
@Entity
class Cupcake {
		@Id @GeneratedValue Long id;
	String flavour;
	String topping;

	@ManyToOne
	Box box;

	private Cupcake(){}
	public Cupcake(String flavour, String topping, Box box) {
		this.flavour = flavour;
		this.topping = topping;
		this.box = box;
	}
}

@Data
@Entity
class Box {
	@Id @GeneratedValue Long id;
	String client;

	@OneToMany
	List<Cupcake> cupcakes;

	private Box(){}
	public Box(String client) {
		this.client = client;
	}
}

@RepositoryRestResource
interface BoxRepository extends CrudRepository<Box, Long>{

	Box findByClient(@Param("q") String name);
}
@RepositoryRestResource
interface CupcakeRepository extends CrudRepository<Cupcake, Long>{
	List<Cupcake> findFlavourByBoxClient(@Param("q") String name);
}