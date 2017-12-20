package Molecularize.src;

/*-----------------------------------------------------*\
	This class describes a molecule made of elements
\*----------------------------------------------------*/

public class Molecule {

	//-----------------[Constant declaration begins here]----------------//

	public static final String NUM_ELEMENTS_PREFIX = "M:";

	//------------------[Constant declaration ends here]-----------------//



	//------------------[Field declarations begin here]------------------//

	private Element[] elements;         //The array of different types of elements in the molecule
	private int[]     elementCounts;    //The number of each element in the molecule
	private int       numElements;      //The number of different elements in the molecule

	//-------------------[Field declarations end here]-------------------//



	//---------------------[Constructors begin here]---------------------//

	private Molecule () {

		//Initialize fields to invalid values
		numElements   = -1;
		elements      = null;
		elementCounts = null;
	}

	//----------------------[Constructors end here]----------------------//



	//------------------[Molecule accessors begin here]------------------//

	//Get molecular formula
	public String getMolecularFormula () {
		StringBuilder output = new StringBuilder();

		//Loop through each element
		for (int i = 0; i < this.numElements; i++) {
			output.append(this.elements     [i].getSymbol());

			//Only output a number if there is more than one of the same element
			if (this.elementCounts[i] > 1) {
				output.append(this.elementCounts[i]);
			}
		}

		return output.toString();
	}

	//Alias for molecularFormula()
	public String toString () {
		return getMolecularFormula();
	}

	//Get molar mass
	public double getMolarMass () {
		double output = 0;

		//Loop through each element
		for (int i = 0; i < this.numElements; i++) {
			output += elements[i].getMolarMass() * elementCounts[i];
		}

		return output;
	}

	//-------------------[Molecule accessors end here]-------------------//



	//--------------[Molecule validity checking begins here]-------------//

	public static boolean isValid (Molecule moleculeToCheck) {
		return	moleculeToCheck                 != null &&     //Check whether object exists
				moleculeToCheck.elements        != null &&     //Check whether element list exists
				moleculeToCheck.elementCounts   != null &&     //Check whether element count array exists
				moleculeToCheck.numElements     >  0    &&     //Check whether the number of elements has been set
				moleculeToCheck.numElements     ==             //Check whether the size of the element array is the same
						moleculeToCheck.elements.length &&     //as the number of elements
				moleculeToCheck.numElements     ==             //Check whether the size of the element count array is
						moleculeToCheck.elementCounts.length;  //the same as the number of elements
	}

	//--------------[Molecule validity checking ends here]--------------//



	//----------------[Object serialization begins here]----------------//

	//Serialize molecule data
	public static String serialize (Molecule moleculeToSerialize) {

		//Check whether the molecule is valid and serializable
		if (!isValid(moleculeToSerialize)) {
			return null;
		} else {
			StringBuilder output = new StringBuilder();

			//Serialize number of unique elements
			output.append("{" + NUM_ELEMENTS_PREFIX + moleculeToSerialize.numElements + "},{");

			//Serialize each element
			for (int i = 0; i < moleculeToSerialize.numElements; i++) {
				output.append("{");
				output.append(moleculeToSerialize.elements     [i].getAtomicNumber() + "},{");
				output.append(moleculeToSerialize.elementCounts[i] + "}");

				if (i < moleculeToSerialize.numElements - 1) {
					output.append(",");
				} else {
					output.append("}");
				}
			}

			return output.toString();
		}
	}

	//Deserialize molecule data
	public static Molecule deserialize (String serializedData, PeriodicTable lookUpTable) throws
			DataFormatException, MoleculeDataException {

		DataParser parser = new DataParser();

		//Get list of separated data
		String[] dataArray = parser.parse(serializedData);

		//Check for a properly formatted dataset
		if (!dataArray[0].startsWith(NUM_ELEMENTS_PREFIX)) {
			throw MoleculeDataException.create(
					MoleculeDataException.ExceptionType.NUM_ELEMENTS_NOT_FOUND
			);
		} else {
			//Create molecule object for storing data
			Molecule output = new Molecule();

			//Create arrays of the correct size
			output.numElements   = Integer.parseInt(dataArray[0].substring(NUM_ELEMENTS_PREFIX.length()));
			output.elements      = new Element[output.numElements];
			output.elementCounts = new int    [output.numElements];

			//Check for conflicting data array length
			if (output.numElements != dataArray.length - 1) {
				throw MoleculeDataException.create(
						MoleculeDataException.ExceptionType.INVALID_ELEMENT_FORMAT
				);
			}

			//Fill elements array
			for (int i = 0; i < output.numElements; i++) {
				// Get list of element data
				String[] elementDataArray = parser.parse(dataArray[i + 1]);

				//Check for a properly formatted dataset
				if (elementDataArray.length != 2) {
					throw MoleculeDataException.create(
							MoleculeDataException.ExceptionType.INVALID_ELEMENT_FORMAT
					);
				} else {
					try {
						output.elements[i] = lookUpTable.getElement(Integer.parseInt(elementDataArray[0]));
						output.elementCounts[i] = Integer.parseInt(elementDataArray[1]);

						//Check whether the element could be found
						if (output.elements[i] == null) {
							throw MoleculeDataException.create(
									MoleculeDataException.ExceptionType.ELEMENT_NOT_FOUND
							);

						//Check whether the element count is valid
						} else if (output.elementCounts[i] < 1) {
							throw MoleculeDataException.create(
									MoleculeDataException.ExceptionType.INVALID_ELEMENT_DATA
							);
						}
					//Handle invalid integer
					} catch (NumberFormatException e) {
						throw MoleculeDataException.create(
								MoleculeDataException.ExceptionType.INVALID_ELEMENT_DATA
						);
					}
				}
			}

			return output;
		}
	}

	//-----------------[Object serialization ends here]-----------------//
}
