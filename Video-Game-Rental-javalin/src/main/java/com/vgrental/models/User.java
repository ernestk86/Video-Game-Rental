package com.vgrental.models;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable{

	private static final long serialVersionUID = -4553508689874995351L;
	
	private int id;
	private String username;
	private String password;
	private String dateOfBirth;
	private String address;
	private String city;
	private STATES state;
	private int zipCode;
	
	public User() {
		super();
	}

	public User(int id, String username, String password, String dateOfBirth, String address, String city, STATES state,
			int zipCode) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public STATES getState() {
		return state;
	}
	
	public void setState(STATES state) {
		this.state = state;
	}
	
	public int getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, city, dateOfBirth, id, password, state, username, zipCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(address, other.address) && Objects.equals(city, other.city)
				&& Objects.equals(dateOfBirth, other.dateOfBirth) && id == other.id
				&& Objects.equals(password, other.password) && state == other.state
				&& Objects.equals(username, other.username) && zipCode == other.zipCode;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", dateOfBirth=" + dateOfBirth
				+ ", address=" + address + ", city=" + city + ", state=" + state + ", zipCode=" + zipCode + "]";
	}	
}
