/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * <Ashvin Roharia>
 * <ar34426>
 * <16475>
 * <Ram Muthukumar>
 * <rm48763>
 * <16470>
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4; // cannot be in default package
import java.util.List;
import java.util.Scanner;
import java.io.*;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console
    static String in;   //user's input
    static boolean hasDisplayedError = false; // true if an error has just been displayed


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        
        while(true) {
			System.out.print("critters>");
			in = kb.nextLine();	
			String[] parameters = in.split(" ");
			
			switch (parameters[0]) {
				//if user's types "stats"
				case "stats":
					//is input valid - base check
					if (parameters.length != 2) {
						System.out.println("error processing: " + in);
					}
					else {
						/*
						String requested_class = parameters[1];
						List<Critter> instances = null;
						try{
							instances = Critter.getInstances(requested_class);	
						} catch (InvalidCritterException e) {
							System.out.println("error processing: " + in);	
							if(hasDisplayedError){
								hasDisplayedError = false;
								break;
							}
						}
						
						if (instances.size() <= 0) {
							System.out.println("error processing: " + in);
							continue;
						}
						Class<?> critter_class = null;
						Class [] paramList = new Class[1];
						paramList[0] = java.util.List.class;

						try{
							critter_class = Class.forName(requested_class);
							java.lang.reflect.Method runStats = critter_class.getMethod("runStats", paramList);
							runStats.invoke(critter_class, instances);
						} catch (Exception e1) {
							System.out.println("error processing: " + in);						
						}
						*/
						String inputClass = parameters[1];
        				List<Critter> crits = null;
        				try {
        					crits = Critter.getInstances(inputClass);
        				}
        				catch (InvalidCritterException e) {
        					System.out.println("error processing: " + parameters);
        				}
        				Class<?> critClass = null;
        				Class<?>[] types = {List.class};
        				try {
        					critClass = Class.forName(myPackage + "." + inputClass);
        					java.lang.reflect.Method runStats = critClass.getMethod("runStats", types);
        					runStats.invoke(critClass, crits);
        				}
        				catch (Exception|NoClassDefFoundError e) {
        					if(hasDisplayedError){
								hasDisplayedError = false;
								break;
							}
        					System.out.println("error processing: " + in);
        				}
						
					}
					continue;
					
				case "seed":	
					//is input valid - base check
					if (parameters.length > 2) {
						System.out.println("error processing: " + in);
					}
					else {
						Integer seedNum;
						try{
							seedNum = Integer.parseInt(parameters[1]);
							Critter.setSeed(seedNum);
						} catch (IndexOutOfBoundsException e) {
							System.out.println("error processing: " + in);
						} catch (NumberFormatException e) {
							System.out.println("error processing: " + in);
						}
					}
					continue;
					
				case "make":
					//is input valid - base check
					if (parameters.length == 1 || parameters.length > 3) {
						System.out.println("error processing: " + in);
					}
					else {
						Integer inputInt;
						String classname = parameters[1];
						if(parameters.length == 2) inputInt = 1;
						else{
							try{
								inputInt = Integer.parseInt(parameters[2]);
							} 
							catch (NumberFormatException e) {
								inputInt = 1;
							}
						}
						try {
							for (int i = 0; i < inputInt; i++) {
								Critter.makeCritter(classname);
								if(hasDisplayedError){
									hasDisplayedError = false;
									break;
								}
							}
						}
						catch (InvalidCritterException e) {
							System.out.println("error processing: " + in);
							break;
						}
						
					}
					continue;
				
				//if user wants to quit the program
				case "quit":
					//is input valid - base check
					if (parameters.length > 1) {
						System.out.println("error processing: " + in);
					}
					else {
						kb.close();
					}
					return;

				//if user wants to display the board
				case "show":
					//is input valid - base check
					if (parameters.length > 1) {
						System.out.println("Invalid command: " + in);
					}
					else {
						try{
							Critter.displayWorld();
						}
						catch(NullPointerException|NumberFormatException exception){
							System.out.println("error processing: " + in);
						}
					}
					continue;
	
				case "step":
					//is input valid - base check
					if (parameters.length > 2)
						System.out.println("error processing: " + in);
					else {
						Integer numSteps;
						try {
							numSteps = Integer.parseInt(parameters[1]);
							for (int i = 0; i < numSteps; i++) {
								if(hasDisplayedError){
									hasDisplayedError = false;
									break;
								}
								Critter.worldTimeStep();
							}
						}
						catch (IndexOutOfBoundsException e) {
							Critter.worldTimeStep();
						} 
						catch (NumberFormatException e ) {
							System.out.println("error processing: " + in);
						}
					}
					continue;
					
				default:
					System.out.println("Invalid command: " + in);	
				}
        		
        	}
        
        /* Write your code above */
    }
}