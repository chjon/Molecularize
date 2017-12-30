package Molecularize.convertor;

/*-----------------------------------------------*\
	This class represents a unit of measurement
\*-----------------------------------------------*/

public class Unit {
	//------------------[Field declarations begin here]------------------//

	private String   name;
	private String   symbol;

	//-------------------[Field declarations end here]-------------------//



	//---------------------[Constructors begin here]---------------------//

	public Unit (String name, String symbol) throws UnitException {
		//Check for input validity
		if (name == null) {
			throw UnitException.create(
					UnitException.ExceptionType.NULL_NAME
			);
		} else if (symbol == null) {
			throw UnitException.create(
					UnitException.ExceptionType.NULL_SYMBOL
			);
		}

		this.name   = name;
		this.symbol = symbol;
	}

	//----------------------[Constructors end here]----------------------//



	//------------------[Validity checking begins here]------------------//

	public boolean isValid () {
		return	this.name   != null &&
				this.symbol != null;
	}

	//-------------------[Validity checking ends here]-------------------//



	//----------------------[Accessors begin here]-----------------------//

	//Get the name of the unit
	public String getName () {
		return this.name;
	}

	//Get the symbol of the unit
	public String getSymbol () {
		return this.symbol;
	}

	//Alias for getSymbol
	@Override
	public String toString () {
		return symbol;
	}

	//-----------------------[Accessors end here]------------------------//



	//-----------------[Comparison functions begin here]-----------------//

	public boolean equals (Unit toCompare) {
		return	this.symbol.equals(toCompare.symbol) &&
				this.name  .equals(toCompare.name);
	}

	//------------------[Comparison functions end here]------------------//
}
