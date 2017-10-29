package Molecularize;

public class DataParser {
	private final char OPEN_CURLY_BRACKET  = '{';
	private final char CLOSE_CURLY_BRACKET = '}';
	private final char DATUM_SEPARATOR     = ',';

	//---------------------[Constructors begin here]---------------------//

	public DataParser () {

	}

	//----------------------[Constructors end here]----------------------//



	//---------------------[Data parsing begins here]--------------------//

	public String[] parse (String data) {

		//Check for a valid data string
		if (data.length() < 2) {
			return null;
		}

		int     openBracketCount   = 0;
		int     numberOfData       = 0;
		boolean expectingSeparator = false;

		//Count the number of data
		for (int i = 0; i < data.length(); i++) {
			char curChar = data.charAt(i);

			//Check for data separator
			if (expectingSeparator) {
				if (curChar == DATUM_SEPARATOR) {
					expectingSeparator = false;
				} else {
					return null;
				}

			//Check for invalid beginning of datum
			} else if (openBracketCount == 0 && curChar != OPEN_CURLY_BRACKET) {
				return null;

			//Check for beginning of datum
			} else if (curChar == OPEN_CURLY_BRACKET) {
				openBracketCount++;

			//Check for end of datum
			} else if (curChar == CLOSE_CURLY_BRACKET) {
				openBracketCount--;

				//Check for illegal number of closed brackets
				if (openBracketCount < 0) {
					return null;

				//Increment number of data
				} else if (openBracketCount == 0) {
					numberOfData++;
					expectingSeparator = true;
				}
			}
		}

		//Check for bracket imbalance
		if (openBracketCount != 0) {
			return null;
		}

		String[] dataArray = new String[numberOfData];
		int dataArrayIndex = 0;
		int startIndex     = 0;

		//Load data into array
		for (int i = 0; i < data.length(); i++) {
			char curChar = data.charAt(i);

			//Increment bracket count
			if (curChar == OPEN_CURLY_BRACKET) {
				openBracketCount++;

				//Check for beginning of datum
				if (openBracketCount == 1) {
					startIndex = i + 1;
				}

			//Decrement bracket count
			} else if (curChar == CLOSE_CURLY_BRACKET) {
				openBracketCount--;

				//Check for end of datum
				if (openBracketCount == 0) {
					dataArray[dataArrayIndex] = data.substring(startIndex, i);
					dataArrayIndex++;
				}
			}
		}

		return dataArray;
	}

	//---------------------[Data parsing begins here]--------------------//
}
