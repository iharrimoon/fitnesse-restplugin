package com.test.api;

import fitnesse.junit.FitNesseSuite;
import fitnesse.junit.FitNesseSuite.FitnesseDir;
import fitnesse.junit.FitNesseSuite.Name;
import fitnesse.junit.FitNesseSuite.OutputDir;
import org.junit.Test;
import org.junit.runner.RunWith;

//DO NOT CHANGE THIS IS WHAT RUNS THE SUITE
@RunWith(FitNesseSuite.class)

//THE SUITE THAT YOU WOULD LIKE TO RUN
@Name("FrontPage.TestRoot.TestingSuite")

//THE LOCATION OF YOUR FITNESSE SUITES
@FitnesseDir("../../fitnesseroot")

//THE LOCATION WHERE THE RESULTS WILL BE STORED
@OutputDir("TestResults")

public class SuiteTest {
	@Test
	public void dummyTest() {
		System.out.println("I work!");
	}
}