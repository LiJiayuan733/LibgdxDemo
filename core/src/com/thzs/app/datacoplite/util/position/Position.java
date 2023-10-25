package com.thzs.app.datacoplite.util.position;

import java.io.Serializable;

public class Position implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public int x, y, z;
	
	public Position() {
	}

	public Position(int x, int y, int z) {
		set(x, y, z);
	}

	public Position set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
}
