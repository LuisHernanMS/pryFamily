package com.everis.pryfamily.services;

import java.util.Date;

import javax.naming.ServiceUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everis.pryfamily.dao.FamilyDao;
import com.everis.pryfamily.documents.Family;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FamilyServiceImpl implements FamilyService{

	@Autowired
	private FamilyDao dao;
	
	@Override
	public Flux<Family> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public Mono<Family> findById(String id) {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public Mono<Family> save(Family family) {

		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("prystudent");
		DBCollection dbCollection = db.getCollection("student");
		DBObject query = new BasicDBObject("numberDocument", family.getNumberDocumentst());
		Integer result = dbCollection.find(query).count(); 
		db = mongo.getDB("pryteacher");
		dbCollection = db.getCollection("teacher");
		query = new BasicDBObject("numberDocument", family.getNumberDocumentst());
		result = result+dbCollection.find(query).count(); 
		if (result>0) {
			db = mongo.getDB("pryfamily");
			dbCollection = db.getCollection("family");
			query = new BasicDBObject("numberDocument", family.getNumberDocument());
			result = dbCollection.find(query).count(); 
			if(result>0) {
				return null;
			}else {
				
				if(family.getParentesco().equals("padre") || family.getParentesco().equals("madre")) {
					query=BasicDBObjectBuilder.start().add("parentesco", "padre").add("numberDocumentst", family.getNumberDocumentst()).get();
					result = dbCollection.find(query).count(); 
					query=BasicDBObjectBuilder.start().add("parentesco", "madre").add("numberDocumentst", family.getNumberDocumentst()).get();
					result = result+dbCollection.find(query).count();
					if(result>1) {
						return Mono.error(new ServiceUnavailableException("This student already has two parents."));
					}else {
						return dao.save(family);
					}
				}else {
					if(family.getParentesco().equals("conyugue")) {
						query=BasicDBObjectBuilder.start().add("parentesco", "conyugue").add("numberDocumentst", family.getNumberDocumentst()).get();
						result = dbCollection.find(query).count();
						if(result>0) {
							return Mono.error(new ServiceUnavailableException("This student already has a spouse."));
						}
						else {
							return dao.save(family);
						}
					}else {
						return dao.save(family);
					}
				}
				
			}
			
		}else {
			return Mono.error(new ServiceUnavailableException("There is no student with ID "+family.getNumberDocumentst()));
		}
	}

	@Override
	public Mono<Void> delete(Family family) {
		return dao.delete(family);
	}

	@Override
	public Flux<Family> findByName(String name) {
		// TODO Auto-generated method stub
		return dao.obtenerPorName(name);
	}
	
	@Override
	public Mono<Family> findByNumberDocument(String numberDocument) {
		// TODO Auto-generated method stub
		return dao.obtenerPorNumberDocument(numberDocument);
	}

	@Override
	public Flux<Family> findByDate(Date start, Date end) {
		return dao.obtenerPorDate(start, end);
	}
}
