/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Adiacenza;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.Review;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnMiglioramento"
    private Button btnMiglioramento; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doRiempiLocali(ActionEvent event) {
    	this.cmbLocale.getItems().clear();
    	String citta = this.cmbCitta.getValue();
    	if(citta != null) {
    		//TODO popolare la tendina dei locali per la città selezionata
    		this.cmbLocale.getItems().addAll(model.getBusinessesCity(citta));
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Business locale = this.cmbLocale.getValue();
    	if(locale==null) {
    		txtResult.setText("Errore - per creare il grafo scegliere un business dopo aver scelto una città");
    		return;
    	}
    	
    	this.model.creaGrafo(locale);
    	
    	txtResult.setText("Grafo creato: ");
    	txtResult.appendText(model.nVertici()+" vertici, "+model.nArchi()+" archi");
    	
    	List<Adiacenza> lista = model.getReviewMax();
    	
    	txtResult.appendText("\n");
    	
    	for(Adiacenza a : lista) {
    		txtResult.appendText("\n"+a);
    	}
    	
    }

    @FXML
    void doTrovaMiglioramento(ActionEvent event) {
    	
    	if(!model.grafoCreato()) {
    		txtResult.setText("Errore-creare prima il grafo");
    		return;
    	}
    	
    	model.preparaRicorsione();
    	List<Review> best = model.getBest();
    	
    	txtResult.setText("Sequenza più lunga: ");
    	for(Review r : best)
    		txtResult.appendText("\n"+r);
    	
    	txtResult.appendText("\n\nDifferenza giorni: "+model.differenzaGiorni());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMiglioramento != null : "fx:id=\"btnMiglioramento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.cmbCitta.getItems().clear();
    	this.cmbCitta.getItems().addAll(model.getCities());
    }
}
