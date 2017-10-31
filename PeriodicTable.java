package Molecularize;

public class PeriodicTable {

	//-----------------[Constant declaration begins here]----------------//

	public static final int NUM_ELEMENTS = 118;

	//------------------[Constant declaration ends here]-----------------//



	//-----------------[Variable declaration begins here]----------------//

	private Element[] elements;

	//------------------[Variable declaration ends here]-----------------//



	//---------------------[Constructors begin here]---------------------//

	public PeriodicTable () {
		elements = new Element[NUM_ELEMENTS];
	}

	//----------------------[Constructors end here]----------------------//



	//----------------------[Accessors begin here]-----------------------//

	//Get an element based on atomic number
	public Element getElement (int atomicNumber) {

		//Check for a valid atomic number input
		if (atomicNumber > 0 && atomicNumber <= NUM_ELEMENTS) {
			return elements[atomicNumber - 1];
		}

		return null;
	}

	//Get an element based on name
	public Element getElementByName (String name) {
		for (int i = 0; i < NUM_ELEMENTS; i++) {
			if (elements[i] != null && elements[i].getName().equalsIgnoreCase(name)) {
				return elements[i];
			}
		}

		return null;
	}

	//Get an element based on symbol
	public Element getElementBySymbol (String symbol) {
		for (int i = 0; i < NUM_ELEMENTS; i++) {
			if (elements[i] != null && elements[i].getSymbol().equals(symbol)) {
				return elements[i];
			}
		}

		return null;
	}

	//-----------------------[Accessors end here]------------------------//



	//-----------------------[Mutators begin here]-----------------------//

	//Check if an element conflicts with an element already in the table
	public boolean conflicts (Element checkElement) {

		//Iterate through each element in the table
		for (int i = 0; i < NUM_ELEMENTS; i++) {

			//Check for a conflict between the elements
			if (checkElement.conflicts(elements[i])) {
				return true;
			}
		}

		return false;
	}

	//Add an element to the table
	public void add (Element toAdd) {
		int atomicNumber = toAdd.getAtomicNumber();

		//Check whether an element with the same information already exists
		if (!conflicts(toAdd)) {
			elements[atomicNumber] = toAdd;
		}
	}

	//Remove an element from the table
	public void remove (int atomicNumber) {

		//Check for a valid atomic number input
		if (atomicNumber > 0 && atomicNumber <= NUM_ELEMENTS) {
			elements[atomicNumber - 1] = null;
		}
	}

	//------------------------[Mutators end here]------------------------//
}
