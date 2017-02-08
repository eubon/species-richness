/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.sav.ibot.speciesrichness.geo.Cell;
import sk.sav.ibot.speciesrichness.geo.LatLon;

/**
 *
 * @author Matus
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
public class CoredataControllerTest {

    @Autowired
    CoredataController coredataService;

    @org.junit.Test
    public void testTidyUpCells() {
        Cell one = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2000, 10, 5);
        Cell two = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2000, 20, 4);
        Cell three = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2001, 15, 5);
        List<Cell> cellsDirty = new LinkedList<>();
        cellsDirty.add(one);
        cellsDirty.add(two);
        cellsDirty.add(three);
        List<Cell> expected = new LinkedList<>();
        expected.add(new Cell(new LatLon(10, 5), new LatLon(15, 10), 2000, 30, 9));
        expected.add(three);

        List<Cell> result = coredataService.tidyUpCells(cellsDirty);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testMakeMap() {
        Cell one = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2000, 30, 4);
        Cell two = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2005, 20, 6);
        Cell three = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2001, 15, 7);
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

        Map<Integer, List<Cell>> mapActual = coredataService.makeMap(cells);
        assertEquals(mapExpected, mapActual);
    }

    @org.junit.Test
    public void testConvergeCells() {
        List<Cell> cells = new LinkedList<>();
        Cell one = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2007, 30, 5);
        Cell two = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2005, 20, 3);
        Cell three = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2001, 15, 4);
        Cell four = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2003, 45, 15);
        cells.add(one);
        cells.add(two);
        cells.add(three);
        cells.add(four);
        List<Cell> expected = new LinkedList<>();
        Cell a = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2007, 30, 6);
        Cell b = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2006, 20, 3);
        Cell c = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2001, 15, 2);
        Cell d = new Cell(new LatLon(10, 5), new LatLon(15, 10), 2006, 45, 11);
        expected.add(a);
        expected.add(b);
        expected.add(c);
        expected.add(d);
        assertEquals(expected, coredataService.convergeCells(cells, 5, 2001, 2007));
    }

    @org.junit.Test
    public void testMakeUsedSpecies() {
        Set<Integer> taxaKeys = new HashSet<>();
        taxaKeys.add(111111);
        taxaKeys.add(222222);
        taxaKeys.add(333333);
        Set<NameUsage> allNames = new HashSet<>();
        NameUsage taxonA = new NameUsageImpl(111111, "Taxon A", "SPECIES", "EXACT");
        NameUsage taxonB = new NameUsageImpl(222222, "Taxon B", "SPECIES", "EXACT");
        NameUsage taxonC = new NameUsageImpl(444444, "Taxon C", "SPECIES", "EXACT");
        allNames.add(taxonA);
        allNames.add(taxonB);
        allNames.add(taxonC);
        Map<Integer, NameUsage> expected = new HashMap<>();
        expected.put(111111, taxonA);
        expected.put(222222, taxonB);
        
        Map<Integer, NameUsage> actual = coredataService.makeUsedSpecies(taxaKeys, allNames);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testConvergeTo() {
        assertEquals(20, CoredataController.convergeTo(18, 5, 0, 20));
        assertEquals(20, CoredataController.convergeTo(20, 5, 0, 20));
        assertEquals(12, CoredataController.convergeTo(11, 5, 2, 20));
        assertEquals(12, CoredataController.convergeTo(12, 5, 2, 20));
        assertEquals(18, CoredataController.convergeTo(17, 5, 3, 18));
    }

    @org.junit.Test
    public void testRegex() {
        String key = "3221";
        String wKey = "3221a";
        String name = "Alyssum alyssoides (L.) L.";
        String wName = "Alyssum alyssoides4 (L.) L.";
        String regex = "[1-9][0-9]*|[A-Za-z]+[A-Za-z \\.\\(\\)]*";
        assertTrue(key.matches(regex));
        assertFalse(wKey.matches(regex));
        assertTrue(name.matches(regex));
        assertFalse(wName.matches(regex));

    }
}
