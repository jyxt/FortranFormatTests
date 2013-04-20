/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

import java.awt.image.BufferedImage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sheldon.chi
 */
public class ModelPanelTest {
    
    public ModelPanelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getTheModel method, of class ModelPanel.
     */
    @Test
    public void testGetTheModel() {
        System.out.println("getTheModel");
        ModelPanel instance = null;
        Model expResult = null;
        Model result = instance.getTheModel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTheModel method, of class ModelPanel.
     */
    @Test
    public void testSetTheModel() {
        System.out.println("setTheModel");
        Model theModel = null;
        ModelPanel instance = null;
        instance.setTheModel(theModel);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLayer method, of class ModelPanel.
     */
    @Test
    public void testGetLayer() {
        System.out.println("getLayer");
        ModelPanel instance = null;
        int expResult = 0;
        int result = instance.getLayer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLayer method, of class ModelPanel.
     */
    @Test
    public void testSetLayer() {
        System.out.println("setLayer");
        int layer = 0;
        ModelPanel instance = null;
        instance.setLayer(layer);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProperty method, of class ModelPanel.
     */
    @Test
    public void testGetProperty() {
        System.out.println("getProperty");
        ModelPanel instance = null;
        ModelPanel.ModelProperty expResult = null;
        ModelPanel.ModelProperty result = instance.getProperty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setProperty method, of class ModelPanel.
     */
    @Test
    public void testSetProperty() {
        System.out.println("setProperty");
        ModelPanel.ModelProperty property = null;
        ModelPanel instance = null;
        instance.setProperty(property);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of model2Image method, of class ModelPanel.
     */
    @Test
    public void testModel2Image() {
        System.out.println("model2Image");
        Model aModel = null;
        int currentLayer = 0;
        ModelPanel instance = null;
        BufferedImage expResult = null;
        BufferedImage result = instance.model2Image(aModel);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}