package com.nikhil.itemCatelog;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EnableDiscoveryClient
@SpringBootApplication
public class ItemCatelogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemCatelogApplication.class, args);
	}

}


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
class Item{
	
	@javax.persistence.Id
	@GeneratedValue
	private Long Id;
	
	private String name;
	
	
	public Item() {
	
	}


	public Item(String name) {
        this.name = name;
    }


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}

@RepositoryRestController
interface ItemRepository extends JpaRepository<Item, Long>{
	
}

@Component
class ItemInitializer implements CommandLineRunner{

	private final ItemRepository itemRepository;

    ItemInitializer(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
	
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		Stream.of("Puma","Nike","Lineing","Bad Boy","Air Jordan","Adidas", "Reebok")
		.forEach(item -> itemRepository.save(new Item(item)));
		
		itemRepository.findAll().forEach(item -> System.out.println(item.getName()));
		
	}
	
	@RestController
	public class itemCatalogController{
		
		@Autowired
		ItemRepository itemRepository;
		
		@GetMapping("/items")
		public List<Item> getitemCatalog(){
			return itemRepository.findAll().stream().collect(Collectors.toList());
		}
	}
	
}
