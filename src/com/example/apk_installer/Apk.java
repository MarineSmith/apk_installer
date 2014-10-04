package com.example.apk_installer;

public class Apk {
	
	private String file_name;
	private String file_path;
	
	public Apk(String file_name,String file_path){
		this.file_name=file_name;
		this.file_path=file_path;
	}
	
	public String getName(){return file_name;}
	public String getPath(){return file_path;}
}
