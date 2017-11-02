package Molecularize.src;

/*-------------------------------------------------------*\
	This class handles invalid Element deserialization.
\*-------------------------------------------------------*/

public class ElementDataException extends Exception {

	//-----------------[Constant declaration begins here]----------------//

	//Possible reasons for an error
	public enum ExceptionType {
		INVALID_ATOMIC_NUMBER,      //Non-positive atomic number
		INVALID_GROUP_NUMBER,       //Non-positive group number
		INVALID_PERIOD_NUMBER,      //Non-positive period number
		INVALID_MOLAR_MASS,         //Non-positive molar mass
		INVALID_NAME,               //Zero-length chemical name
		INVALID_SYMBOL,             //Zero-length chemical symbol
		INVALID_LENGTH              //No data found
	}

	//Error messages
	public static final String INVALID_ATOMIC_NUMBER_MESSAGE =
			"Expecting positive atomic number; received: ";
	public static final String INVALID_GROUP_NUMBER_MESSAGE  =
			"Expecting positive group number; received: ";
	public static final String INVALID_PERIOD_NUMBER_MESSAGE =
			"Expecting positive period number; received: ";
	public static final String INVALID_MOLAR_MASS_MESSAGE    =
			"Expecting positive molar mass; received: ";
	public static final String INVALID_NAME_MESSAGE          =
			"Expecting non-empty alphabetical name; received: ";
	public static final String INVALID_SYMBOL_MESSAGE        =
			"Expecting non-empty alphabetical symbol; received: ";
	public static final String INVALID_LENGTH_MESSAGE        =
			"Expecting six data blocks; received ";

	//------------------[Constant declaration ends here]-----------------//



	//-----------------[Variable declaration begins here]----------------//

	private String        data;
	private ExceptionType errorReason;

	//------------------[Variable declaration ends here]-----------------//



	//---------------------[Constructors begin here]---------------------//

	private ElementDataException (String message, String data, ExceptionType errorReason) {
		super(message);
		this.data        = data;
		this.errorReason = errorReason;
	}

	public static ElementDataException create (String data, ExceptionType errorReason) {
		StringBuilder message = new StringBuilder();

		//Determine required message based on the cause of the exception
		switch (errorReason) {
			case INVALID_ATOMIC_NUMBER:
				message.append(INVALID_ATOMIC_NUMBER_MESSAGE);
				break;
			case INVALID_GROUP_NUMBER:
				message.append(INVALID_GROUP_NUMBER_MESSAGE);
				break;
			case INVALID_PERIOD_NUMBER:
				message.append(INVALID_PERIOD_NUMBER_MESSAGE);
				break;
			case INVALID_MOLAR_MASS:
				message.append(INVALID_MOLAR_MASS_MESSAGE);
				break;
			case INVALID_NAME:
				message.append(INVALID_NAME_MESSAGE);
				break;
			case INVALID_SYMBOL:
				message.append(INVALID_SYMBOL_MESSAGE);
				break;
			default:
				message.append(INVALID_LENGTH_MESSAGE);
		}

		//Append given data
		if (errorReason != ExceptionType.INVALID_LENGTH) {
			message.append("\"").append(data).append("\"");
		} else {
			message.append(data);
		}

		return new ElementDataException(message.toString(), data, errorReason);
	}

	//----------------------[Constructors end here]----------------------//



	//----------------------[Accessors begin here]-----------------------//

	public String getData() {
		return data;
	}

	public ExceptionType getErrorReason() {
		return errorReason;
	}

	//-----------------------[Accessors end here]------------------------//
}
