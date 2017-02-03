/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.logic;

/**
 *
 * @author Matus
 */
public interface NameUsage {
    
    public int getKey();
    public String getScientificName();
    public String getRank();
    
    public boolean isBelongsTo(int higherTaxonKey);
    
}
