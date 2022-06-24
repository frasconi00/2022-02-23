package it.polito.tdp.yelp.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	
	private List<String> cities;
	private Map<String, Review> idMap;
	
	private Graph<Review, DefaultWeightedEdge> grafo;
	
	private List<Review> best;
	
	public Model() {
		this.dao = new YelpDao();
		this.cities = this.dao.getCities();
	}
	
	public List<String> getCities() {
		
		List<String> result = this.cities;
		
		Collections.sort(result);
		
		return this.cities;
	}
	
	public List<Business> getBusinessesCity(String city) {
		List<Business> result = this.dao.getBusinessesCity(city);
		
		Collections.sort(result, new Comparator<Business>() {

			@Override
			public int compare(Business o1, Business o2) {
				return o1.getBusinessName().compareTo(o2.getBusinessName());
			}
		});
		
		return result;
	}
	
	public void creaGrafo(Business locale) {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.idMap = new HashMap<String, Review>();
		this.dao.getReviewsBusiness(locale, idMap);
		
		Graphs.addAllVertices(this.grafo, this.idMap.values());
		
//		System.out.println("#vertici: "+grafo.vertexSet().size());
		
		//aggiungo gli archi
		
		for(Review r1 : grafo.vertexSet()) {
			for(Review r2 : grafo.vertexSet()) {
				
				if(r1.getReviewId().compareTo(r2.getReviewId())<0) {
					
					LocalDate ld1 = r1.getDate();
					LocalDate ld2 = r2.getDate();
					
					int peso = Math.abs((int) ChronoUnit.DAYS.between(ld1, ld2));
					
					if(peso!=0) {
						if(ld1.isBefore(ld2)) {
							Graphs.addEdgeWithVertices(grafo, r1, r2, peso);
						} else {
							Graphs.addEdgeWithVertices(grafo, r2, r1, peso);
						}
					}
					
				}
				
			}
		}
		
//		System.out.println("#archi: "+grafo.edgeSet().size());
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean grafoCreato() {
		if(this.grafo == null)
			return false;
		else
			return true;
	}
	
	public List<Adiacenza> getReviewMax() {
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		
		int max = -1;
		for(Review r1 : grafo.vertexSet()) {
			if(grafo.outDegreeOf(r1)>max) {
				result = new ArrayList<Adiacenza>();
				result.add(new Adiacenza(r1, grafo.outDegreeOf(r1)));
				max = grafo.outDegreeOf(r1);
			} else if(grafo.outDegreeOf(r1)==max) {
				result.add(new Adiacenza(r1, max));
			}
		}
		
		return result;
	}
	
	public void preparaRicorsione() {
		
		this.best = new ArrayList<Review>();
		
		for(Review partenza : this.grafo.vertexSet()) {
			List<Review> parziale = new ArrayList<Review>();
			parziale.add(partenza);
			cerca(parziale);
			
		}
		
	}

	private void cerca(List<Review> parziale) {
		if(parziale.size()>this.best.size()) {
			this.best = new ArrayList<Review>(parziale);
		}
		
		for(Review r : Graphs.successorListOf(grafo, parziale.get(parziale.size()-1))) {
			if(r.getStars()>=parziale.get(parziale.size()-1).getStars()) {
				parziale.add(r);
				cerca(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	
	public int differenzaGiorni() {
		int tot=0;
		for(int i=0;i<best.size()-1;i++) {
			tot+=grafo.getEdgeWeight(grafo.getEdge(best.get(i), best.get(i+1)));
		}
		
		return tot;
	}

	public List<Review> getBest() {
		return this.best;
	}
	
	
	
}
