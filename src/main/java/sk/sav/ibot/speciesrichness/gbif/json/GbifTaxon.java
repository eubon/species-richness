/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.gbif.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

/**
 * Representation of JSON taxon returned by GBIF Rest API
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GbifTaxon {
    
    public static final String SPECIES = "SPECIES";
    public static final String ACCEPTED = "ACCEPTED";
    public static final String NONE = "NONE";
    
    private Integer key;
    private Integer usageKey;
    private Integer kingdomKey;
    private Integer phylumKey;
    private Integer classKey;
    private Integer orderKey;
    private Integer familyKey;
    private Integer genusKey;
    
    private String matchType;
    private String note;
    
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

    public Integer getFamilyKey() {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.key);
        hash = 43 * hash + Objects.hashCode(this.usageKey);
        hash = 43 * hash + Objects.hashCode(this.kingdomKey);
        hash = 43 * hash + Objects.hashCode(this.phylumKey);
        hash = 43 * hash + Objects.hashCode(this.classKey);
        hash = 43 * hash + Objects.hashCode(this.orderKey);
        hash = 43 * hash + Objects.hashCode(this.familyKey);
        hash = 43 * hash + Objects.hashCode(this.genusKey);
        hash = 43 * hash + Objects.hashCode(this.matchType);
        hash = 43 * hash + Objects.hashCode(this.note);
        hash = 43 * hash + Objects.hashCode(this.scientificName);
        hash = 43 * hash + Objects.hashCode(this.rank);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GbifTaxon other = (GbifTaxon) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        if (!Objects.equals(this.rank, other.rank)) {
            return false;
        }
        if (!Objects.equals(this.scientificName, other.scientificName)) {
            return false;
        }
        if (!Objects.equals(this.usageKey, other.usageKey)) {
            return false;
        }
        if (!Objects.equals(this.genusKey, other.genusKey)) {
            return false;
        }
        if (!Objects.equals(this.familyKey, other.familyKey)) {
            return false;
        }
        if (!Objects.equals(this.orderKey, other.orderKey)) {
            return false;
        }
        if (!Objects.equals(this.classKey, other.classKey)) {
            return false;
        }
        if (!Objects.equals(this.phylumKey, other.phylumKey)) {
            return false;
        }
        if (!Objects.equals(this.kingdomKey, other.kingdomKey)) {
            return false;
        }
        if (!Objects.equals(this.matchType, other.matchType)) {
            return false;
        }
        return Objects.equals(this.note, other.note);
    }
    
    
    
    @Override
    public String toString() {
        return "GbifTaxon{" + "key=" + key + ", usageKey=" + usageKey + ", kingdomKey=" + kingdomKey + ", phylumKey=" + phylumKey + ", classKey=" + classKey + ", orderKey=" + orderKey + ", familyKey=" + familyKey + ", genusKey=" + genusKey + ", scientificName=" + scientificName + '}';
    }
    
}
