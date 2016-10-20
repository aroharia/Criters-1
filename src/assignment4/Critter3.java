package assignment4;

/*
 * This Critter only likes to move diagonally or in standard up, down, left or right. Opposite of how parent moved
 * low confidence Critter. It assumes it will lose any fight and attempts to shoot out a baby before even deciding to fight
 * has a 50/50 chance to fight
 * 
 */
public class Critter3 extends Critter {
	int moveType;
	
	public Critter3(){
		moveType = 1;
	}
	public Critter3(int mt){
		if (mt == 0) moveType = 0;
		else moveType = 1;
	}
	@Override
	public String toString(){ return "3"; }
	@Override
	public void doTimeStep() {
		int decision = (2 * Critter.getRandomInt(4)) + moveType;
		walk(decision);
		
	}

	@Override
	public boolean fight(String oponent) {
		reproduce(new Critter3(this.moveType % 1), Critter.getRandomInt(8));
		
		if(Critter.getRandomInt(2) == 1) return true;
		return false;
	}
	
}
