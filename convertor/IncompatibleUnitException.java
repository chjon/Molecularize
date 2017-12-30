package Molecularize.convertor;

/*-----------------------------------------*\
	This class handles incompatible units
\*-----------------------------------------*/

public class IncompatibleUnitException extends Exception {
	//-----------------[Constant declaration begins here]----------------//

	//Error messages
	public static final String INCOMPATIBLE_UNIT_EXCEPTION =
			"Quantity units are incompatible";

	//------------------[Constant declaration ends here]-----------------//



	//---------------------[Constructors begin here]---------------------//

	public IncompatibleUnitException () {
		super(INCOMPATIBLE_UNIT_EXCEPTION);
	}

	//----------------------[Constructors end here]----------------------//
}
