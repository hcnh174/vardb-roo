package org.vardb.resources;

import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Family.class)
public class FamilyIntegrationTest {

    @Test
    public void testMarkerMethod() {
    	
    	Family family=new Family();
    	family.setIdentifier("plasmodium.falciparum.var");
    	family.setName("var");
    	family.setAttribute("chromosomes","subtelomeres of most chromosomes");
    	family.setAttribute("symptoms","fevers");
    	family.persist();
    }
}
