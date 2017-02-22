package com.elekscamp.messenger_javafx_client.entities;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalInfo {

	private int id;
	private String picture;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private long birthDate;

	public PersonalInfo() {
	}

	public PersonalInfo(String picture, String firstName, String lastName, String phoneNumber, long birthDate) {
		this.picture = picture;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "PersonalInfo [id=" + id + ", picture=" + picture + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", phoneNumber=" + phoneNumber + ", birthDate=" + new Timestamp(birthDate) + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public long getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(long birthDate) {
		this.birthDate = birthDate;
	}
}
