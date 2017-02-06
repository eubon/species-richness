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

    private LatLon bottomLeft;
    private LatLon topRight;

    public ResultCellBounds() {
    }

    public ResultCellBounds(LatLon bottomLeft, LatLon topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    public LatLon getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(LatLon bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public LatLon getTopRight() {
        return topRight;
    }

    public void setTopRight(LatLon topRight) {
        this.topRight = topRight;
    }

    @Override
    public String toString() {
        return "ResultCellBounds{" + "bottomLeft=" + bottomLeft + ", topRight=" + topRight + '}';
    }

}
