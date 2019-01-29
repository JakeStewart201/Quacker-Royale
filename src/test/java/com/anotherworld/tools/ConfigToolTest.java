package com.anotherworld.tools;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConfigToolTest {
    private PropertyReader testProperty;
    private String propertyFileName = "test.properties";

    private String nameTag = "name";
    private String ageTag = "age";
    private String emptyTag = "empty";
    //private

    private void setUp(){
        try {
            testProperty = new PropertyReader(propertyFileName);
            testProperty.setValue(nameTag, "Bob");
            testProperty.setValue(ageTag,"21");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testGettingNonExisting() throws Exception{
        setUp();

        //Test the non-existing tag
        assertNull(testProperty.getValue(emptyTag) );
    }

    @Test
    public  void testGettingValues() throws Exception{
        setUp();

        //Reading values
        assertEquals("Bob",testProperty.getValue(nameTag));
        assertEquals("21", testProperty.getValue(ageTag));

    }

    @Test
    public void testSettingValues()throws Exception{
        setUp();

        //Changing existing values
        testProperty.setValue(nameTag,"Mike");
        testProperty.setValue(ageTag,"10");
        assertEquals("Mike", testProperty.getValue(nameTag));
        assertEquals("10", testProperty.getValue(ageTag));
    }

    @Test
    public void testSettingNewValues() throws Exception{
        setUp();

        //checking key are non-existent
        assertNull(testProperty.getValue(emptyTag));

        //setting the value
        testProperty.setValue(emptyTag,"Ball");

        //Checking if has a value now
        assertEquals("Ball",testProperty.getValue(emptyTag));

        //Cleaning up
        testProperty.deleteValue(emptyTag);
    }

    @Test
    public void testRemoval() throws Exception{
        setUp();

        //Check that doesn't exist
        assertNull(testProperty.getValue("NonExistent"));

        //Create the key
        testProperty.setValue("NonExistent","newValue");
        assertEquals("newValue",testProperty.getValue("NonExistent"));

        //Delete the key
        testProperty.deleteValue("NonExistent");
        assertNull(testProperty.getValue("NonExistent"));

    }
}
