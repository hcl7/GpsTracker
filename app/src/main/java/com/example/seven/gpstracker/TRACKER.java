package com.example.seven.gpstracker;

public class TRACKER {
	
	private int id;
	private int uid;
	private String name = null;
    private String brand = null;
    private String model = null;
    private String phonenumber = null;
    private String password = null;
    private String color = null;
    
    TRACKER(){}
    
    TRACKER(String _brand, String _model){
    	this.brand = _brand;
    	this.model = _model;
    }
    
    TRACKER(String _name, String _brand, String _model, String _phonenumber, String _password){
    	this.name = _name;
        this.brand = _brand;
        this.model = _model;
        this.phonenumber = _phonenumber;
        this.password = _password;
    }

    TRACKER(int _id, String _name, String _brand, String _model, String _phonenumber, String _password, String _color){
    	this.id = _id;
    	this.name = _name;
        this.brand = _brand;
        this.model = _model;
        this.phonenumber = _phonenumber;
        this.password = _password;
        this.color = _color;
    }
    
    TRACKER(int _id, int _uid, String _name, String _brand, String _model, String _phonenumber, String _password, String _color){
    	this.id = _id;
    	this.uid = _uid;
    	this.name = _name;
        this.brand = _brand;
        this.model = _model;
        this.phonenumber = _phonenumber;
        this.password = _password;
        this.color = _color;
    }
    
    TRACKER(int _id, int _uid, String _name, String _brand, String _model, String _phonenumber, String _password){
    	this.id = _id;
    	this.uid = _uid;
    	this.name = _name;
        this.brand = _brand;
        this.model = _model;
        this.phonenumber = _phonenumber;
        this.password = _password;
    }
    
    public int getID()
    {
    	return this.id;
    }

    public void setID(int _id)
    {
    	this.id = _id;
    }
    public String getBrand() {
        return this.brand;
    }
    
    public int getUID(){
    	return this.uid;
    }
    
    public void setUID(int _uid){
    	this.uid = _uid;
    }
    
    public String getName()
    {
    	return this.name;
    }
    
    public void setName(String _name)
    {
    	this.name = _name;
    }
    
    public void setBrand(String _brand)
    {
    	this.brand = _brand;
    }

    public String getModel() {
        return this.model;
    }
    
    public void setModel(String _model)
    {
    	this.model = _model;
    }
    
    public String getPhoneNumber()
    {
    	return this.phonenumber;
    }
    
    public void setPhoneNumber(String _phonenumber)
    {
    	this.phonenumber = _phonenumber;
    }
    
    public String getPassword()
    {
    	return this.password;
    }
    
    public void setPassword(String _password)
    {
    	this.password = _password;
    }
    
    public String getColor()
    {
    	return this.color;
    }
    
    public void setColor(String _color)
    {
    	this.color = _color;
    }

}

class TypeModel {
	private int id;
    private String brand = null;
    private String model = null;
    
    TypeModel(){}
    
    TypeModel(int _id, String _brand, String _model){
    	this.id = _id;
    	this.brand = _brand;
    	this.model = _model;
    }
    
    TypeModel(String _brand, String _model){
    	this.brand = _brand;
    	this.model = _model;
    }
    
    public int getId(){
    	return this.id;
    }
    
    public void setID(int _id){
    	this.id = _id;
    }
    
    public String getBrand(){
    	return this.brand;
    }
    
    public void setBrand(String _brand){
    	this.brand = _brand;
    }
    
    public String getModel(){
    	return this.model;
    }
    
    public void setModel(String _model){
    	this.model = _model;
    }
}

class ModelCommand{
	
	private int id;
    private String model = null;
    private String name = null;
    private String command = null;
    
    ModelCommand(){}
    
    ModelCommand(int _id, String _model, String _name, String _command){
    	this.id = _id;
    	this.model = _model;
    	this.name = _name;
    	this.command = _command;
    }
    
    ModelCommand(String _model, String _name, String _command){
    	this.model = _model;
    	this.name = _name;
    	this.command = _command;
    }
    
    ModelCommand(String _name, String _command){
    	this.name = _name;
    	this.command = _command;
    }
    
    public int getId(){
    	return this.id;
    }
    
    public void setId(int _id){
    	this.id = _id;
    }
	
    public String getModel(){
    	return this.model;
    }
    
    public void setModel(String _model){
    	this.model = _model;
    }
    
    public String getName(){
    	return this.name;
    }
    
    public void setName(String _name){
    	this.name = _name;
    }
    
    public String getCommand(){
    	return this.command;
    }
    
    public void setCommand(String _command){
    	this.command = _command;
    }
}

class usrTracker {
	private int id;
    private String email = null;
    private String pass = null;
    
    usrTracker(){}
    usrTracker(int _id, String _email, String _pass){
    	this.id = _id;
    	this.email = _email;
    	this.pass = _pass;
    }
    
    usrTracker(String _email, String _pass){
    	this.email = _email;
    	this.pass = _pass;
    }
    
    public int getID(){
    	return this.id;
    }
    public void setID(int _id){
    	this.id = _id;
    }
    public String getEmail(){
    	return this.email;
    }
    public void setEmail(String _email){
    	this.email = _email;
    }
    public String getPass(){
    	return this.pass;
    }
    public void setPass(String _pass){
    	this.pass = _pass;
    }
}

class Logs {
	private int id;
	private int uid;
	private String sim = null;
	private String sms = null;
	private String date = null;
	private String name = null;
	private String color = null;
	Logs(){}
	Logs(int _id, int _uid, String _sim, String _sms, String _date, String _name, String _color){
		this.id = _id;
		this.uid = _uid;
		this.sim = _sim;
		this.sms = _sms;
		this.date = _date;
		this.name = _name;
		this.color = _color;
	}
	Logs(int _uid, String _sim, String _sms, String _date){
		this.uid = _uid;
		this.sim = _sim;
		this.sms = _sms;
		this.date = _date;
	}
	public int getID(){
		return this.id;
	}
	public void setID(int _id){
		this.id = _id;
	}
	public int getUid(){
		return this.uid;
	}
	public void setUid(int _uid){
		this.uid = _uid;
	}
	public String getSim(){
		return this.sim;
	}
	public void setSim(String _sim){
		this.sim = _sim;
	}
	public String getSms(){
		return this.sms;
	}
	public void setSms(String _sms){
		this.sms = _sms;
	}
	public String getDate(){
		return this.date;
	}
	public void setDate(String _date){
		this.date = _date;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String _name){
		this.name = _name;
	}
	public String getColor(){
		return this.color;
	}
	public void setColor(String _color){
		this.color = _color;
	}
}
