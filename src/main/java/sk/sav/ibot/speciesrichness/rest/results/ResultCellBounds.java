/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.rest.results;

import sk.sav.ibot.speciesrichness.geo.LatLon;

/**
 * POJO class of cell boundaries.
 * @author Matus
 */
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
    
}
