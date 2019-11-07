package com.everis.pryfamily.documents;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "family")
public class Family {
	
	@Id
	private String id;
	@NotEmpty
	private String name;
	@NotEmpty
	private String flastName;
	@NotEmpty
	private String mlastName;
	@NotEmpty
	private String gender;
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birth;
	@NotEmpty
	private String document;
	@NotEmpty
	private String numberDocument;
	@NotEmpty
	private String numberDocumentst;
	@NotEmpty
	private String parentesco;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFlastName() {
		return flastName;
	}
	public void setFlastName(String flastName) {
		this.flastName = flastName;
	}
	public String getMlastName() {
		return mlastName;
	}
	public void setMlastName(String mlastName) {
		this.mlastName = mlastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public String getNumberDocument() {
		return numberDocument;
	}
	public void setNumberDocument(String numberDocument) {
		this.numberDocument = numberDocument;
	}
	public String getNumberDocumentst() {
		return numberDocumentst;
	}
	public void setNumberDocumentst(String numberDocumentst) {
		this.numberDocumentst = numberDocumentst;
	}
	public String getParentesco() {
		return parentesco;
	}
	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}
	public Family(String name, String flastName, String mlastName, String gender, Date birth,
			String document, String numberDocument, String numberDocumentst, String parentesco) {
		super();
		this.name = name;
		this.flastName = flastName;
		this.mlastName = mlastName;
		this.gender = gender;
		this.birth = birth;
		this.document = document;
		this.numberDocument = numberDocument;
		this.numberDocumentst = numberDocumentst;
		this.parentesco = parentesco;
	}
	
	public Family() {
		super();
		// TODO Auto-generated constructor stub
	}

}
