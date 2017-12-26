package Molecularize.convertor;

/*-----------------------------------------------*\
	This class represents a unit of measurement
\*-----------------------------------------------*/

public class Unit {
	//------------------[Field declarations begin here]------------------//

	private String   name;
	private String   symbol;
	private UnitType type;

	//-------------------[Field declarations end here]-------------------//



	//---------------------[Constructors begin here]---------------------//

	public Unit (String name, String symbol, UnitType type) throws UnitException {
		//Check for input validity
		if (name == null) {
			throw UnitException.create(
					UnitException.ExceptionType.NULL_NAME
			);
		} else if (symbol == null) {
			throw UnitException.create(
					UnitException.ExceptionType.NULL_SYMBOL
			);
		} else if (type == null) {
			throw UnitException.create(
					UnitException.ExceptionType.NULL_TYPE
			);
		}

		this.name   = name;
		this.symbol = symbol;
		this.type   = type;
	}

	//----------------------[Constructors end here]----------------------//



	//----------------------[Accessors begin here]-----------------------//

	//Get the name of the unit
	public String getName () {
		return this.name;
	}

	//Get the symbol of the unit
	public String getSymbol () {
		return this.symbol;
	}

	//Get the type of the unit
	public UnitType getUnitType () {
		return this.type;
	}

	//-----------------------[Accessors end here]------------------------//



	//-----------------[Comparison functions begin here]-----------------//

	public boolean equals (Unit toCompare) {
		return	this.symbol.equals(toCompare.symbol) &&
				this.name  .equals(toCompare.name)   &&
				this.type  .equals(toCompare.type);
	}

	//------------------[Comparison functions end here]------------------//
}
