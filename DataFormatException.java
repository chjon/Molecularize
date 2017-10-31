package Molecularize;

/*-------------------------------------------------*\
	This class handles invalid DataParser input.
\*-------------------------------------------------*/

public class DataFormatException extends Exception {

	//-----------------[Constant declaration begins here]----------------//

	//Possible reasons for an error
	public enum ExceptionType {
		EXPECTING_BEGINNING,    //Missing open bracket
		EXPECTING_SEPARATOR,    //Missing datum separator
		EXPECTING_ENDING,       //Missing close bracket
		NO_DATA                 //No data found
	}

	//Error message for a missing open bracket
	public static final String EXPECTING_BEGINNING_MESSAGE =
			"Expecting '" + DataParser.OPEN_CURLY_BRACKET  + "' at index ";

	//Error message for a missing datum separator
	public static final String EXPECTING_SEPARATOR_MESSAGE =
			"Expecting '" + DataParser.DATUM_SEPARATOR     + "' at index ";

	//Error message for a missing close bracket
	public static final String EXPECTING_ENDING_MESSAGE    =
			"Expecting '" + DataParser.CLOSE_CURLY_BRACKET + "' at index ";

	//Error message for no data
	public static final String NO_DATA_MESSAGE             =
			"Expecting data at index ";

	//------------------[Constant declaration ends here]-----------------//



	//-----------------[Variable declaration begins here]----------------//

	private String        data;             //Raw data
	private int           index;            //Index of error in data
	private ExceptionType errorReason;      //Reason for the error

	//------------------[Variable declaration ends here]-----------------//



	//---------------------[Constructors begin here]---------------------//

	private DataFormatException (String message, String data, int index, ExceptionType errorReason) {
		super(message);
		this.data        = data;
		this.index       = index;
		this.errorReason = errorReason;
	}

	public static DataFormatException create (String data, int index, ExceptionType errorReason) {
		StringBuilder message = new StringBuilder();

		//Determine required message based on the cause of the exception
		switch (errorReason) {
			case EXPECTING_BEGINNING:
				message.append(EXPECTING_BEGINNING_MESSAGE);
				break;
			case EXPECTING_SEPARATOR:
				message.append(EXPECTING_SEPARATOR_MESSAGE);
				break;
			case EXPECTING_ENDING:
				message.append(EXPECTING_ENDING_MESSAGE   );
				break;
			default:
				message.append(NO_DATA_MESSAGE);
		}

		//Indicate where the error occurred
		message.append(index).append(": \"").append(data).append("\"");

		return new DataFormatException(message.toString(), data, index, errorReason);
	}

	//----------------------[Constructors end here]----------------------//



	//----------------------[Accessors begin here]-----------------------//

	public String getData() {
		return data;
	}


	public int getIndex() {
		return index;
	}

	public ExceptionType getErrorReason() {
		return errorReason;
	}

	//-----------------------[Accessors end here]------------------------//
}
