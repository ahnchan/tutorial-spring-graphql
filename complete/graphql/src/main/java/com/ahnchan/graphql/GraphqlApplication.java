package com.ahnchan.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import graphql.com.google.common.base.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GraphqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlApplication.class, args);
	}

}

@Controller
class ProductGraphqlController {

	// Repository
	private ProductRepository productRepository;
	private ReviewRepository reviewRepository;

	public ProductGraphqlController(ProductRepository productRepository, ReviewRepository reviewRepository) {
		this.productRepository = productRepository;
		this.reviewRepository = reviewRepository;
	}

	/**
	 * Find all products
	 * @return Flux<Product> return product list
	 */
	// Same annotation
//	@SchemaMapping (typeName = "Query", field = "products")
//	@QueryMapping (name = "products")
	@QueryMapping
	Flux<Product> products () {
		return this.productRepository.findAll();
	}

	/**
	 * Find a product by title
	 * @param title Title
	 * @return Flux<Prodcut> return product list
	 */
	@QueryMapping
	Flux<Product> productByTitle(@Argument String title) {
		return this.productRepository.findByTitle(title);
	}

	/**
	 * Find a product by id
	 * @param id ID
	 * @return Mono<Product> return a product
	 */
	@QueryMapping
	Mono<Product> productById(@Argument Integer id) {
		return this.productRepository.findById(id);
	}

	/**
	 * Find reviews by product
	 * @param product Product
	 * @return Flux<Review> return reviews
	 */
	@SchemaMapping(typeName = "Product")
	Flux<Review> reviews(Product product) {
		return this.reviewRepository.findByProductid(product.id());
	}

	@MutationMapping
	Mono<Product> addProduct(@Argument String title, @Argument String author, @Argument String ISBN) {
		return this.productRepository.save(new Product(null, title, author, ISBN));
	}

}

interface ProductRepository  extends ReactiveCrudRepository<Product, Integer> {
	Flux<Product> findByTitle(String title);
}

interface ReviewRepository extends ReactiveCrudRepository<Review, Integer> {
	Flux<Review> findByProductid(Integer productid);
}

record Product(@Id Integer id, String title, String author, String ISBN) {
}

record Review(@Id Integer id, Integer productid, String reviewer, String text) {
}

