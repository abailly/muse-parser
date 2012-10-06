/*______________________________________________________________________________
 * 
 * Copyright (C) 2008 Arnaud Bailly / OQube 
 * 
 * This work is licensed under the Creative Commons
 * Attribution-Noncommercial 3.0 License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by-nc/3.0/ or
 * send a letter to Creative Commons, 171 Second Street, Suite 300,
 * San Francisco, California, 94105, USA. 
 *  
 * email: contact@oqube.com
 * creation: Fri Aug  3 2007
 */
package oqube.muse.doxia;

import org.junit.Test;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.apache.maven.it.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Check that modules get properly linked when generating site.
 * 
 * @author abailly@norsys.fr
 * @version $Id$
 */
public class DoxiaModuleTest {
  @Test
   public void callingSiteGoalShouldParseMuseFiles() throws Exception {
    File testDir = ResourceExtractor.simpleExtractResources(getClass(), 
							    "/test-project" );
    System.err.println(testDir);
    Verifier verifier = new Verifier( testDir.getAbsolutePath() );
    verifier.executeGoal( "clean" );
    verifier.executeGoal( "site" );
    verifier.assertFilePresent("target/site/test.html");
    verifier.assertFilePresent("target/site/main.html");
    verifier.verifyErrorFreeLog();
    verifier.resetStreams();
 }
}
