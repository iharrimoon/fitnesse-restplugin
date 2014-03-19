import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.junit.runner.RunWith;

import org.junit.After;

import fitnesse.junit.FitNesseSuite;
import fitnesse.junit.FitNesseSuite.FitnesseDir;
import fitnesse.junit.FitNesseSuite.Name;
import fitnesse.junit.FitNesseSuite.OutputDir;

@RunWith(FitNesseSuite.class)
@Name("FrontPage.TestRoot.TestingSuite.DevelopmentTestSuite")
@FitnesseDir("../../fitnesseroot")
@OutputDir("TestResults")


public class TestRunner {
    @After
    public void Results() throws IOException {
        // using this in real life, you'd probably want to check that the desktop
        // methods are supported using isDesktopSupported()...

        System.out.print("AFTER");

        String htmlFilePath = "fitnesse/FrontPage.TestRoot.TestingSuite.DevelopmentTestSuite.html"; // path to your new file
        File htmlFile = new File(htmlFilePath);

        // open the default web browser for the HTML page
        Desktop.getDesktop().browse(htmlFile.toURI());

        // if a web browser is the default HTML handler, this might work too
        Desktop.getDesktop().open(htmlFile);
    }
}