package com.want2play.core;

public enum Sport {
	FOOTBALL("Football"),
	BASKETBALL("Basket"),
	TENNIS("Tennis");
	
	private String label;
	
	private Sport (String label) {
		this.label = label;
	}
	
	public String getLabel() { return this.label; }
}
