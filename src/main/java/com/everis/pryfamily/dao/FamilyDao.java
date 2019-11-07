package com.everis.pryfamily.dao;

import java.util.Date;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.everis.pryfamily.documents.Family;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FamilyDao extends ReactiveMongoRepository<Family, String>{
	
	
	public Flux<Family> findByName(String name);

	@Query("{ $or : [{ name : ?0  }, { numberDocument : ?0  }, { id : ?0}]}")
	public Flux<Family> obtenerPorName(String name);
	
	public Mono<Family> findByNumberDocument(String numberDocument);
	
	@Query("{ numberDocument : ?0  }")
	public Mono<Family> obtenerPorNumberDocument(String numberDocument);

	//public Flux<student> findByDate(String start, String end);
	
	@Query("{ birth : {$gt : ?0, $lt : ?1}  }")
	public Flux<Family> obtenerPorDate(Date start, Date end);

}
