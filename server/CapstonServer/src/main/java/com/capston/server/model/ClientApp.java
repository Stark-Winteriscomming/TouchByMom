package com.capston.server.model;

public class ClientApp {
	private int appNumber;
	private String appName;
	private String packageName;
	private String client_id;
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getAppNumber() {
		return appNumber;
	}
	public void setAppNumber(int appNumber) {
		this.appNumber = appNumber;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
}
