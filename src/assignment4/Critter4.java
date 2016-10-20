package assignment4;

/*
 * Sedentary Critter that rarely moves, but it is more likely move the lower it's energy
 * Will attempt run from a fight
 * if it cannot run, it will give up and choose not to fight
 */
public class Critter4 extends Critter{
	
	@Override
	public String toString(){return "4";}
	
	@Override
	public void doTimeStep() {
		int moveDec = Critter.getRandomInt(getEnergy());
		if(moveDec < 50) walk(Critter.getRandomInt(8));
		
	}

	@Override
	public boolean fight(String oponent) {
		run(Critter.getRandomInt(8));
		return false;
	}

}
