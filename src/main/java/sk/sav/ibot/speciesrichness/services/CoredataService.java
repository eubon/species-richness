package sk.sav.ibot.speciesrichness.services;

import java.util.List;
import sk.sav.ibot.speciesrichness.model.Coredata;

/**
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
public interface CoredataService {

    public List<Coredata> getCoredataByTaxonkeys(List<Integer> taxonkeys, int sinceYear, int untilYear);

    public List<Integer> getAllTaxonkeys(int sinceYear, int untilYear);

}
