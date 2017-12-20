package Molecularize.src;

/*-----------------------------------------------------*\
	This class describes a molecule made of elements
\*----------------------------------------------------*/

public class Molecule {

	//-----------------[Constant declaration begins here]----------------//

	public static final String NUM_ELEMENTS_PREFIX = "M:";

	//------------------[Constant declaration ends here]-----------------//



	//------------------[Field declarations begin here]------------------//

	protected Molecule[] molecules;         //The array of different types of elements in the molecule
	protected int[]      moleculeCounts;    //The number of each element in the molecule
	protected int        numMolecules;      //The number of different elements in the molecule

	//-------------------[Field declarations end here]-------------------//



	//---------------------[Constructors begin here]---------------------//

	protected Molecule () {

		//Initialize fields to invalid values
		numMolecules    = -1;
		molecules       = null;
		moleculeCounts  = null;
	}

	//----------------------[Constructors end here]----------------------//



	//------------------[Molecule accessors begin here]------------------//

	//Get molecular formula
	public String getMolecularFormula () {
		StringBuilder output = new StringBuilder();

		//Loop through each molecule
		for (int i = 0; i < this.numMolecules; i++) {
			//Check if molecule is an element
			if (this.molecules[i] instanceof Element) {
				output.append(this.molecules[i].getMolecularFormula());
			} else {
				//Only output brackets if there is more than one of the same molecule
				if (this.moleculeCounts[i] > 1) {
					output.append("(");
				}

				output.append(this.molecules[i].getMolecularFormula());

				//Only output brackets if there is more than one of the same molecule
				if (this.moleculeCounts[i] > 1) {
					output.append(")");
				}
			}

			//Only output a number if there is more than one of the same molecule
			if (this.moleculeCounts[i] > 1) {
				output.append(this.moleculeCounts[i]);
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
		for (int i = 0; i < this.numMolecules; i++) {
			output += molecules[i].getMolarMass() * moleculeCounts[i];
		}

		return output;
	}

	//-------------------[Molecule accessors end here]-------------------//



	//--------------[Molecule validity checking begins here]-------------//

	public static boolean isValid (Molecule moleculeToCheck) {
		//Check if molecule is a single element
		if (moleculeToCheck instanceof Element) {
			return Element.isValid((Element)moleculeToCheck);
		}

		boolean isValid =
				moleculeToCheck                 != null  &&     //Check whether object exists
				moleculeToCheck.molecules       != null  &&     //Check whether element list exists
				moleculeToCheck.moleculeCounts  != null  &&     //Check whether element count array exists
				moleculeToCheck.numMolecules    >  0     &&     //Check whether the number of elements has been set
				moleculeToCheck.numMolecules    ==              //Check whether the size of the element array is the same
						moleculeToCheck.molecules.length &&     //as the number of elements
				moleculeToCheck.numMolecules    ==              //Check whether the size of the element count array is
						moleculeToCheck.moleculeCounts.length;  //the same as the number of elements

		if (!isValid) {
			return false;
		} else {
			for (int i = 0; i < moleculeToCheck.numMolecules && isValid; i++) {
				isValid &= isValid(moleculeToCheck.molecules[i]);
			}

			return isValid;
		}
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

			//Serialize number of unique molecules
			output.append("{" + NUM_ELEMENTS_PREFIX + moleculeToSerialize.numMolecules + "},{");

			//Serialize each molecule
			for (int i = 0; i < moleculeToSerialize.numMolecules; i++) {
				output.append("{");

				//Check if molecule is an element
				if (moleculeToSerialize.molecules[i] instanceof Element) {
					output.append(((Element)moleculeToSerialize.molecules[i]).getAtomicNumber() + "},{");
				} else {
					output.append(Molecule.serialize(moleculeToSerialize.molecules[i]) + "},{");
				}

				output.append(moleculeToSerialize.moleculeCounts[i] + "}");

				//Check if molecule is last in list
				if (i < moleculeToSerialize.numMolecules - 1) {
					output.append("},{");
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
			output.numMolecules   = Integer.parseInt(dataArray[0].substring(NUM_ELEMENTS_PREFIX.length()));
			output.molecules      = new Molecule[output.numMolecules];
			output.moleculeCounts = new int     [output.numMolecules];

			//Check for conflicting data array length
			if (output.numMolecules != dataArray.length - 1) {
				throw MoleculeDataException.create(
						MoleculeDataException.ExceptionType.INVALID_ELEMENT_FORMAT
				);
			}

			//Fill molecules array
			for (int i = 0; i < output.numMolecules; i++) {
				// Get list of molecule data
				String[] moleculeDataArray = parser.parse(dataArray[i + 1]);

				//Check for a properly formatted dataset
				if (moleculeDataArray.length != 2) {
					throw MoleculeDataException.create(
							MoleculeDataException.ExceptionType.INVALID_ELEMENT_FORMAT
					);
				} else {
					//Check for a nested molecule
					if (moleculeDataArray[0].charAt(0) == '{') {
						output.molecules[i] = Molecule.deserialize(moleculeDataArray[0], lookUpTable);;
					//Handle single elements
					} else {
						output.molecules[i] = lookUpTable.getElement(Integer.parseInt(moleculeDataArray[0]));

						//Check whether the element could be found
						if (output.molecules[i] == null) {
							throw MoleculeDataException.create(
									MoleculeDataException.ExceptionType.ELEMENT_NOT_FOUND
							);
						}
					}

					//Get molecule count
					try {
						output.moleculeCounts[i] = Integer.parseInt(moleculeDataArray[1]);

						//Check whether the element count is valid
						if (output.moleculeCounts[i] < 1) {
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
