package Molecularize.convertor;

/*--------------------------------------------*\
	This class handles invalid UnitType data
\*--------------------------------------------*/

public class UnitTypeException extends Exception {

	//-----------------[Constant declaration begins here]----------------//

	//Possible reasons for an error
	public enum ExceptionType {
		NO_PARENT_TYPES,    //No parent types found
		NULL_PROPERTY,      //No property found
		NULL_OBJECT         //No object found
	}

	//Error messages
	public static final String NO_PARENT_TYPES_MESSAGE =
			"Tried to access non-existent parent types";
	public static final String NULL_PROPERTY_MESSAGE =
			"No property found";
	public static final String NULL_OBJECT_MESSAGE =
			"Source object could not be found";

	//------------------[Constant declaration ends here]-----------------//



	//---------------------[Constructors begin here]---------------------//

	private UnitTypeException (String message) {
		super(message);
	}

	public static UnitTypeException create (ExceptionType errorReason) {
		StringBuilder message = new StringBuilder();

		//Determine required message based on the cause of the exception
		switch (errorReason) {
			case NO_PARENT_TYPES:
				message.append(NO_PARENT_TYPES_MESSAGE);
				break;
			case NULL_PROPERTY:
				message.append(NULL_PROPERTY_MESSAGE);
				break;
			case NULL_OBJECT:
				message.append(NULL_OBJECT_MESSAGE);
				break;
		}

		return new UnitTypeException(message.toString());
	}

	//----------------------[Constructors end here]----------------------//
}
