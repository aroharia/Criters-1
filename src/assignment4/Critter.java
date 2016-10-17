/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * <Ashvin Roharia>
 * <ar34426>
 * <16475>
 * <Ram Muthukumar>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4;

import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	private boolean hasMoved; // true if Critter has moved in this time step
	
	private final void move(int direction, int distance) {
		switch (direction) {
			//right
			case 0: this.x_coord = this.x_coord + distance;
			break;
			
			//down-right
			case 1: this.x_coord = this.x_coord + distance;
			this.y_coord = this.y_coord - distance;
			break;
			
			//down
			case 2: this.y_coord = this.y_coord - distance;
			break;
			
			//down-left
			case 3: this.x_coord = this.x_coord - distance;
			this.y_coord = this.y_coord - distance;
			break;
			
			//left
			case 4: this.x_coord = this.x_coord - distance;
			break;
			
			//up-left
			case 5: this.x_coord = this.x_coord - distance;
			this.y_coord = this.y_coord + distance;
			break;
			
			//up
			case 6: this.y_coord = this.y_coord + distance;
			break;
			
			//up-right
			case 7: this.x_coord = this.x_coord + distance;
			this.y_coord = this.y_coord + distance;
			break;
			
			//error
			default: break;
		}
		
		if (this.x_coord > Params.world_width - 1) { // relocate to left side
			this.x_coord = this.x_coord - Params.world_width;
		}
		else if (this.x_coord < 0) { // relocate to right side
			this.x_coord = this.x_coord + Params.world_width;
		}
		if (this.y_coord > Params.world_height - 1) { // relocate to top
			this.y_coord = this.y_coord - Params.world_height;
		}
		else if (this.y_coord < 0) { // relocate to bottom
			this.y_coord = this.y_coord + Params.world_height;
		}
	}
	
	/**
	 * Critters moves a distance of one in the given direction
	 * @param direction is the direction the critter will walk
	 */
	protected final void walk(int direction) {
		this.energy = this.energy - Params.walk_energy_cost;
		if (hasMoved)
			return;
		this.move(direction, 1);
		hasMoved = true;
	}
	
	/**
	 * Critters moves a distance of one in the given direction
	 * @param direction is the direction the critter will run
	 */
	protected final void run(int direction) {
		this.energy = this.energy - Params.run_energy_cost;
		if (hasMoved)
			return;
		this.move(direction, 2);
		hasMoved = true;
		}
	
	protected final void reproduce(Critter offspring, int direction) {
		
		if (this.energy < Params.min_reproduce_energy)
			return;
		
		offspring.energy = this.energy / 2;
		this.energy = (int) Math.ceil(this.energy /2);
		
		//puts baby next to parent
		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;
		offspring.move(direction, 1);
		babies.add(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		
		try{
			//adds input to the world if its valid
			Critter c = (Critter) Class.forName(myPackage + "." + critter_class_name).newInstance();
			Critter.population.add(c);
			
			//initializations
			c.energy=Params.start_energy;
			c.x_coord = Critter.getRandomInt(Params.world_width);
			c.y_coord = Critter.getRandomInt(Params.world_height);
		}
		catch(ClassNotFoundException|InstantiationException|IllegalAccessException|NoClassDefFoundError e){
			System.out.println("error processing: " + Main.in);
			Main.hasDisplayedError = true; //so that error isn't displayed multiple times
		}
		
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		Class<?> critterClass = null;

		try {
			critterClass = Class.forName(critter_class_name);
		} 
		catch (ClassNotFoundException|NoClassDefFoundError e) {
			System.out.println("error processing: " + Main.in);
		}

		for (Critter a : population) {
			if (critterClass.isInstance(a)) {
				result.add(a);
			}
		}
		
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
		babies.clear();
	}
	
	public static void worldTimeStep() {
		
		//TO DO - big!
		
	}
	
	public static void displayWorld() {
		
		String[][] critterWorld = new String[Params.world_height+2][Params.world_width+2]; 
		// iterate through entire grid
		for (int height = 0; height < Params.world_height+2; height++ ) {
			for (int width = 0; width < Params.world_width+2; width++ ) {
				//if you're on the horizontal border
				if (height == 0 || height == Params.world_height+1) {
					//if you're on the corner's of the horizontal border
					if (width == 0 || width == Params.world_width+1) {
						critterWorld[height][width] = "+";
						continue;
					}
					//if you're on the bar for the horizontal border
					else 
						critterWorld[height][width] = "-";
				}
				
				//if you're on the vertical border (corners have already been drawn)
				else if (width == 0 || width == Params.world_width+1)
					critterWorld[height][width] = "|";
				
				//blanks for everything within the border
				else 
					critterWorld[height][width] = " ";
			}
		}
		
		//fill in the world with alive creatures
		for (Critter crit : population) {
			critterWorld[crit.y_coord + 1][crit.x_coord + 1] = crit.toString(); //offset by 1 because of top border
		}
		
		//iterate through the entire grid and prints the border, corners, blank spaces, and critters
		for (int height = 0; height < Params.world_height+2; height++) {
			for (int width = 0; width < Params.world_width+2; width++) {
				System.out.print(critterWorld[height][width]);
			}
			System.out.print("\n"); //go to the next line, to print next row
		}
		
	}
}