package Molecularize.atomos;

/*----------------------------------------------------*\
	This class describes a compound made of elements
\*----------------------------------------------------*/

public class Molecule extends Particle {

	//-----------------[Constant declaration begins here]----------------//

	public static final String NUM_ELEMENTS_PREFIX = "M:";

	//------------------[Constant declaration ends here]-----------------//



	//------------------[Field declarations begin here]------------------//

	private Particle[] particles;       //The array of different types of particles in the molecule
	private int[]      particleCounts;  //The number of each particle in the molecule
	private int        numParticles;    //The number of different particle in the molecule

	//-------------------[Field declarations end here]-------------------//



	//---------------------[Constructors begin here]---------------------//

	private Molecule (int charge) {
		super(charge);

		//Initialize fields to invalid values
		numParticles    = -1;
		particles       = null;
		particleCounts  = null;
	}

	private Molecule () {
		super();

		//Initialize fields to invalid values
		numParticles    = -1;
		particles       = null;
		particleCounts  = null;
	}

	//Copy constructor (deep copy)
	public Molecule (Molecule source) {
		this.numParticles   = source.numParticles;
		this.particles      = new Particle[this.numParticles];
		this.particleCounts = new int     [this.numParticles];

		//Copy particles
		for (int i = 0; i < this.numParticles; i++) {

			//Check if particle is an element
			if (source.particles[i] instanceof Element) {
				this.particles[i] = new Element((Element)source.particles[i]);

			//Check if particle is a molecule
			} else if (source.particles[i] instanceof Molecule) {
				this.particles[i] = new Molecule((Molecule)source.particles[i]);

			//Copy by reference by default
			} else {
				this.particles[i] = source.particles[i];
			}

			this.particleCounts[i] = source.particleCounts[i];
		}
	}

	//----------------------[Constructors end here]----------------------//



	//------------------[Molecule accessors begin here]------------------//

	//Get molar mass
	public double getMolarMass () {
		double output = 0;

		//Loop through each constituent particle
		for (int i = 0; i < this.numParticles; i++) {
			output += particles[i].getMolarMass() * particleCounts[i];
		}

		return output;
	}

	//Get charge
	@Override
	public int getCharge () {
		int charge = 0;

		//Loop through each constituent particle
		for (int i = 0; i < this.numParticles; i++) {
			charge += particles[i].getCharge() * particleCounts[i];
		}

		return charge;
	}

	//-------------------[Molecule accessors end here]-------------------//



	//--------------[Molecule validity checking begins here]-------------//

	public static boolean isValid (Molecule mToCheck) {
		//Check base particle validity
		boolean isValid =
				mToCheck.particles != null &&            //Check whether element list exists
				mToCheck.particleCounts != null &&       //Check whether element count array exists
				mToCheck.numParticles > 0 &&             //Check whether the number of elements has been set
				mToCheck.numParticles ==                 //Check whether the size of the element array is the same
						mToCheck.particles.length &&     //as the number of elements
				mToCheck.numParticles ==                 //Check whether the size of the element count array is
						mToCheck.particleCounts.length;  //the same as the number of elements

		//Check whether base part
		if (!isValid) {
			return false;

		//Check nested particle validity
		} else {
			for (int i = 0; i < mToCheck.numParticles && isValid; i++) {
				//Check if constituent particle is a molecule
				if (mToCheck.particles[i] instanceof Molecule) {
					isValid &= isValid(
							(Molecule)mToCheck.particles[i]);

				//Check if constituent particle is an element
				} else if (mToCheck.particles[i] instanceof Element) {
					isValid &= Element.isValid(
							(Element)mToCheck.particles[i]);
				}
			}

			return isValid;
		}
	}

	//--------------[Molecule validity checking ends here]---------------//



	//----------------[Object serialization begins here]-----------------//

