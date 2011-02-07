// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.sequences;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.vardb.sequences.SequenceDataOnDemand;

privileged aspect SequenceIntegrationTest_Roo_IntegrationTest {
    
    declare @type: SequenceIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: SequenceIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: SequenceIntegrationTest: @Transactional;
    
    @Autowired
    private SequenceDataOnDemand SequenceIntegrationTest.dod;
    
    @Test
    public void SequenceIntegrationTest.testCountSequences() {
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to initialize correctly", dod.getRandomSequence());
        long count = org.vardb.sequences.Sequence.countSequences();
        org.junit.Assert.assertTrue("Counter for 'Sequence' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void SequenceIntegrationTest.testFindSequence() {
        org.vardb.sequences.Sequence obj = dod.getRandomSequence();
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to provide an identifier", id);
        obj = org.vardb.sequences.Sequence.findSequence(id);
        org.junit.Assert.assertNotNull("Find method for 'Sequence' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Sequence' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void SequenceIntegrationTest.testFindAllSequences() {
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to initialize correctly", dod.getRandomSequence());
        long count = org.vardb.sequences.Sequence.countSequences();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Sequence', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<org.vardb.sequences.Sequence> result = org.vardb.sequences.Sequence.findAllSequences();
        org.junit.Assert.assertNotNull("Find all method for 'Sequence' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'Sequence' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void SequenceIntegrationTest.testFindSequenceEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to initialize correctly", dod.getRandomSequence());
        long count = org.vardb.sequences.Sequence.countSequences();
        if (count > 20) count = 20;
        java.util.List<org.vardb.sequences.Sequence> result = org.vardb.sequences.Sequence.findSequenceEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'Sequence' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Sequence' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void SequenceIntegrationTest.testFlush() {
        org.vardb.sequences.Sequence obj = dod.getRandomSequence();
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to provide an identifier", id);
        obj = org.vardb.sequences.Sequence.findSequence(id);
        org.junit.Assert.assertNotNull("Find method for 'Sequence' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySequence(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Sequence' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SequenceIntegrationTest.testMerge() {
        org.vardb.sequences.Sequence obj = dod.getRandomSequence();
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to provide an identifier", id);
        obj = org.vardb.sequences.Sequence.findSequence(id);
        boolean modified =  dod.modifySequence(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        org.vardb.sequences.Sequence merged = (org.vardb.sequences.Sequence) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'Sequence' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SequenceIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to initialize correctly", dod.getRandomSequence());
        org.vardb.sequences.Sequence obj = dod.getNewTransientSequence(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Sequence' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'Sequence' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void SequenceIntegrationTest.testRemove() {
        org.vardb.sequences.Sequence obj = dod.getRandomSequence();
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Sequence' failed to provide an identifier", id);
        obj = org.vardb.sequences.Sequence.findSequence(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'Sequence' with identifier '" + id + "'", org.vardb.sequences.Sequence.findSequence(id));
    }
    
}