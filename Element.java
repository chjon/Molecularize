package Molecularize;

/*------------------------------------------------------------------*\
	This class stores data about an element of the periodic table.
\*------------------------------------------------------------------*/

public class Element {
	//------------------[Field declarations begin here]------------------//

	private int    atomicNumber;        //The element's atomic number
	private int    group;               //The element's group number
	private int    period;              //The element's period number
	private double molarMass;           //The element's molar mass
	private String name;                //The element's name
	private String symbol;              //The element's chemical symbol

	//-------------------[Field declarations end here]-------------------//



	//---------------------[Constructors begin here]---------------------//

	public Element () {
		//Initialize variables to invalid states
		this.atomicNumber = -1;
		this.group        = -1;
		this.period       = -1;
		this.molarMass    = -1;
		this.name         = "";
		this.symbol       = "";
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



	//----------------------[Mutators begin here]-----------------------//

	//Set the element's atomic number
	public boolean setAtomicNumber (int atomicNumber) {
		//Check for a valid atomic number
		if (atomicNumber > 0) {
			this.atomicNumber = atomicNumber;
			return true;
		} else {
			return false;
		}
	}

	//Set the element's group
	public boolean setGroup (int group) {
		//Check for a valid group
		if (group > 0) {
			this.group = group;
			return true;
		} else {
			return false;
		}
	}

	//Set the element's period
	public boolean setPeriod (int period) {
		//Check for a valid period
		if (period > 0) {
			this.period = period;
			return true;
		} else {
			return false;
		}
	}

	//Set the element's molar mass
	public boolean setMolarMass(double molarMass) {
		//Check for a valid molar mass
		if (molarMass > 0) {
			this.molarMass = molarMass;
			return true;
		} else {
			return false;
		}
	}

	//Set the element's name
	public boolean setName(String name) {
		//Check for a valid name
		if (name.length() != 0) {
			//Ensure that name is lowercase
			this.name = name.toLowerCase();
			return true;
		} else {
			return false;
		}
	}

	//Set the element's chemical symbol
	public boolean setSymbol(String symbol) {
		//Check for a valid symbol
		if (symbol.length() != 0) {
			//Ensure that only the first letter of the symbol is uppercase
			symbol = symbol.toLowerCase();
			this.symbol = (char) (symbol.charAt(0) + ('A' - 'a')) + symbol.substring(1);
			return true;
		} else {
			return false;
		}
	}

	//-----------------------[Mutators end here]------------------------//



	//--------------[Object validity checking begins here]--------------//

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

	//---------------[Object validity checking ends here]---------------//



	//----------------[Object serialization begins here]----------------//

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
	public static Element deserialize (String serializedData) {
		DataParser parser = new DataParser();
		String[] dataArray = parser.parse(serializedData);

		//Check for the expected data length
		if (dataArray.length != 6) {
			return null;
		}

		//Flag boolean to mark whether the deserialization was successful
		boolean deserializationSuccessful = true;

		try {
			Element newElement = new Element();

			//Set element properties
			deserializationSuccessful &=
					newElement.setAtomicNumber  (Integer.parseInt(dataArray[0]));
			deserializationSuccessful &=
					newElement.setGroup         (Integer.parseInt(dataArray[1]));
			deserializationSuccessful &=
					newElement.setPeriod        (Integer.parseInt(dataArray[2]));
			deserializationSuccessful &=
					newElement.setMolarMass     (Double.parseDouble(dataArray[3]));
			deserializationSuccessful &=
					newElement.setName          (dataArray[4]);
			deserializationSuccessful &=
					newElement.setSymbol        (dataArray[5]);

			//Check whether the deserialization was successful
			if (deserializationSuccessful) {
				return newElement;
			}
		} catch (NumberFormatException e) {
			System.err.println("NumberFormatException: " + e.getMessage());
		}

		return null;
	}

	//-----------------[Object serialization ends here]-----------------//
}