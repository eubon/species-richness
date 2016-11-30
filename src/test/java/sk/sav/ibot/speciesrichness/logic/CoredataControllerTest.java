/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.logic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import sk.sav.ibot.speciesrichness.geo.Cell;
import sk.sav.ibot.speciesrichness.geo.LatLon;

/**
 *
 * @author Matus
 */
public class CoredataControllerTest {
    
    public CoredataControllerTest() {
    }
//
    /**
     * Test of retrieveCells method, of class CoredataController.
     */
    @org.junit.Test
    public void testRetrieveCells() {
//        System.out.println("retrieveCells");
//        SearchTerms search = null;
//        List<GbifTaxon> species = null;
//        CoredataController instance = null;
//        ResultItems expResult = null;
//        ResultItems result = instance.retrieveCells(search, species);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
    @org.junit.Test
    public void testTidyUpCells() {
        Cell one = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2000, 10);
        Cell two = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2000, 20);
        Cell three = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2001, 15);
        List<Cell> cellsDirty = new LinkedList<>();
        cellsDirty.add(one);
        cellsDirty.add(two);
        cellsDirty.add(three);
        List<Cell> expected = new LinkedList<>();
        expected.add(new Cell(new LatLon(10, 5), new LatLon(15, 10), 2000, 30));
        expected.add(three);
        
        CoredataController instance = new CoredataController(null);
        List<Cell> result = instance.tidyUpCells(cellsDirty);
        assertEquals(expected, result);
    }
    
    @org.junit.Test
    public void testMakeMap() {
        Cell one = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2000, 30);
        Cell two = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2005, 20);
        Cell three = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2001, 15);
        List<Cell> cells = new LinkedList<>();
        cells.add(one);
        cells.add(two);
        cells.add(three);
        
        Map<Integer, List<Cell>> mapExpected = new HashMap<>();
        List<Cell> cellsOne = new LinkedList<>();
        cellsOne.add(one);
        List<Cell> cellsTwo = new LinkedList<>();
        cellsTwo.add(two);
        List<Cell> cellsThree = new LinkedList<>();
        cellsThree.add(three);
        mapExpected.put(2000, cellsOne);
        mapExpected.put(2005, cellsTwo);
        mapExpected.put(2001, cellsThree);
        
        CoredataController instance = new CoredataController(null);
        Map<Integer, List<Cell>> mapActual = instance.makeMap(cells);
        assertEquals(mapExpected, mapActual);
    }
    
    @org.junit.Test
    public void testConvergeCells() {
        List<Cell> cells = new LinkedList<>();
        Cell one = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2007, 30);
        Cell two = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2005, 20);
        Cell three = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2001, 15);
        Cell four = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2003, 45);
        cells.add(one);
        cells.add(two);
        cells.add(three);
        cells.add(four);
        List<Cell> expected = new LinkedList<>();
        Cell a = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2011, 30);
        Cell b = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2006, 20);
        Cell c = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2001, 15);
        Cell d = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2006, 45);
        expected.add(a);
        expected.add(b);
        expected.add(c);
        expected.add(d);
        CoredataController instance = new CoredataController(null);
        assertEquals(expected, instance.convergeCells(cells, 5, 2001));
    }
    
    @org.junit.Test
    public void testConvergeTo() {
        CoredataController instance = new CoredataController(null);
        assertEquals(20, instance.convergeTo(18, 5, 0));
        assertEquals(20, instance.convergeTo(20, 5, 0));
        assertEquals(12, instance.convergeTo(11, 5, 2));
        assertEquals(12, instance.convergeTo(12, 5, 2));
    }
}
