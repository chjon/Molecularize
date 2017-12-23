package Molecularize.atomos;

/*------------------------------------------------------------------*\
	This class stores data about an element of the periodic table.
\*------------------------------------------------------------------*/

import Molecularize.DataParser;

public class Element extends Particle {

	//------------------[Field declarations begin here]------------------//

	private int    atomicNumber;        //The element's atomic number
	private int    group;               //The element's group number
	private int    period;              //The element's period number
	private double molarMass;           //The element's molar mass
	private String name;                //The element's name
	private String symbol;              //The element's chemical symbol

	//-------------------[Field declarations end here]-------------------//



	//---------------------[Constructors begin here]---------------------//

	private Element (int charge, int atomicNumber, int group, int period, int molarMass, String name, String symbol) {
		super(charge);

		//Initialize variables to invalid values
		this.atomicNumber = atomicNumber;
		this.group        = group;
		this.period       = period;
		this.molarMass    = molarMass;
		this.name         = name;
		this.symbol       = symbol;
	}

	private Element () {
		super();

		//Initialize variables to invalid values
		this.atomicNumber = -1;
		this.group        = -1;
		this.period       = -1;
		this.molarMass    = -1;
		this.name         = "";
		this.symbol       = "";
	}

	//Copy constructor (deep copy)
	public Element (Element source) {
		this.charge       = source.getCharge();
		this.atomicNumber = source.getAtomicNumber();
		this.group        = source.getGroup();
		this.period       = source.getPeriod();
		this.molarMass    = source.getMolarMass();
		this.name         = source.getName();
		this.symbol       = source.getSymbol();
	}

	//----------------------[Constructors end here]----------------------//



	//----------------------[Accessors begin here]------------------------//

	public int getAtomicNumber () {
		return atomicNumber;
	}

	public int getGroup () {
		return group;
	}

	public int getPeriod () {
		return period;
	}

