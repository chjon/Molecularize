package Molecularize;

/*-----------------------------------------*\
	This class stores a list of elements.
\*-----------------------------------------*/

public class PeriodicTable {

	//-----------------[Constant declaration begins here]----------------//

	public static final String NUM_ELEMENTS_PREFIX = "L:";

	//------------------[Constant declaration ends here]-----------------//



	//-----------------[Variable declaration begins here]----------------//

	private int tableSize;          //The size of the elements array
	private Element[] elements;     //The array of elements

	//------------------[Variable declaration ends here]-----------------//



	//---------------------[Constructors begin here]---------------------//

	public PeriodicTable () {

		//Initialize variables to invalid states
		tableSize = -1;
		elements  = null;
	}

	//----------------------[Constructors end here]----------------------//



	//----------------------[Accessors begin here]-----------------------//

	public int getTableSize () {
		return tableSize;
	}

	public Element[] getElements () {
		return elements;
	}

	//Returns the number of non-null elements in the table
	public int getNumElements () {
		int count = 0;

		for (int i = 0; i < tableSize; i++) {
			if (elements[i] != null) {
				count++;
			}
		}

		return count;
	}

	//Get an element based on atomic number
	public Element getElement (int atomicNumber) {

		//Check for a valid atomic number input
		if (atomicNumber > 0 && atomicNumber <= tableSize) {
			return elements[atomicNumber - 1];
		}

		return null;
	}

	//Get an element based on name
	public Element getElementByName (String name) {
		for (int i = 0; i < tableSize; i++) {
			if (elements[i] != null && elements[i].getName().equalsIgnoreCase(name)) {
				return elements[i];
			}
		}

		return null;
	}

	//Get an element based on symbol
	public Element getElementBySymbol (String symbol) {
		for (int i = 0; i < tableSize; i++) {
			if (elements[i] != null && elements[i].getSymbol().equals(symbol)) {
				return elements[i];
			}
		}

		return null;
	}

	//-----------------------[Accessors end here]------------------------//



	//-----------------------[Mutators begin here]-----------------------//

	//Change the periodic table's list of elements
	public void setElements (Element[] elements) throws PeriodicTableDataException {

		//Check for a valid input elements list
		if (elements == null) {
			throw PeriodicTableDataException.create(
					PeriodicTableDataException.ExceptionType.NULL_ELEMENTS
			);
		} else {
			this.tableSize = elements.length;
			this.elements  = elements;
		}
	}

	//Add an element to the table
	public void push (Element toAdd) {

		//Check whether the element is null
		if (toAdd != null) {
			int atomicNumber = toAdd.getAtomicNumber();

			//Check whether an element with the same information already exists
			if (!conflicts(toAdd)) {
				elements[atomicNumber - 1] = toAdd;
			}
		}
	}

	//Remove an element from the table
	public void pop (int atomicNumber) {

		//Check for a valid atomic number input
		if (atomicNumber > 0 && atomicNumber <= tableSize) {
			elements[atomicNumber - 1] = null;
		}
	}

	//------------------------[Mutators end here]------------------------//



	//-----------------[Comparison functions begin here]----------------//

	//Check if an element conflicts with an element already in the table
	public boolean conflicts (Element checkElement) {

		//Iterate through each element in the table
		for (int i = 0; i < tableSize; i++) {

			//Check for a conflict between the elements
			if (checkElement.conflicts(elements[i])) {
				return true;
			}
		}

		return false;
	}

	//------------------[Comparison functions end here]-----------------//



	//-----------[PeriodicTable validity checking begins here]-----------//

	public static boolean isValid (PeriodicTable tableToSerialize) {
		return
				tableToSerialize             != null &&     //Check whether object exists
				tableToSerialize.elements    != null &&     //Check whether element list exists
				tableToSerialize.tableSize   >  0;          //Check whether the number of elements has been set
	}

	//------------[PeriodicTable validity checking ends here]-----------//



	//----------------[Object serialization begins here]----------------//

	//Serialize periodic table data
	public static String serialize (PeriodicTable tableToSerialize) {

		//Check whether the periodic table is valid and serializable
		if (!isValid(tableToSerialize)) {
			return null;
		} else {
			StringBuilder output = new StringBuilder();

			//Serialize table metadata
			output.append("{" + NUM_ELEMENTS_PREFIX + tableToSerialize.tableSize + "},");

			//Serialize each element
			for (int i = 0; i < tableToSerialize.tableSize; i++) {
				String serializedElement =
						Element.serialize(tableToSerialize.getElement(i + 1));

				//Check if element is serializable
				if (serializedElement != null) {
					output.append("{" + serializedElement + "},");
				}
			}

			output.deleteCharAt(output.length() - 1);

			return output.toString();
		}
	}

	//Deserialize periodic table data
	public static PeriodicTable deserialize (String serializedData) throws
			DataFormatException, PeriodicTableDataException {

		DataParser parser = new DataParser();

		//Get list of separated data
		String[] dataArray = parser.parse(serializedData);

		//Create a new table
		PeriodicTable table = new PeriodicTable();

		//Check for a properly formatted dataset
		if (!dataArray[0].startsWith(NUM_ELEMENTS_PREFIX)) {
			throw PeriodicTableDataException.create(
					PeriodicTableDataException.ExceptionType.NUM_ELEMENTS_NOT_FOUND
			);
		} else {
			dataArray[0] = dataArray[0].substring(NUM_ELEMENTS_PREFIX.length());
		}

		try {
			table.setElements(new Element[Integer.parseInt(dataArray[0])]);
		} catch (NumberFormatException e) {
			throw PeriodicTableDataException.create(
					PeriodicTableDataException.ExceptionType.NULL_ELEMENTS
			);
		}

		//Add each element to the table
		for (int i = 1; i < dataArray.length; i++) {
			Element curElement = null;

			//Check for a valid deserializable element
			try {
				curElement = Element.deserialize(dataArray[i]);
			} catch (ElementDataException e) {
				curElement = null;
			} finally {

				//Add element to the array
				table.push(curElement);
			}
		}

		return table;
	}

	//-----------------[Object serialization ends here]-----------------//
}