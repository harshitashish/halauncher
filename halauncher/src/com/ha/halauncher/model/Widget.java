package com.ha.halauncher.model;

public class Widget {
	private int mid;
	private int mtopx;
	private int mtopy;

	public Widget(int mid, int mtopx, int mtopy) {
		super();
		this.mid = mid;
		this.mtopx = mtopx;
		this.mtopy = mtopy;
	}
	
	public Widget(){
		
	}

	public int getId() {
		return mid;
	}

	public void setId(int mid) {
		this.mid = mid;
	}

	public int getTopX() {
		return mtopx;
	}

	public void setTopX(int mtopx) {
		this.mtopx = mtopx;
	}

	public int getTopY() {
		return mtopy;
	}

	public void setTopY(int mtopy) {
		this.mtopy = mtopy;
	}

}
