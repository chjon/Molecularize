package Molecularize.atomos;

/*-------------------------------------------------------------*\
	This class handles invalid PeriodicTable deserialization.
\*-------------------------------------------------------------*/

public class PeriodicTableDataException extends Exception {

	//-----------------[Constant declaration begins here]----------------//

	//Possible reasons for an error
	public enum ExceptionType {
		NUM_ELEMENTS_NOT_FOUND,     //Size of table not found
		NULL_ELEMENTS               //No data found
	}

	//Error messages
	public static final String NULL_ELEMENTS_MESSAGE =
			"No elements array received";
	public static final String NUM_ELEMENTS_NOT_FOUND_MESSAGE =
			"Number of elements not found";

	//------------------[Constant declaration ends here]-----------------//



	//-----------------[Variable declaration begins here]----------------//

	private ExceptionType errorReason;

	//------------------[Variable declaration ends here]-----------------//



	//---------------------[Constructors begin here]---------------------//

	private PeriodicTableDataException (String message, ExceptionType errorReason) {
		super(message);
		this.errorReason = errorReason;
	}

	public static PeriodicTableDataException create (ExceptionType errorReason) {
		StringBuilder message = new StringBuilder();

		//Determine required message based on the cause of the exception
		switch (errorReason) {
			case NULL_ELEMENTS:
				message.append(NULL_ELEMENTS_MESSAGE);
				break;
			default:
				message.append(NUM_ELEMENTS_NOT_FOUND_MESSAGE);
		}

		return new PeriodicTableDataException(message.toString(), errorReason);
	}

	//----------------------[Constructors end here]----------------------//
}
