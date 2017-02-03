/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.logic;

import java.util.HashSet;
import java.util.Set;
import sk.sav.ibot.speciesrichness.gbif.json.GbifTaxon;

/**
 * Used for storing basic information about taxonomy object
 * Either species or higher taxon
 * @author Matus
 */
public class NameUsageImpl implements NameUsage {
    
    private int gbifKey;
    private String scientificName;
    private String rank;
    private String matchType;
    
    private Set<Integer> higherKeys = new HashSet<>();

    public NameUsageImpl() {
    }

    public NameUsageImpl(int gbifKey, String scientificName, String rank, String matchType) {
        this.gbifKey = gbifKey;
        this.scientificName = scientificName;
        this.rank = rank;
        this.matchType = matchType;
    }
    
    public NameUsageImpl(GbifTaxon taxon) {
        this.gbifKey = taxon.getKey() != null ? taxon.getKey() : taxon.getUsageKey();
        this.scientificName = taxon.getScientificName();
        this.rank = taxon.getRank();
        this.matchType = taxon.getMatchType();
        this.higherKeys.add(taxon.getGenusKey());
        this.higherKeys.add(taxon.getFamilyKey());
        this.higherKeys.add(taxon.getOrderKey());
        this.higherKeys.add(taxon.getPhylumKey());
        this.higherKeys.add(taxon.getClassKey());
        this.higherKeys.add(taxon.getKingdomKey());
    }

    @Override
    public int getKey() {
        return gbifKey;
    }

    public void setKey(int gbifKey) {
        this.gbifKey = gbifKey;
    }

    @Override
    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    @Override
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    /**
     * Set of higher taxa gbif keys that this taxon belongs to
     * @return 
     */
    public Set<Integer> getHigherKeys() {
        return higherKeys;
    }

    public void setHigherKeys(Set<Integer> higherKeys) {
        this.higherKeys = higherKeys;
    }
    
    /**
     * Is the key present in the higher hierarchy of this taxon?
     * In other words, does this taxon belongs to higher taxon identified by key?
     * @param key
     * @return 
     */
    @Override
    public boolean isBelongsTo(int key) {
        return this.higherKeys.contains(key);
    }

    @Override
    public String toString() {
        return "NameUsageImpl{" + "gbifKey=" + gbifKey + ", scientificName=" + scientificName + ", rank=" + rank + ", matchType=" + matchType + '}';
    }

    

}
