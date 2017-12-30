package Molecularize.convertor;

/*------------------------------------*\
	This class represents a quantity
\*------------------------------------*/

import java.util.Arrays;

public class Quantity {
	//------------------[Field declarations begin here]------------------//

	private double value;       //Value of the quantity
	private Unit[] units;       //Unit for the quantity
	private int [] exponents;   //Exponents for each of the units

	//-------------------[Field declarations end here]-------------------//



	//---------------------[Constructors begin here]---------------------//

	public Quantity (double value) {
		this.value     = value;
		this.units     = null;
		this.exponents = null;
	}

	public Quantity (double value, Unit unit) {
		this.value        = value;
		this.units        = new Unit[1];
		this.units[0]     = unit;
		this.exponents    = new int[1];
		this.exponents[0] = 1;
	}

	public Quantity (double value, Unit unit, int exponent) {

		this.value        = value;
		this.units        = new Unit[1];
		this.units[0]     = unit;
		this.exponents    = new int[1];
		this.exponents[0] = exponent;

		simplify();
	}

	public Quantity (double value, Unit[] units, int[] exponents) {
		this.value     = value;
		this.units     = units;
		this.exponents = exponents;

		simplify();
	}

	//----------------------[Constructors end here]----------------------//



	//----------------------[Accessors begin here]-----------------------//

	public double getValue () {
		return value;
	}

	@Override
	public String toString () {
		StringBuilder output = new StringBuilder();

		output.append(value);

		//Append each unit's symbol
		if (units != null) {
			for (int i = 0; i < units.length; i++) {
				output.append(' ');
				output.append(units[i].getSymbol());

				if (exponents[i] != 1) {
					output.append("^(");
					output.append(exponents[i]);
					output.append(")");
				}
			}
		}

		return output.toString();
	}

	//-----------------------[Accessors end here]------------------------//



	//-----------------[Comparison functions begin here]-----------------//

	public boolean unitsEquals (Quantity toCompare) {
		//Check if units are null
		if (this.units == null && toCompare.units == null) {
			return true;

		//Check if one of the units are null
		} else if (this.units == null || toCompare.units == null) {
			return false;
		}

		//Check the number of units
		if (this.units.length != toCompare.units.length) {
			return false;
		}

		//Check each unit

		//Loop through each unit of this quantity
		boolean unitsMatch = true;

		for (int i = 0; i < this.units.length && unitsMatch; i++) {
			boolean found = false;

			//Loop through each unit of toCompare
			for (int j = 0; j < toCompare.units.length && !found; j++) {

				//Check whether units and exponents match
				if (this.exponents[i] == toCompare.exponents[j] &&
						this.units[i].equals(toCompare.units[j])) {

					//Set flag that units match
					found = true;
				}
			}

			//Set flag that a unit was found without a match
			if (!found) {
				unitsMatch = false;
			}
		}

		return unitsMatch;
	}

	//------------------[Comparison functions end here]------------------//



	//---------------------[Operations begin here]-----------------------//

	//Simplify units
	private void simplify () {
		if (this.units == null) {
			return;
		}

		Unit[] simplifiedUnits     = new Unit[this.units.length];
		int [] simplifiedExponents = new int [this.units.length];

		int simplifiedIndex = 0;

		//Check whether each of the current units is in the simplified units list
		for (int i = 0; i < this.units.length; i++) {
			boolean found = false;

			for (int j = 0; j < simplifiedIndex && !found; j++) {
				if (simplifiedUnits[j].equals(this.units[i])) {
					simplifiedExponents[j] += this.exponents[i];
					found = true;
				}
			}

			if (!found) {
				simplifiedUnits    [simplifiedIndex] = this.units    [i];
				simplifiedExponents[simplifiedIndex] = this.exponents[i];
				simplifiedIndex++;
			}
		}

		//Count zero-power units
		int numZero = 0;

		for (int i = 0; i < simplifiedIndex; i++) {
			if (simplifiedExponents[i] == 0) {
				numZero++;
			}
		}

		this.units     = new Unit[simplifiedIndex - numZero];
		this.exponents = new int [simplifiedIndex - numZero];

		//Remove zero-power exponents
		int newIndex = 0;

		for (int i = 0; i < simplifiedIndex; i++) {
			if (simplifiedExponents[i] != 0) {
				this.units    [newIndex] = simplifiedUnits    [i];
				this.exponents[newIndex] = simplifiedExponents[i];
				newIndex++;
			}
		}
	}