	public double getMolarMass () {
		return molarMass;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	//-----------------------[Accessors end here]-----------------------//



	//Helper function for setName and setSymbol: check whether all characters are letters
	private static boolean allLetters (String s) {
		s = s.toLowerCase();

		//Iterate through all letters
		for (int i = 0; i < s.length(); i++) {
			char curChar = s.charAt(i);

			if (curChar < 'a' || curChar > 'z') {
				return false;
			}
		}

		return true;
	}



	//----------------------[Mutators begin here]-----------------------//

	//Set the particle's charge
	public void setCharge (int charge) {
		this.charge = charge;
	}

	//Set the element's atomic number
	private void setAtomicNumber (int atomicNumber) throws ElementDataException {
		//Check for a valid atomic number
		if (atomicNumber > 0) {
			this.atomicNumber = atomicNumber;
		} else {
			throw ElementDataException.create(
					"" + atomicNumber,
					ElementDataException.ExceptionType.INVALID_ATOMIC_NUMBER
			);
		}
	}

	//Set the element's group
	private void setGroup (int group) throws ElementDataException {
		//Check for a valid group
		if (group > 0) {
			this.group = group;
		} else {
			throw ElementDataException.create(
					"" + group,
					ElementDataException.ExceptionType.INVALID_GROUP_NUMBER
			);
		}
	}

	//Set the element's period
	private void setPeriod (int period) throws ElementDataException {
		//Check for a valid period
		if (period > 0) {
			this.period = period;
		} else {
			throw ElementDataException.create(
					"" + period,
					ElementDataException.ExceptionType.INVALID_PERIOD_NUMBER
			);
		}
	}

	//Set the element's molar mass
	private void setMolarMass(double molarMass) throws ElementDataException {
		//Check for a valid molar mass
		if (molarMass > 0) {
			this.molarMass = molarMass;
		} else {
			throw ElementDataException.create(
					"" + molarMass,
					ElementDataException.ExceptionType.INVALID_MOLAR_MASS
			);
		}
	}

	//Set the element's name
	private void setName(String name) throws ElementDataException {
		//Check for a valid name
		if (name.length() != 0 && allLetters(name)) {

			//Ensure that name is lowercase
			this.name = name.toLowerCase();
		} else {
			throw ElementDataException.create(
					name,
					ElementDataException.ExceptionType.INVALID_NAME
			);
		}
	}

	//Set the element's chemical symbol
	private void setSymbol(String symbol) throws ElementDataException {
		//Check for a valid symbol
		if (symbol.length() != 0 && allLetters(symbol)) {

			//Ensure that only the first letter of the symbol is uppercase
			symbol = symbol.toLowerCase();
			this.symbol = (char) (symbol.charAt(0) + ('A' - 'a')) + symbol.substring(1);
		} else {
			throw ElementDataException.create(
					"" + symbol,
					ElementDataException.ExceptionType.INVALID_SYMBOL
			);
		}
	}

	//-----------------------[Mutators end here]------------------------//



	//--------------[Element validity checking begins here]-------------//

	public static boolean isValid (Element elementToSerialize) {
		return
				elementToSerialize                      != null &&      //Check whether object exists
				elementToSerialize.getAtomicNumber()    > 0     &&      //Check for a valid atomic number
				elementToSerialize.getGroup()           > 0     &&      //Check for a valid group
				elementToSerialize.getPeriod()          > 0     &&      //Check for a valid period
				elementToSerialize.getMolarMass()       > 0     &&      //Check for a valid molar mass
				elementToSerialize.getName().length()   != 0    &&      //Check for a valid name
				elementToSerialize.getSymbol().length() != 0;           //Check for a valid symbol
	}

	//---------------[Element validity checking ends here]--------------//



	//----------------[Object serialization begins here]----------------//

	//Get atomic symbol for output
	public String getMolecularFormula() {
		return getSymbol();
	}

	//Serialize element data
	public static String serialize (Element elementToSerialize) {

		//Check whether the element is valid and serializable
		if (!isValid(elementToSerialize)) {
			return null;
		} else {
			return "{" +
					elementToSerialize.getAtomicNumber() + "},{" +
					elementToSerialize.getGroup()        + "},{" +
					elementToSerialize.getPeriod()       + "},{" +
					elementToSerialize.getMolarMass()    + "},{" +
					elementToSerialize.getName()         + "},{" +
					elementToSerialize.getSymbol()       + "}";
		}
	}

	//Deserialize element data
	public static Element deserialize (String serializedData) throws
			Molecularize.DataFormatException, ElementDataException {

		DataParser parser = new DataParser();

		//Get list of separated data
		String[] dataArray = parser.parse(serializedData);

		//Check for the expected data length
		if (dataArray.length != 6) {
			throw ElementDataException.create(
					"" + dataArray.length,
					ElementDataException.ExceptionType.INVALID_LENGTH
			);
		}

		//Trim preceding and succeeding whitespace from each datum
		for (int i = 0; i < dataArray.length; i++) {
			dataArray[i] = dataArray[i].trim();
		}

		Element newElement = new Element();

		//Set atomic number
		try {
			newElement.setAtomicNumber(Integer.parseInt(dataArray[0]));
		} catch (NumberFormatException e) {
			throw ElementDataException.create(
					"" + dataArray[0],
					ElementDataException.ExceptionType.INVALID_ATOMIC_NUMBER
			);
		}

		//Set group number
		try {
			newElement.setGroup(Integer.parseInt(dataArray[1]));
		} catch (NumberFormatException e) {
			throw ElementDataException.create(
					"" + dataArray[1],
					ElementDataException.ExceptionType.INVALID_GROUP_NUMBER
			);
		}

		//Set period number
		try {
			newElement.setPeriod(Integer.parseInt(dataArray[2]));
		} catch (NumberFormatException e) {
			throw ElementDataException.create(
					"" + dataArray[2],
					ElementDataException.ExceptionType.INVALID_PERIOD_NUMBER
			);
		}

		//Set molar mass
		try {
			newElement.setMolarMass(Double.parseDouble(dataArray[3]));
		} catch (NumberFormatException e) {
			throw ElementDataException.create(
					"" + dataArray[3],
					ElementDataException.ExceptionType.INVALID_MOLAR_MASS
			);
		}

		//Set name
		newElement.setName  (dataArray[4]);

		//Set symbol
		newElement.setSymbol(dataArray[5]);

		return newElement;
	}

	//-----------------[Object serialization ends here]-----------------//



	//-----------------[Comparison functions begin here]----------------//

	//Check whether two elements are equal
	public boolean equals (Particle pToCompare) {

		//Check whether the element to compare is null
		if (pToCompare == null) {
			return false;
		}

		//Check for a type-match
		if (!(pToCompare instanceof Element)) {
			return false;
		}

		Element eToCompare = (Element)pToCompare;

		return (this.getCharge      () == eToCompare.getCharge              ()  &&
				this.getAtomicNumber() == eToCompare.getAtomicNumber        ()  &&
				this.getGroup       () == eToCompare.getGroup               ()  &&
				this.getPeriod      () == eToCompare.getPeriod              ()  &&
				this.getMolarMass   () == eToCompare.getMolarMass           ()  &&
				this.getName        ().equalsIgnoreCase(eToCompare.getName  ()) &&
				this.getSymbol      ().equalsIgnoreCase(eToCompare.getSymbol())
		);
	}

	//Check whether two elements have the same identifying information
	public boolean conflicts (Element toCompare) {

		//Check whether the element to compare is null
		if (toCompare == null) {
			return false;
		}

		return (this.getAtomicNumber() == toCompare.getAtomicNumber        ()  ||
				this.getName        ().equalsIgnoreCase(toCompare.getName  ()) ||
				this.getSymbol      ().equalsIgnoreCase(toCompare.getSymbol())
		);
	}

	//------------------[Comparison functions end here]-----------------//
}