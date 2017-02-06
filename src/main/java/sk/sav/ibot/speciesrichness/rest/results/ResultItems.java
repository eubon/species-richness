package sk.sav.ibot.speciesrichness.rest.results;

import com.fasterxml.jackson.annotation.JsonGetter;
import io.swagger.annotations.ApiModel;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO class representing complete output. Contains search terms and layers of
 * cells
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@ApiModel(value = "Response")
@XmlRootElement(name = "response")
public class ResultItems {

    @XmlElement(name = "query")
    private SearchTerms terms;

    private List<Layer> layers;

    public ResultItems() {
    }

    public ResultItems(SearchTerms terms, List<Layer> layers) {
        this.terms = terms;
        this.layers = layers;
    }

    @JsonGetter("query")
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

    @Override
    public String toString() {
        return "ResultItems{layers=" + layers.size() + '}';
    }

}
