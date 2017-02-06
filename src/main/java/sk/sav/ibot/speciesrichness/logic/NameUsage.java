package sk.sav.ibot.speciesrichness.logic;

/**
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
public interface NameUsage {

    public int getKey();

    public String getScientificName();

    public String getRank();

    public boolean isBelongsTo(int higherTaxonKey);

}
