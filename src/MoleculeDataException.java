package Molecularize.src;

/*-------------------------------------------------------------*\
	This class handles invalid PeriodicTable deserialization.
\*-------------------------------------------------------------*/

public class MoleculeDataException extends Exception {

	//-----------------[Constant declaration begins here]----------------//

	//Possible reasons for an error
	public enum ExceptionType {
		NUM_ELEMENTS_NOT_FOUND,     //Size of table not found
		ELEMENT_NOT_FOUND,          //Element could not be found
		INVALID_ELEMENT_FORMAT,     //Problem with element data format
		INVALID_ELEMENT_DATA,       //Invalid element data
		INVALID_MOLECULE_FORMAT,    //Invalid molecule data format
		BRACKET_IMBALANCE,          //Detected bracket imbalance
		NULL_ELEMENTS               //No data found
	}

	//Error messages
	public static final String NULL_ELEMENTS_MESSAGE =
			"No elements array received";
	public static final String ELEMENT_NOT_FOUND_MESSAGE =
			"Element could not be found";
	public static final String NUM_ELEMENTS_NOT_FOUND_MESSAGE =
			"Number of elements not found";
	public static final String INVALID_ELEMENT_FORMAT_MESSAGE =
			"Received invalid element format";
	public static final String INVALID_MOLECULE_FORMAT_MESSAGE =
			"Received invalid molecule format";
	public static final String INVALID_ELEMENT_DATA_MESSAGE =
			"Received invalid element data";
	public static final String BRACKET_IMBALANCE_MESSAGE =
			"Detected bracket imbalance";

	//------------------[Constant declaration ends here]-----------------//



	//-----------------[Variable declaration begins here]----------------//

	private ExceptionType errorReason;

	//------------------[Variable declaration ends here]-----------------//



	//---------------------[Constructors begin here]---------------------//

	private MoleculeDataException(String message, ExceptionType errorReason) {
		super(message);
		this.errorReason = errorReason;
	}

	public static MoleculeDataException create (ExceptionType errorReason) {
		StringBuilder message = new StringBuilder();

		//Determine required message based on the cause of the exception
		switch (errorReason) {
			case ELEMENT_NOT_FOUND:
				message.append(ELEMENT_NOT_FOUND_MESSAGE);
				break;
			case NUM_ELEMENTS_NOT_FOUND:
				message.append(NUM_ELEMENTS_NOT_FOUND_MESSAGE);
				break;
			case INVALID_ELEMENT_FORMAT:
				message.append(INVALID_ELEMENT_FORMAT_MESSAGE);
				break;
			case INVALID_ELEMENT_DATA:
				message.append(INVALID_ELEMENT_DATA_MESSAGE);
				break;
			case INVALID_MOLECULE_FORMAT:
				message.append(INVALID_MOLECULE_FORMAT_MESSAGE);
				break;
			case BRACKET_IMBALANCE:
				message.append(BRACKET_IMBALANCE_MESSAGE);
				break;
			default:
				message.append(NULL_ELEMENTS_MESSAGE);
		}

		return new MoleculeDataException(message.toString(), errorReason);
	}

	//----------------------[Constructors end here]----------------------//
}
