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

	//-----------------------[Accessors end here]------------------------//
}
