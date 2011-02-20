package org.vardb.setup;

//import groovy.lang.GroovyShell;

import javax.swing.JFrame;

import org.vardb.util.CException;
import org.vardb.weka.WekaHelper;

import bsh.Interpreter;
import bsh.util.JConsole;


public class Setup {

	// mvn exec:java
	public static void main(String ... args)
	{
		//Setup setup=new Setup();
		String filename="f:/analysis/personalized/svr.arff";
		WekaHelper.test(filename);
	}
	
	
	public Setup()
	{
		String dir="C:/dropbox/My Dropbox/vardb/dr-5/sequence/";
		//String dir="C:/Documents and Settings/nhayes/My Documents/My Dropbox/vardb/dr-5/sequence/";
		String filename=dir+"anaplasma.marginale_st_maries-msp2_p44_map1_omp.txt";
		
		try
		{
			JConsole console = new JConsole();
			SetupServiceImpl service=new SetupServiceImpl();
			Interpreter interpreter = new Interpreter(console);
			interpreter.set("service",service);
			interpreter.set("filename",filename);
			new Thread( interpreter ).start(); // start a thread to call the run() method
			
			JFrame frame = new JFrame("varDB shell");		
			frame.getContentPane().add( console, "Center" );
			frame.pack();
			frame.setSize(500,400);
			frame.setVisible(true);
		}
		catch (Exception e)
		{
			throw new CException(e);
		}
	}

}


//java groovy.lang.GroovyShell foo/MyScript.groovy [arguments]
//GroovyShell.main(new String[]{"setup.groovy"});
//groovy.lang.GroovyShell.main(groovy.lang.GroovyShell.EMPTY_ARGS);

//String password = new jline.ConsoleReader().readLine(new Character('*'));
//System.out.println("password: "+password);

//ConsoleRunner console = new ConsoleRunner();
//ConsoleRunner.main(new String[]{"bsh.Interpreter"});
//bsh.Console.main(new String[]{});
//bsh.Interpreter.main(new String[]{});


/*		
//String dir="C:/Documents and Settings/nhayes/My Documents/My Dropbox/vardb/dr-5/sequence/";
String dir=basedir+"sequence/";
Setup setup=new Setup();
String filename="anaplasma.marginale_st_maries-msp2_p44_map1_omp.txt";
setup.loadSequences(dir+filename);
*/	
