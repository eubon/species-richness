package sk.sav.ibot.speciesrichness.dao;

import java.util.List;
import sk.sav.ibot.speciesrichness.model.Coredata;

/**
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
public interface CoredataDAO {
    
    public List<Coredata> getCoredataByTaxonkeys(List<Integer> taxonkeys, int sinceYear, int untilYear);
    public List<Integer> getAllTaxonkeys(int sinceYear, int untilYear);
    
}
