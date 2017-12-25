package Molecularize.convertor;

/*-------------------------------------------------------------------*\
	This class wraps unit types with the exponent of each unit type
\*-------------------------------------------------------------------*/

public class UnitTypeWrapper {
	//------------------[Field declarations begin here]------------------//

	private UnitType unitType;
	private int      exponent;

	//-------------------[Field declarations end here]-------------------//



	//---------------------[Constructors begin here]---------------------//

	public UnitTypeWrapper (UnitType unitType, int exponent) {
		this.unitType = unitType;
		this.exponent = exponent;
	}

	//----------------------[Constructors end here]----------------------//



	//----------------------[Accessors begin here]-----------------------//

	UnitType getUnitType () {
		return unitType;
	}

	int getExponent () {
		return exponent;
	}

	//-----------------------[Accessors end here]------------------------//



	//----------------------[Mutators begin here]------------------------//

	public void setUnitType (UnitType unitType) {
		this.unitType = unitType;
	}

	void setExponent (int exponent) {
		this.exponent = exponent;
	}

	//-----------------------[Mutators end here]-------------------------//
}