	//Get molecular formula
	public String getMolecularFormula () {
		StringBuilder output = new StringBuilder();

		//Loop through each constituent particle
		for (int i = 0; i < this.numParticles; i++) {

			//Check if particle is an element
			if (this.particles[i] instanceof Element) {
				output.append(this.particles[i].getMolecularFormula());

			//Check if particle is a molecule
			} else if (this.particles[i] instanceof Molecule) {
				//Only output brackets if there is more than one of the same molecule
				if (this.particleCounts[i] > 1) {
					output.append("(");
				}

				output.append(this.particles[i].getMolecularFormula());

				//Only output brackets if there is more than one of the same molecule
				if (this.particleCounts[i] > 1) {
					output.append(")");
				}
			}

			//Only output a number if there is more than one of the same constituent particle
			if (this.particleCounts[i] > 1) {
				output.append(this.particleCounts[i]);
			}
		}

		return output.toString();
	}

	//Get molecule from molecular formula
	public static Molecule fromMolecularFormula (String molecularFormula, PeriodicTable lookUpTable) throws
			MoleculeDataException {

		final char OPEN_BRACKET    = '(';
		final char CLOSE_BRACKET   = ')';
		int openBracketCount       = 0;
		int numberOfMolecules      = 0;
		char curChar               = 0;
		boolean foundStartOfSymbol = false;

		//Remove spaces from molecularFormula
		molecularFormula = molecularFormula.replaceAll(" ", "");

		//Check for an invalid length
		if (molecularFormula.length() == 0) {
			throw MoleculeDataException.create(
					MoleculeDataException.ExceptionType.INVALID_MOLECULE_FORMAT
			);
		}

		//Check for an invalid start character
		curChar = molecularFormula.charAt(0);

		if (curChar != '(' && (curChar < 'A' || curChar > 'Z')) {
			throw MoleculeDataException.create(
					MoleculeDataException.ExceptionType.INVALID_MOLECULE_FORMAT
			);
		}

		//Count number of nested molecules
		for (int i = 0; i < molecularFormula.length(); i++) {
			curChar = molecularFormula.charAt(i);

			//Check for open bracket
			if (curChar == OPEN_BRACKET) {
				openBracketCount++;

			//Check for close bracket
			} else if (curChar == CLOSE_BRACKET) {

				//Check for invalid bracket placement
				if (openBracketCount < 1) {
					throw MoleculeDataException.create(
							MoleculeDataException.ExceptionType.INVALID_MOLECULE_FORMAT
					);
				}

				openBracketCount--;

				//Increment number of molecules
				if (openBracketCount == 0) {
					numberOfMolecules++;
				}

			//Only check for other characters if brackets are closed
			} else if (openBracketCount == 0) {

				//Check for the beginning of a symbol
				if (curChar >= 'A' && curChar <= 'Z') {
					numberOfMolecules++;
					foundStartOfSymbol = true;

				//Check for invalid symbol
				} else if (curChar >= 'a' && curChar <= 'z') {
					if (!foundStartOfSymbol) {
						throw MoleculeDataException.create(
								MoleculeDataException.ExceptionType.INVALID_MOLECULE_FORMAT
						);
					}

				//Check for numbers
				} else if (curChar >= '0' && curChar < '9') {
					foundStartOfSymbol = false;

				//Check for invalid characters
				} else {
					throw MoleculeDataException.create(
							MoleculeDataException.ExceptionType.INVALID_MOLECULE_FORMAT
					);
				}
			}
		}

		//Check for bracket imbalance
		if (openBracketCount != 0) {
			throw MoleculeDataException.create(
					MoleculeDataException.ExceptionType.BRACKET_IMBALANCE
			);

		//Check for invalid number of molecules
		} else if (numberOfMolecules < 1) {
			throw MoleculeDataException.create(
					MoleculeDataException.ExceptionType.INVALID_MOLECULE_FORMAT
			);
		}

		//Create molecule for output
		Molecule output       = new Molecule();
		output.numParticles   = numberOfMolecules;
		output.particles      = new Particle[output.numParticles];
		output.particleCounts = new int     [output.numParticles];

		foundStartOfSymbol = false;
		openBracketCount       = 0;
		int moleculeIndex      = 0;
		int moleculeStartIndex = -1;
		int moleculeCount      = 0;

		//Get all molecules
		for (int i = 0; i < molecularFormula.length(); i++) {
			curChar = molecularFormula.charAt(i);

			//Handle open brackets
			if (curChar == OPEN_BRACKET) {

				if (openBracketCount == 0) {
					//Check for preceding molecule
					if (!foundStartOfSymbol) {
						output.particleCounts[Math.max(0, moleculeIndex - 1)] = Math.max(1, moleculeCount);

						//Check for preceding element
					} else {
						output.particles[moleculeIndex] =
								lookUpTable.getElementBySymbol(molecularFormula.substring(moleculeStartIndex, i));

						//Check whether element could be found
						if (output.particles[moleculeIndex] == null) {
							throw MoleculeDataException.create(
									MoleculeDataException.ExceptionType.ELEMENT_NOT_FOUND
							);
						}

						output.particleCounts[moleculeIndex] = Math.max(1, moleculeCount);
						moleculeIndex++;
					}

					foundStartOfSymbol = false;
					moleculeStartIndex = i + 1;
				}

				openBracketCount++;
				moleculeCount = 0;

			//Handle close brackets
			} else if (curChar == CLOSE_BRACKET) {
				openBracketCount--;

				//Handle end of molecule
				if (openBracketCount == 0) {

					//Handle empty brackets
					if (moleculeStartIndex >= i) {
						throw MoleculeDataException.create(
								MoleculeDataException.ExceptionType.INVALID_MOLECULE_FORMAT
						);
					}

					output.particles[moleculeIndex] =
							fromMolecularFormula(molecularFormula.substring(moleculeStartIndex, i), lookUpTable);
					output.particleCounts[moleculeIndex] = 1;
					moleculeIndex++;
					moleculeStartIndex = -1;
				}

			//Handle other characters only if brackets are not open
			} else if (openBracketCount == 0) {

				//Handle numbers
				if (curChar >= '0' && curChar <= '9') {

					//Find preceding element
					if (foundStartOfSymbol) {
						output.particles[moleculeIndex] =
								lookUpTable.getElementBySymbol(molecularFormula.substring(moleculeStartIndex, i));

						//Check whether element could be found
						if (output.particles[moleculeIndex] == null) {
							throw MoleculeDataException.create(
									MoleculeDataException.ExceptionType.ELEMENT_NOT_FOUND
							);
						}

						output.particleCounts[moleculeIndex] = 1;
						moleculeIndex++;
						moleculeStartIndex = -1;
						foundStartOfSymbol = false;
					}

					moleculeCount = (moleculeCount * 10) + (curChar - '0');

				//Handle beginning of symbol
				} else if (curChar >= 'A' && curChar <= 'Z') {

					//Check for preceding molecule
					if (!foundStartOfSymbol) {
						output.particleCounts[Math.max(0, moleculeIndex - 1)] = Math.max(1, moleculeCount);

					//Check for preceding element
					} else {
						output.particles[moleculeIndex] =
								lookUpTable.getElementBySymbol(molecularFormula.substring(moleculeStartIndex, i));

						//Check whether element could be found
						if (output.particles[moleculeIndex] == null) {
							throw MoleculeDataException.create(
									MoleculeDataException.ExceptionType.ELEMENT_NOT_FOUND
							);
						}

						output.particleCounts[moleculeIndex] = Math.max(1, moleculeCount);
						moleculeIndex++;
					}

					foundStartOfSymbol = true;
					moleculeStartIndex = i;
					moleculeCount = 0;
				}
			}
		}

		if (moleculeCount > 1) {
			output.particleCounts[Math.max(0, moleculeIndex - 1)] = moleculeCount;
		} else if (foundStartOfSymbol) {
			output.particles[moleculeIndex] =
					lookUpTable.getElementBySymbol(molecularFormula.substring(moleculeStartIndex));

			//Check whether element could be found
			if (output.particles[moleculeIndex] == null) {
				throw MoleculeDataException.create(
						MoleculeDataException.ExceptionType.ELEMENT_NOT_FOUND
				);
			}

			output.particleCounts[moleculeIndex] = Math.max(1, moleculeCount);
		}

		return output;
	}

