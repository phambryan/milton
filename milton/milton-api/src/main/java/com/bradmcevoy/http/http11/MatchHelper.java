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
 */
package com.bradmcevoy.http.http11;

import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Resource;

/**
 *
 * @author brad
 */
public interface MatchHelper {

	/**
	 * Check if the resource has been modified based on etags
	 *
	 * Returns true if the match comparison indicates that the resource has NOT
	 * been modified
	 *
	 * Ie, returning "true" means to continue with PUT processing. Returning "false"
	 * means that the comparison indicates that processing should not continue
	 *
	 * @param r
	 * @param req
	 * @return
	 */
	boolean checkIfMatch(Resource r, Request req);

	/**
	 * Returns true if none of the given etags match those given in the if-none-match header
	 *
	 * In the usual use case of GET returning false means "do nothing different", ie continue processing.
	 *
	 * @param handler
	 * @param req
	 * @return
	 */
	boolean checkIfNoneMatch(Resource r, Request req);
    
}
