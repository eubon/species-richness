/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.gbif.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Representation of JSON results returned by GBIF Rest API
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GbifResults {
    
    private int offset;
    private int limit;
    private boolean endOfRecords;
    private int count;
    private List<GbifTaxon> results;

    public GbifResults() {
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isEndOfRecords() {
        return endOfRecords;
    }

    public void setEndOfRecords(boolean endOfRecords) {
        this.endOfRecords = endOfRecords;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<GbifTaxon> getResults() {
        return results;
    }

    public void setResults(List<GbifTaxon> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "GbifResults{" + "offset=" + offset + ", limit=" + limit + ", endOfRecords=" + endOfRecords + ", count=" + count + ", results=" + results + '}';
    }
    
}
