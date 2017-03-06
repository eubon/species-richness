package sk.sav.ibot.speciesrichness.rest.results;

import io.swagger.annotations.ApiModel;
import sk.sav.ibot.speciesrichness.geo.LatLon;

/**
 * POJO class of cell boundaries.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@ApiModel(value = "CellBounds")
public class ResultCellBounds {

    private final LatLon bottomLeft;
    private final LatLon topRight;

    public ResultCellBounds(final LatLon bottomLeft, final LatLon topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    public LatLon getBottomLeft() {
        return bottomLeft;
    }

    public LatLon getTopRight() {
        return topRight;
    }

    @Override
    public String toString() {
        return "ResultCellBounds{" + "bottomLeft=" + bottomLeft + ", topRight=" + topRight + '}';
    }

}
