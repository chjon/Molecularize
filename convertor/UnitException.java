package Molecularize.convertor;

/*----------------------------------------*\
	This class handles invalid Unit data
\*----------------------------------------*/

public class UnitException extends Exception{
	//-----------------[Constant declaration begins here]----------------//

	//Possible reasons for an error
	public enum ExceptionType {
		NULL_NAME,      //No name found
		NULL_SYMBOL,    //No symbol found
		NULL_TYPE       //No type found
	}

	//Error messages
	public static final String NULL_NAME_MESSAGE =
			"No name found";
	public static final String NULL_SYMBOL_MESSAGE =
			"No symbol found";
	public static final String NULL_TYPE_MESSAGE =
			"No type found";

	//------------------[Constant declaration ends here]-----------------//



	//---------------------[Constructors begin here]---------------------//

	private UnitException (String message) {
		super(message);
	}

	public static UnitException create (ExceptionType errorReason) {
		StringBuilder message = new StringBuilder();

		//Determine required message based on the cause of the exception
		switch (errorReason) {
			case NULL_NAME:
				message.append(NULL_NAME_MESSAGE);
				break;
			case NULL_SYMBOL:
				message.append(NULL_SYMBOL_MESSAGE);
				break;
			case NULL_TYPE:
				message.append(NULL_TYPE_MESSAGE);
				break;
		}

		return new UnitException(message.toString());
	}

	//----------------------[Constructors end here]----------------------//
}
