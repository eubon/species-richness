/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.json.gbif;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Representation of JSON taxon returned by GBIF Rest API
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GbifTaxon {
    
    private Integer key;
    private Integer usageKey;
    private Integer kingdomKey;
    private Integer phylumKey;
    private Integer classKey;
    private Integer orderKey;
    private Integer familyKey;
    private Integer genusKey;
    private String matchType;
    
    private String scientificName;
    private String rank;

    public GbifTaxon() {
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getUsageKey() {
        return usageKey;
    }

    public void setUsageKey(Integer usageKey) {
        this.usageKey = usageKey;
    }

    public Integer getKingdomKey() {
        return kingdomKey;
    }

    public void setKingdomKey(Integer kingdomKey) {
        this.kingdomKey = kingdomKey;
    }

    public Integer getPhylumKey() {
        return phylumKey;
    }

    public void setPhylumKey(Integer phylumKey) {
        this.phylumKey = phylumKey;
    }

    public Integer getClassKey() {
        return classKey;
    }

    public void setClassKey(Integer classKey) {
        this.classKey = classKey;
    }

    public Integer getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(Integer orderKey) {
        this.orderKey = orderKey;
    }

    public long getFamilyKey() {
        return familyKey;
    }

    public void setFamilyKey(Integer familyKey) {
        this.familyKey = familyKey;
    }

    public Integer getGenusKey() {
        return genusKey;
    }

    public void setGenusKey(Integer genusKey) {
        this.genusKey = genusKey;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
    
    @Override
    public String toString() {
        return "Taxon{" + "key=" + key + ", usageKey=" + usageKey + ", kingdomKey=" + kingdomKey + ", phylumKey=" + phylumKey + ", classKey=" + classKey + ", orderKey=" + orderKey + ", familyKey=" + familyKey + ", genusKey=" + genusKey + ", scientificName=" + scientificName + '}';
    }
    
}