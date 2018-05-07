package utils;

public class Counter {

	private int countObj;
	private int scanCounter;
	
	public Counter() {
		this.countObj = 0;
		this.scanCounter = 0;
	}
	
	public int getCount() {
		return this.countObj;
	}
	
	public void incCount() {
		this.countObj++;
	}
	
	public void resetCount() {
		this.countObj = 0;
	}
	
	public int getScanCount() {
		return this.scanCounter;
	}
	
	public void incScanCount() {
		this.scanCounter++;
	}
}
