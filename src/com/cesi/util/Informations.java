package com.cesi.util;

import java.io.Serializable;

public class Informations implements Serializable{
	public long number;
	public String role;
	public long roleNum;
	public String name;
	public String userName;
	public long id;
	public String image;
	public String sex;
	public String userRole;
	
	public Informations(String number,String role,String roleNum,String name,String userName,String id,String image,String sex,String userRole) {
		// TODO Auto-generated constructor stub
		this.number=Long.parseLong(number);
		this.role=role;
		this.roleNum=Long.parseLong(roleNum);
		this.name=name;
		this.userName=userName;
		this.id=Long.parseLong(id);
		this.image=image;
		this.sex=sex;
		this.userRole=userRole;
	}
}
