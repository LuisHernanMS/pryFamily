package com.everis.pryfamily.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import static org.springframework.web.reactive.function.BodyInserters.*;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.everis.pryfamily.documents.Family;
import com.everis.pryfamily.services.FamilyService;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import reactor.core.publisher.Mono;

@Component
public class FamilyHandler {
	
	@Autowired
	private FamilyService service;

	public Mono<ServerResponse> listar(ServerRequest request){
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(service.findAll(), Family.class);
	}
	
	public Mono<ServerResponse> ver(ServerRequest request){
		String name = request.pathVariable("name");
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(service.findByName(name), Family.class)
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> verfec(ServerRequest request){
		String start = request.pathVariable("start");
		String end = request.pathVariable("end");
		start=start+" 00:00:00.000 +0000";
		end=end+" 00:00:00.000 +0000";
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
		try {
			Date inicio=df.parse(start);
			Date fin=df.parse(end);
			return ServerResponse.ok()
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(service.findByDate(inicio, fin), Family.class)
					.switchIfEmpty(ServerResponse.notFound().build());
		} catch (Exception e) {
			ServerResponse.notFound().build();
			return null;
		}	
	}
	
	public Mono<ServerResponse> crear(ServerRequest request){
		Mono<Family> family= request.bodyToMono(Family.class);
		return family.flatMap(p->{
			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("prystudent");
			DBCollection dbCollection = db.getCollection("student");
			DBObject query = new BasicDBObject("numberDocument", p.getNumberDocumentst());
			Integer result = dbCollection.find(query).count(); 
			db = mongo.getDB("pryteacher");
			dbCollection = db.getCollection("teacher");
			query = new BasicDBObject("numberDocument", p.getNumberDocumentst());
			result = result+dbCollection.find(query).count(); 
			if (result>0) {
				db = mongo.getDB("pryfamily");
				dbCollection = db.getCollection("family");
				query = new BasicDBObject("numberDocument", p.getNumberDocument());
				result = dbCollection.find(query).count(); 
				if(result>0) {
					return null;
				}else {
					
					if(p.getParentesco().equals("padre") || p.getParentesco().equals("madre")) {
						//query = new BasicDBObject("parentesco", "padre");
						query=BasicDBObjectBuilder.start().add("parentesco", "padre").add("numberDocumentst", p.getNumberDocumentst()).get();
						result = dbCollection.find(query).count(); 
						//Integer n=result;
						query=BasicDBObjectBuilder.start().add("parentesco", "madre").add("numberDocumentst", p.getNumberDocumentst()).get();
						result = result+dbCollection.find(query).count();
						//n=n+result;
						if(result>1) {
							System.out.println(result + " ya existen 2 padres");
							return null;
						}else {
							return service.save(p);
						}
					}else {
						if(p.getParentesco().equals("conyugue")) {
							query=BasicDBObjectBuilder.start().add("parentesco", "conyugue").add("numberDocumentst", p.getNumberDocumentst()).get();
							result = dbCollection.find(query).count();
							if(result>0) {
								return null;
							}
							else {
								return service.save(p);
							}
						}else {
							return service.save(p);
						}
					}
					
				}
				
			}else {
				System.out.println(result + " no existe student");
				return null;
			}	
		}).flatMap(p->ServerResponse.created(URI.create("api/v2/family/".concat(p.getId())))
				.body(fromObject(p)));
	}
	
	public Mono<ServerResponse> editar(ServerRequest request){
		Mono<Family> family= request.bodyToMono(Family.class);
		String id = request.pathVariable("id");
		
		Mono<Family> familyDB = service.findById(id);
		
		return familyDB.zipWith(family, (db,req)->{
			db.setName(req.getName());
			db.setFlastName(req.getFlastName());
			db.setMlastName(req.getMlastName());
			db.setBirth(req.getBirth());
			db.setDocument(req.getDocument());
			db.setNumberDocument(req.getNumberDocument());
			db.setNumberDocumentst(req.getNumberDocumentst());
			db.setParentesco(req.getParentesco());
			db.setGender(req.getGender());
			return db;
		}).flatMap(p->ServerResponse.created(URI.create("/api/v2/family".concat(p.getId())))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(service.save(p),Family.class))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest request){
		String id = request.pathVariable("id");
		Mono<Family> familyDB = service.findById(id);
		
		return familyDB.flatMap(p->service.delete(p).then(ServerResponse.noContent().build()))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	
}
