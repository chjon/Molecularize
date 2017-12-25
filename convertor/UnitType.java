package Molecularize.convertor;

/*-------------------------------------------------*\
	This class represents a quantifiable property
\*-------------------------------------------------*/

import java.util.ArrayList;

public class UnitType {
	//------------------[Field declarations begin here]------------------//

	private String                     property;      //The name of the property represented by this unit type
	private ArrayList<UnitTypeWrapper> parentTypes;   //A list of unit types from which this unit type is derived

	//-------------------[Field declarations end here]-------------------//



	//---------------------[Constructors begin here]---------------------//

	//Given only the property, assume the unit type is a base type
	public UnitType (String property) {
		this.property    = property.toLowerCase();
		this.parentTypes = null;
	}

	public UnitType (String property, ArrayList<UnitTypeWrapper> parentTypes) {
		if (property == null) {
			this.property = null;
		} else {
			this.property = property.toLowerCase();
		}

		this.parentTypes = parentTypes;

		this.simplify();
	}

	//----------------------[Constructors end here]----------------------//



	//----------------------[Accessors begin here]-----------------------//

	//Get the name of the property represented by this unit type
	public String getProperty () {
		StringBuilder output = new StringBuilder();

		//Check if property exists
		if (this.property != null) {
			output.append(this.property);
			output.append(": ");
		}

		//Check if unit type is a base type
		if (!this.isBaseType()) {
			for (UnitTypeWrapper typeWrapper : parentTypes) {
				output.append(typeWrapper.getUnitType().property);
				output.append("^(");
				output.append(typeWrapper.getExponent());
				output.append(")");
			}
		}

		return output.toString();
	}

	//Alias for getProperty()
	@Override
	public String toString () {
		return getProperty();
	}

	//Get the unit type's parent types
	public ArrayList<UnitTypeWrapper> getParentTypes () {
		return parentTypes;
	}

	//Check if unit type is a base type - unit type is a base type if it has no parent types
	public boolean isBaseType () {
		return this.parentTypes == null;
	}

	//-----------------------[Accessors end here]------------------------//



	//----------------------[Mutators begin here]------------------------//

	//Add parent types
	public void addParent (ArrayList<UnitTypeWrapper> parentTypes) {
		//Check whether a parent type list exists
		if (this.parentTypes == null) {
			this.parentTypes = new ArrayList<>();
		}

		ArrayList<UnitTypeWrapper> newParentTypes = new ArrayList<>();
		newParentTypes.addAll(this.parentTypes);

		for (UnitTypeWrapper parentTypeWrapper : parentTypes) {
			//Check if parent type already exists in list
			boolean isAlreadyInList = false;

			for (UnitTypeWrapper typeWrapper : this.parentTypes) {
				if (parentTypeWrapper.getUnitType().property.equals(typeWrapper.getUnitType().property)) {
					newParentTypes.remove(typeWrapper);
					newParentTypes.add(new UnitTypeWrapper(
							typeWrapper.getUnitType(),
							typeWrapper.getExponent() + parentTypeWrapper.getExponent()
					));
					isAlreadyInList = true;
					break;
				}
			}

			if (!isAlreadyInList) {
				newParentTypes.add(parentTypeWrapper);
			}
		}

		this.parentTypes = newParentTypes;
		this.simplify();
	}

	public void addParent (UnitType parentType, int exponent) {
		//Check whether a parent type list exists
		if (this.parentTypes == null) {
			this.parentTypes = new ArrayList<>();
		}

		//Check if parent type already exists in list
		boolean isAlreadyInList = false;
		ArrayList<UnitTypeWrapper> newParentTypes = new ArrayList<>();
		newParentTypes.addAll(this.parentTypes);

		for (UnitTypeWrapper typeWrapper : this.parentTypes) {
			if (parentType.property.equals(typeWrapper.getUnitType().property)) {
				newParentTypes.remove(typeWrapper);
				newParentTypes.add(new UnitTypeWrapper(
						typeWrapper.getUnitType(),
						typeWrapper.getExponent() + exponent
				));
				isAlreadyInList = true;
				break;
			}
		}

		//Add to list if not already found in list
		if (!isAlreadyInList) {
			newParentTypes.add(new UnitTypeWrapper(parentType, exponent));
		}

		this.parentTypes = newParentTypes;
		this.simplify();
	}

	public void addParent (UnitType parentType) {
		addParent(parentType, 1);
	}

	private void simplify () {
		//Type cannot be simplified if it has not parent types
		if (parentTypes != null) {

			ArrayList<UnitTypeWrapper> simplifiedParentTypes = new ArrayList<>();

			//Simplify each parent type
			for (UnitTypeWrapper typeWrapper : parentTypes) {
				//Check for simplifiable parent type
				if (!typeWrapper.getUnitType().isBaseType()) {

					//Add each parent type to simplified parent types
					for (UnitTypeWrapper parentTypeWrapper : typeWrapper.getUnitType().getParentTypes()) {
						//Merge type into list of simplified parent types
						boolean isAlreadyInList = false;
						String desiredProperty = parentTypeWrapper.getUnitType().getProperty();

						for (UnitTypeWrapper simplifiedParentTypeWrapper : simplifiedParentTypes) {
							//Add to exponent if type already exists in list
							if (simplifiedParentTypeWrapper.getUnitType().getProperty().equals(desiredProperty)) {
								simplifiedParentTypeWrapper.setExponent(
										simplifiedParentTypeWrapper.getExponent() +
										parentTypeWrapper.getExponent() * typeWrapper.getExponent()
								);

								isAlreadyInList = true;
								break;
							}
						}

						//Add new type if type is not already in list
						if (!isAlreadyInList) {
							simplifiedParentTypes.add(new UnitTypeWrapper(
									parentTypeWrapper.getUnitType(), parentTypeWrapper.getExponent()
							));
						}
					}

				} else {
					//Merge type into list of simplified parent types
					boolean isAlreadyInList = false;
					String desiredProperty = typeWrapper.getUnitType().getProperty();

					for (UnitTypeWrapper simplifiedParentTypeWrapper : simplifiedParentTypes) {
						//Add to exponent if type already exists in list
						if (simplifiedParentTypeWrapper.getUnitType().getProperty().equals(desiredProperty)) {
							simplifiedParentTypeWrapper.setExponent(
									simplifiedParentTypeWrapper.getExponent() + typeWrapper.getExponent());
							isAlreadyInList = true;
							break;
						}
					}

					//Add new type if type is not already in list
					if (!isAlreadyInList) {
						simplifiedParentTypes.add(typeWrapper);
					}
				}
			}

			//Check whether parent types were divided out
			ArrayList<UnitTypeWrapper> toRemove = new ArrayList<>();

			for (UnitTypeWrapper typeWrapper : simplifiedParentTypes) {
				if (typeWrapper.getExponent() == 0) {
					toRemove.add(typeWrapper);
				}
			}

			simplifiedParentTypes.removeAll(toRemove);

			this.parentTypes = simplifiedParentTypes;
		}
	}

	//------------------------[Mutators end here]------------------------//
}