	//Serialize molecule data
	public static String serialize (Molecule moleculeToSerialize) {

		//Check whether the molecule is valid and serializable
		if (!isValid(moleculeToSerialize)) {
			return null;
		} else {
			StringBuilder output = new StringBuilder();

			//Serialize number of unique molecules
			output.append("{" + NUM_ELEMENTS_PREFIX + moleculeToSerialize.numParticles + "},{");

			//Serialize each molecule
			for (int i = 0; i < moleculeToSerialize.numParticles; i++) {
				output.append("{");

				//Check if particle is an element
				if (moleculeToSerialize.particles[i] instanceof Element) {
					output.append(((Element)moleculeToSerialize.particles[i]).getAtomicNumber() + "},{");

				//Check if particle is a molecule
				} else if (moleculeToSerialize.particles[i] instanceof Molecule) {
					output.append(Molecule.serialize(((Molecule)moleculeToSerialize.particles[i])) + "},{");

				//Reject if particle is neither an element nor a molecule
				} else {
					return null;
				}

				//Output charge and particle count
				output.append(moleculeToSerialize.particles[i].getCharge() + "},{");
				output.append(moleculeToSerialize.particleCounts[i]        + "}");

				//Check if molecule is last in list
				if (i < moleculeToSerialize.numParticles - 1) {
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
			Molecularize.DataFormatException, MoleculeDataException {

		Molecularize.DataParser parser = new Molecularize.DataParser();

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
			output.numParticles   = Integer.parseInt(dataArray[0].substring(NUM_ELEMENTS_PREFIX.length()));
			output.particles      = new Particle[output.numParticles];
			output.particleCounts = new int     [output.numParticles];

			//Check for conflicting data array length
			if (output.numParticles != dataArray.length - 1) {
				throw MoleculeDataException.create(
						MoleculeDataException.ExceptionType.INVALID_ELEMENT_FORMAT
				);
			}

			//Fill molecules array
			for (int i = 0; i < output.numParticles; i++) {
				// Get list of molecule data
				String[] moleculeDataArray = parser.parse(dataArray[i + 1]);

				//Check for a properly formatted dataset
				if (moleculeDataArray.length != 3) {
					throw MoleculeDataException.create(
							MoleculeDataException.ExceptionType.INVALID_ELEMENT_FORMAT
					);
				} else {
					//Check for a nested molecule
					if (moleculeDataArray[0].charAt(0) == '{') {
						output.particles[i] = Molecule.deserialize(moleculeDataArray[0], lookUpTable);
					//Handle single elements
					} else {
						output.particles[i] = lookUpTable.getElement(Integer.parseInt(moleculeDataArray[0]));

						//Check whether the element could be found
						if (output.particles[i] == null) {
							throw MoleculeDataException.create(
									MoleculeDataException.ExceptionType.ELEMENT_NOT_FOUND
							);
						}
					}

					//Get particle charge
					try {
						int charge = Integer.parseInt(moleculeDataArray[1]);

						//Copy particle if there is a charge and the particle is an element
						if (output.particles[i] instanceof Element && charge != 0) {
							output.particles[i] = new Element((Element)output.particles[i]);
						}

						output.particles[i].setCharge(Integer.parseInt(moleculeDataArray[1]));

					//Handle invalid integer
					} catch (NumberFormatException e) {
						throw MoleculeDataException.create(
								MoleculeDataException.ExceptionType.INVALID_ELEMENT_DATA
						);
					}

					//Get particle count
					try {
						output.particleCounts[i] = Integer.parseInt(moleculeDataArray[2]);

						//Check whether the element count is valid
						if (output.particleCounts[i] < 1) {
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

	//-----------------[Object serialization ends here]------------------//



	//-----------------[Comparison functions begin here]----------------//

	//Check whether two molecules are equal
	public boolean equals (Particle pToCompare) {

		//Check whether the molecule to compare is null
		if (pToCompare == null) {
			return false;
		}

		//Check for a type-match
		if (!(pToCompare instanceof Molecule)) {
			return false;
		}

		Molecule mToCompare = (Molecule)pToCompare;

		//Check if the number of particles in each molecule are equal
		if (this.numParticles != mToCompare.numParticles) {
			return false;
		}

		//Loop through each constituent particle
		for (int i = 0; i < this.numParticles; i++) {
			if (!this.particles[i].equals(mToCompare.particles[i])) {
				return false;
			}
		}

		return true;
	}

	//------------------[Comparison functions end here]-----------------//
}
