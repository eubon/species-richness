/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.rest.results;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO class representing complete output. Contains search terms and layers of
 * cells
 *
 * @author Matus
 */
@XmlRootElement(name = "response")
public class ResultItems {

    @XmlElement(name = "searchTerms")
    private SearchTerms terms;

//    @XmlJavaTypeAdapter(ResultItemsMapAdapter.class)
//    @XmlElement(name="results")
//    private Map<String, List<ResultCell>> items; 
    
    private List<Layer> layers;

    public ResultItems() {
    }

    public ResultItems(SearchTerms terms, List<Layer> layers) {
        this.terms = terms;
        this.layers = layers;
    }

//    public ResultItems(SearchTerms terms, Map<String, List<ResultCell>> items) {
//        this.terms = terms;
//        this.items = items;
//    }
//    @JsonGetter("results")
//    public Map<String, List<ResultCell>> getResultItems() {
//        return this.items;
//    }
//
//    public void setItems(Map<String, List<ResultCell>> items) {
//        this.items = items;
//    }
    @JsonGetter("searchTerms")
    public SearchTerms getSearchTerms() {
        return this.terms;
    }

    public void setTerms(SearchTerms terms) {
        this.terms = terms;
    }

    @XmlElementWrapper(name = "layers")
    @XmlElement(name = "cells")
    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

}
