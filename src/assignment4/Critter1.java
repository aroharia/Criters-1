package assignment4;

/*
 * Very simple custom critter, likes to make babies
 * This critter can walk or run
 * 10% chance of trying to reproduce, if it reproduces or even tries, it does not move
 * Has a 50/50 chance of wanting to fight another Critter
 */
public class Critter1 extends Critter {
	
	@Override
	public String toString() { return "1"; }

	@Override
	public void doTimeStep() {
		if(Critter.getRandomInt(10) == 9){
			reproduce(new Critter1(), Critter.getRandomInt(8));
		}
		else{
			int moveType = Critter.getRandomInt(2);
			if(moveType == 0) walk(Critter.getRandomInt(8));
			else run(Critter.getRandomInt(8));
		}
	}

	@Override
	public boolean fight(String oponent) {
		if(Critter.getRandomInt(2) == 0) return true;
		else return false;
	}

}
