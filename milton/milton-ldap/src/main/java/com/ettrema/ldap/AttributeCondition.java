package com.ettrema.ldap;

import com.ettrema.ldap.Condition.Operator;

/**
 *
 * @author brad
 */
public class AttributeCondition implements Condition {

	protected final String attributeName;
	protected final Operator operator;
	protected final String value;
	protected boolean isIntValue;

	public AttributeCondition(String attributeName, Operator operator, String value) {
		this.attributeName = attributeName;
		this.operator = operator;
		this.value = value;
	}

	public AttributeCondition(String attributeName, Operator operator, int value) {
		this(attributeName, operator, String.valueOf(value));
		isIntValue = true;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * Get attribute name.
	 *
	 * @return attribute name
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Condition value.
	 *
	 * @return value
	 */
	public String getValue() {
		return value;
	}
//
//	public void appendTo(StringBuilder buffer) {
//		Field field = Field.get(attributeName);
//		buffer.append('"').append(field.getUri()).append('"');
//		buffer.append(Conditions.OPERATOR_MAP.get(operator));
//		//noinspection VariableNotUsedInsideIf
//		if (field.cast != null) {
//			buffer.append("CAST (\"");
//		} else if (!isIntValue && !field.isIntValue()) {
//			buffer.append('\'');
//		}
//		if (Operator.Like == operator) {
//			buffer.append('%');
//		}
//		if ("urlcompname".equals(field.alias)) {
//			buffer.append(StringUtil.encodeUrlcompname(StringUtil.davSearchEncode(value)));
//		} else if (field.isIntValue()) {
//			// check value
//			try {
//				Integer.parseInt(value);
//				buffer.append(value);
//			} catch (NumberFormatException e) {
//				// invalid value, replace with 0
//				buffer.append('0');
//			}
//		} else {
//			buffer.append(StringUtil.davSearchEncode(value));
//		}
//		if (Operator.Like == operator || Operator.StartsWith == operator) {
//			buffer.append('%');
//		}
//		if (field.cast != null) {
//			buffer.append("\" as '").append(field.cast).append("')");
//		} else if (!isIntValue && !field.isIntValue()) {
//			buffer.append('\'');
//		}
//	}

	public boolean isMatch(Contact contact) {
		String lowerCaseValue = value.toLowerCase();
		String actualValue = contact.get(attributeName);
		Operator actualOperator = operator;
		// patch for iCal or Lightning search without galLookup
		if (actualValue == null && ("givenName".equals(attributeName) || "sn".equals(attributeName))) {
			actualValue = contact.get("cn");
			actualOperator = Operator.Like;
		}
		if (actualValue == null) {
			return false;
		}
		actualValue = actualValue.toLowerCase();
		return (actualOperator == Operator.IsEqualTo && actualValue.equals(lowerCaseValue))
				|| (actualOperator == Operator.Like && actualValue.contains(lowerCaseValue))
				|| (actualOperator == Operator.StartsWith && actualValue.startsWith(lowerCaseValue));
	}
}