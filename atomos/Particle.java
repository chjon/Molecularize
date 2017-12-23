package Molecularize.atomos;

/*-------------------------------------------------------------*\
	This class abstracts functionality for a general particle
\*-------------------------------------------------------------*/

public abstract class Particle {

	//------------------[Field declarations begin here]------------------//

	protected int charge;

	//-------------------[Field declarations end here]-------------------//



	//---------------------[Constructors begin here]---------------------//

	//Assume neutral particle
	public Particle () {
		this.charge = 0;
	}

	public Particle (int charge) {
		this.charge = charge;
	}

	//----------------------[Constructors end here]----------------------//



	//----------------------[Accessors begin here]-----------------------//

	public abstract double getMolarMass();
	public abstract String getMolecularFormula();

	public int getCharge () {
		return charge;
	}

	//Output molecular formula with charges if necessary
	@Override
	public String toString() {
		int charge = getCharge();

		//Only output square brackets if there is a charge
		if (charge != 0) {
			StringBuilder output = new StringBuilder();
			output.append('[').append(getMolecularFormula()).append(']');

			//Only output a number if the absolute value of the charge is greater than one
			if (Math.abs(charge) > 1) {
				output.append(Math.abs(charge));
			}

			//Determine the sign of the charge
			if (charge > 0) {
				output.append('+');
			} else {
				output.append('-');
			}

			return output.toString();

		//Only output molecular formula if particle is neutral
		} else {
			return this.getMolecularFormula();
		}
	}

	//-----------------------[Accessors end here]------------------------//



	//----------------------[Mutators begin here]------------------------//

	protected void setCharge (int charge) {
		this.charge = charge;
	}

	//-----------------------[Mutators end here]-------------------------//



	//-----------------[Comparison functions begin here]-----------------//

	//Check whether two particles are equal
	public abstract boolean equals (Particle pToCompare);

	//------------------[Comparison functions end here]------------------//
}
