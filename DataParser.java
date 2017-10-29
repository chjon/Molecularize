package Molecularize;

/*----------------------------------------------------------------------*\
	This class is a custom parser to separate data values in a String.
\*----------------------------------------------------------------------*/

public class DataParser {
	//-----------------[Constant declaration begins here]----------------//

	static final char OPEN_CURLY_BRACKET  = '{';
	static final char CLOSE_CURLY_BRACKET = '}';
	static final char DATUM_SEPARATOR     = ',';

	//------------------[Constant declaration ends here]-----------------//



	//---------------------[Constructors begin here]---------------------//

	public DataParser () {

	}

	//----------------------[Constructors end here]----------------------//



	//---------------------[Data parsing begins here]--------------------//

	public String[] parse (String data) throws DataFormatException {

		//Check for a valid data string
		if (data.length() == 0) {
			throw DataFormatException.create(
					data,
					0,
					DataFormatException.ExceptionType.NO_DATA
			);
		}

		int     openBracketCount   = 0;         //The net number of open brackets
		int     numberOfData       = 0;         //The counted number of data in the dataset
		boolean expectingSeparator = false;     //Whether or not a separator is expected

		//Count the number of data
		for (int i = 0; i < data.length(); i++) {
			char curChar = data.charAt(i);

			//Check for data separator
			if (expectingSeparator) {
				if (curChar == DATUM_SEPARATOR) {
					expectingSeparator = false;
				} else {
					throw DataFormatException.create(
							data,
							i,
							DataFormatException.ExceptionType.EXPECTING_SEPARATOR
					);
				}

			//Check for invalid beginning of datum
			} else if (openBracketCount == 0 && curChar != OPEN_CURLY_BRACKET) {
				throw DataFormatException.create(
						data,
						i,
						DataFormatException.ExceptionType.EXPECTING_BEGINNING
				);

			//Check for beginning of datum
			} else if (curChar == OPEN_CURLY_BRACKET) {
				openBracketCount++;

			//Check for end of datum
			} else if (curChar == CLOSE_CURLY_BRACKET) {
				openBracketCount--;

				//Check for illegal number of closed brackets
				if (openBracketCount < 0) {
					throw DataFormatException.create(
							data,
							i,
							DataFormatException.ExceptionType.EXPECTING_SEPARATOR
					);

				//Increment number of data
				} else if (openBracketCount == 0) {
					numberOfData++;
					expectingSeparator = true;
				}
			}
		}

		//Check for bracket imbalance
		if (openBracketCount != 0) {
			throw DataFormatException.create(
					data,
					data.length(),
					DataFormatException.ExceptionType.EXPECTING_ENDING
			);
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

	//----------------------[Data parsing ends here]---------------------//
}
