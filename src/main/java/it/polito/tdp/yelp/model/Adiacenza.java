package it.polito.tdp.yelp.model;

public class Adiacenza {
	
	private Review r;
	private int numArchiUscenti;
	public Adiacenza(Review r, int numArchiUscenti) {
		super();
		this.r = r;
		this.numArchiUscenti = numArchiUscenti;
	}
	public Review getR() {
		return r;
	}
	public void setR(Review r) {
		this.r = r;
	}
	public int getNumArchiUscenti() {
		return numArchiUscenti;
	}
	public void setNumArchiUscenti(int numArchiUscenti) {
		this.numArchiUscenti = numArchiUscenti;
	}
	@Override
	public String toString() {
		return r.getReviewId()+"\t#archi uscenti: "+numArchiUscenti;
	}

}
