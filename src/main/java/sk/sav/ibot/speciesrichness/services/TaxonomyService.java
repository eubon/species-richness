package sk.sav.ibot.speciesrichness.services;

import java.util.List;
import sk.sav.ibot.speciesrichness.model.Taxonomy;

/**
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
public interface TaxonomyService {

    public Taxonomy getTaxonByGbifkey(long gbifkey);

    public List<Taxonomy> getHigherTaxonStartsWith(String query);

}
