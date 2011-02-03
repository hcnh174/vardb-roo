// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.vardb.resources.PathogenDataOnDemand;

privileged aspect PathogenIntegrationTest_Roo_IntegrationTest {
    
    declare @type: PathogenIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: PathogenIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: PathogenIntegrationTest: @Transactional;
    
    @Autowired
    private PathogenDataOnDemand PathogenIntegrationTest.dod;
    
    @Test
    public void PathogenIntegrationTest.testCountPathogens() {
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to initialize correctly", dod.getRandomPathogen());
        long count = org.vardb.resources.Pathogen.countPathogens();
        org.junit.Assert.assertTrue("Counter for 'Pathogen' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void PathogenIntegrationTest.testFindPathogen() {
        org.vardb.resources.Pathogen obj = dod.getRandomPathogen();
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to provide an identifier", id);
        obj = org.vardb.resources.Pathogen.findPathogen(id);
        org.junit.Assert.assertNotNull("Find method for 'Pathogen' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Pathogen' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void PathogenIntegrationTest.testFindAllPathogens() {
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to initialize correctly", dod.getRandomPathogen());
        long count = org.vardb.resources.Pathogen.countPathogens();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Pathogen', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<org.vardb.resources.Pathogen> result = org.vardb.resources.Pathogen.findAllPathogens();
        org.junit.Assert.assertNotNull("Find all method for 'Pathogen' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'Pathogen' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void PathogenIntegrationTest.testFindPathogenEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to initialize correctly", dod.getRandomPathogen());
        long count = org.vardb.resources.Pathogen.countPathogens();
        if (count > 20) count = 20;
        java.util.List<org.vardb.resources.Pathogen> result = org.vardb.resources.Pathogen.findPathogenEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'Pathogen' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Pathogen' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void PathogenIntegrationTest.testFlush() {
        org.vardb.resources.Pathogen obj = dod.getRandomPathogen();
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to provide an identifier", id);
        obj = org.vardb.resources.Pathogen.findPathogen(id);
        org.junit.Assert.assertNotNull("Find method for 'Pathogen' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyPathogen(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Pathogen' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void PathogenIntegrationTest.testMerge() {
        org.vardb.resources.Pathogen obj = dod.getRandomPathogen();
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to provide an identifier", id);
        obj = org.vardb.resources.Pathogen.findPathogen(id);
        boolean modified =  dod.modifyPathogen(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        org.vardb.resources.Pathogen merged = (org.vardb.resources.Pathogen) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'Pathogen' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void PathogenIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to initialize correctly", dod.getRandomPathogen());
        org.vardb.resources.Pathogen obj = dod.getNewTransientPathogen(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Pathogen' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'Pathogen' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void PathogenIntegrationTest.testRemove() {
        org.vardb.resources.Pathogen obj = dod.getRandomPathogen();
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Pathogen' failed to provide an identifier", id);
        obj = org.vardb.resources.Pathogen.findPathogen(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'Pathogen' with identifier '" + id + "'", org.vardb.resources.Pathogen.findPathogen(id));
    }
    
}
