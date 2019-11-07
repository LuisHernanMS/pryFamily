package com.everis.pryfamily.services;

import java.util.Date;

import com.everis.pryfamily.documents.Family;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FamilyService {

	public Flux<Family> findAll();
	
	public Mono<Family> findById(String id);
	
	public Mono<Family> save(Family family);
	
	public Mono<Void> delete(Family family);
	
	public Flux<Family> findByName(String name);
	
	public Mono<Family> findByNumberDocument(String numberDocument);
	
	public Flux<Family> findByDate(Date start, Date end);
}