	//Add quantities
	public Quantity add (Quantity toAdd) throws
			IncompatibleUnitException {

		//Check whether units are equal
		if (this.unitsEquals(toAdd)) {
			Quantity output = new Quantity(this.value, this.units, this.exponents);
			output.value = this.value + toAdd.value;

			return output;
		} else {
			throw new IncompatibleUnitException();
		}
	}

	//Subtract quantities
	public Quantity subtract (Quantity toSubtract) throws
			IncompatibleUnitException {

		//Check whether units are equal
		if (this.unitsEquals(toSubtract)) {
			Quantity output = new Quantity(this.value, this.units, this.exponents);
			output.value = this.value - toSubtract.value;

			return output;
		} else {
			throw new IncompatibleUnitException();
		}
	}

	//Multiply quantities
	public Quantity multiply (Quantity toMultiply) {
		Quantity output = new Quantity(this.value * toMultiply.value);

		//Get length
		int length = 0;

		if (this.units != null) {
			length += this.units.length;
		}

		if (toMultiply.units != null) {
			length += toMultiply.units.length;
		}

		//Merge units
		Unit[] outputUnits     = new Unit[length];
		int [] outputExponents = new int [length];

		int outputIndex = 0;

		//Add units from this quantity if units exist
		if (this.units != null) {
			while (outputIndex < this.units.length) {
				outputUnits    [outputIndex] = this.units    [outputIndex];
				outputExponents[outputIndex] = this.exponents[outputIndex];

				outputIndex++;
			}
		}

		//Add units from toMultiply if units exist
		if (toMultiply.units != null) {
			for (int i = 0; i < toMultiply.units.length; i++) {
				boolean found = false;

				//Check whether unit is in output array
				for (int j = 0; j < outputIndex && !found; j++) {

					//Check whether units are equal
					if (outputUnits[i].equals(toMultiply.units[j])) {
						outputExponents[j] += toMultiply.exponents[i];
						found = true;
					}
				}

				//Add unit to output arrays
				if (!found) {
					outputUnits    [outputIndex] = toMultiply.units    [i];
					outputExponents[outputIndex] = toMultiply.exponents[i];
					outputIndex++;
				}
			}
		}

		//Assign arrays to output
		output.units     = Arrays.copyOf(outputUnits,     outputIndex);
		output.exponents = Arrays.copyOf(outputExponents, outputIndex);

		output.simplify();

		return output;
	}

	//Divide quantities
	public Quantity divide (Quantity toDivide) {
		Quantity output = new Quantity(this.value / toDivide.value);

		//Get length
		int length = 0;

		if (this.units != null) {
			length += this.units.length;
		}

		if (toDivide.units != null) {
			length += toDivide.units.length;
		}

		//Merge units
		Unit[] outputUnits     = new Unit[length];
		int [] outputExponents = new int [length];

		int outputIndex = 0;

		//Add units from this quantity if units exist
		if (this.units != null) {
			while (outputIndex < this.units.length) {
				outputUnits    [outputIndex] = this.units    [outputIndex];
				outputExponents[outputIndex] = this.exponents[outputIndex];

				outputIndex++;
			}
		}

		//Add units from toDivide if units exist
		if (toDivide.units != null) {
			for (int i = 0; i < toDivide.units.length; i++) {
				boolean found = false;

				//Check whether unit is in output array
				for (int j = 0; j < outputIndex && !found; j++) {

					//Check whether units are equal
					if (outputUnits[i].equals(toDivide.units[j])) {
						outputExponents[j] -= toDivide.exponents[i];
						found = true;
					}
				}

				//Add unit to output arrays
				if (!found) {
					outputUnits    [outputIndex] = toDivide.units     [i];
					outputExponents[outputIndex] = -toDivide.exponents[i];
					outputIndex++;
				}
			}
		}

		//Assign arrays to output
		output.units     = Arrays.copyOf(outputUnits,     outputIndex);
		output.exponents = Arrays.copyOf(outputExponents, outputIndex);

		output.simplify();

		return output;
	}

	//----------------------[Operations end here]------------------------//
}
