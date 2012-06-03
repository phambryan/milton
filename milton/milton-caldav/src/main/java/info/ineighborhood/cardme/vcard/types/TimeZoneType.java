/*
 * Copyright (C) 2012 McEvoy Software Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package info.ineighborhood.cardme.vcard.types;

import info.ineighborhood.cardme.util.ISOFormat;
import info.ineighborhood.cardme.util.ISOUtils;
import info.ineighborhood.cardme.util.Util;
import info.ineighborhood.cardme.vcard.EncodingType;
import info.ineighborhood.cardme.vcard.VCardType;
import info.ineighborhood.cardme.vcard.features.TimeZoneFeature;
import info.ineighborhood.cardme.vcard.types.parameters.ParameterTypeStyle;
import info.ineighborhood.cardme.vcard.types.parameters.TimeZoneParameterType;
import java.util.TimeZone;

/**
 * Copyright (c) 2004, Neighborhood Technologies
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of Neighborhood Technologies nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * 
 * @author George El-Haddad
 * <br/>
 * Feb 4, 2010
 *
 */
public class TimeZoneType extends Type implements TimeZoneFeature {

	private TimeZone timeZone = null;
	private int hourOffset = 0;
	private int minuteOffset = 0;
	private String textValue = null;
	private TimeZoneParameterType timeZoneParameterType = null;
	
	public TimeZoneType() {
		super(EncodingType.EIGHT_BIT, ParameterTypeStyle.PARAMETER_VALUE_LIST);
	}
	
	public TimeZoneType(TimeZone timeZone) {
		super(EncodingType.EIGHT_BIT, ParameterTypeStyle.PARAMETER_VALUE_LIST);
		setTimeZone(timeZone);
	}
	
	public TimeZoneType(int hourOffset, int minuteOffset) {
		super(EncodingType.EIGHT_BIT, ParameterTypeStyle.PARAMETER_VALUE_LIST);
		setHourOffset(hourOffset);
		setMinuteOffset(minuteOffset);
	}
	
	public TimeZoneType(String textValue) {
		super(EncodingType.EIGHT_BIT, ParameterTypeStyle.PARAMETER_VALUE_LIST);
		setTextValue(textValue);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TimeZone getTimeZone()
	{
		return timeZone;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHourOffset()
	{
		return hourOffset;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getMinuteOffset()
	{
		return minuteOffset;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getIso8601Offset()
	{
		return ISOUtils.toISO8601_TimeZone(timeZone, ISOFormat.ISO8601_EXTENDED);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTextValue()
	{
		return textValue;
	}

	/**
	 * {@inheritDoc}
	 */
	public TimeZoneParameterType getTimeZoneParameterType()
	{
		return timeZoneParameterType;
	}
	
	public void parseTimeZoneOffset(String iso8601Offset) {
		String[] tz = iso8601Offset.split(":");
		hourOffset = Integer.parseInt(tz[0]);
		minuteOffset = Integer.parseInt(tz[1]);
		calculateTimeZone();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTimeZoneParameterType(TimeZoneParameterType timeZoneParameterType) {
		this.timeZoneParameterType = timeZoneParameterType;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHourOffset(int hourOffset) {
		this.hourOffset = hourOffset;
		calculateTimeZone();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMinuteOffset(int minuteOffset) {
		this.minuteOffset = minuteOffset;
		calculateTimeZone();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTextValue(String textValue) {
		this.textValue = textValue;
		hourOffset = 0;
		minuteOffset = 0;
		timeZone = null;
	}

	/**
	 * {@inheritDoc}
	 */
	private void calculateTimeZone() {
		int offsetMillis = hourOffset + (minuteOffset / 10);
		offsetMillis = (((offsetMillis * 60) * 60) * 1000);
		timeZone.setRawOffset(offsetMillis);
		textValue = ISOUtils.toISO8601_TimeZone(timeZone, ISOFormat.ISO8601_EXTENDED);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTypeString()
	{
		return VCardType.TZ.getType();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null) {
			if(obj instanceof TimeZoneType) {
				if(this == obj || ((TimeZoneType)obj).hashCode() == this.hashCode()) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return Util.generateHashCode(toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName());
		sb.append("[ ");
		if(encodingType != null) {
			sb.append(encodingType.getType());
			sb.append(",");
		}
		
		if(timeZone != null) {
			sb.append(timeZone.getRawOffset());
			sb.append(",");
		}
		
		if(timeZoneParameterType != null) {
			sb.append(timeZoneParameterType.getType());
			sb.append(",");
		}

		if(super.id != null) {
			sb.append(super.id);
			sb.append(",");
		}
		
		sb.deleteCharAt(sb.length()-1);	//Remove last comma.
		sb.append(" ]");
		return sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TimeZoneFeature clone()
	{
		TimeZoneType cloned = new TimeZoneType();
		
		if(timeZone != null) {
			TimeZone tz = TimeZone.getDefault();
			tz.setRawOffset(timeZone.getRawOffset());
			cloned.setTimeZone(tz);
		}
		else if(textValue != null) {
			cloned.setTextValue(new String(textValue));
		}
		else {
			cloned.setTextValue(null);
			cloned.setTimeZone(null);
		}
		
		if(timeZoneParameterType != null) {
			cloned.setTimeZoneParameterType(timeZoneParameterType);
		}
		
		cloned.setParameterTypeStyle(getParameterTypeStyle());
		cloned.setEncodingType(getEncodingType());
		cloned.setID(getID());
		return cloned;
	}
}
