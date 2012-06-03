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

package com.ettrema.http.carddav;
import com.bradmcevoy.http.Resource;
import com.ettrema.http.report.Report;
import org.jdom.Document;

/**
 * The report can be used to search principals for matching properties
 * and return specified properties for the matching principal resources.
 * To search for an address book owned by a user named "Laurie", the
 * REPORT request body would look like this:
 * 
 * <?xml version="1.0" encoding="utf-8" ?>
 *      <D:principal-property-search xmlns:D="DAV:">
 *          <D:property-search>
 *              <D:prop>
 *                  <D:displayname/>
 *              </D:prop>
 *              <D:match>Laurie</D:match>
 *          </D:property-search>
 *          <D:prop>
 *              <C:addressbook-home-set xmlns:C="urn:ietf:params:xml:ns:carddav"/>
 *              <D:displayname/>
 *          </D:prop>
 *      </D:principal-property-search>
 * 
 * The server performs a case-sensitive or caseless search for a
 * matching string subset of "Laurie" within the DAV:displayname
 * property. Thus, the server might return "Laurie Dusseault", "Laurier
 * Desruisseaux", or "Wilfrid Laurier" all as matching DAV:displayname
 * values, and the address books for each of these.
 * 
 * @author nabil.shams
 */
public class PrincipalPropertySearchReport implements Report{
    @Override
    public String getName() {
            return "principal-property-search";
    }

    @Override
    public String process(String host, String path, Resource r, Document doc) {
            throw new UnsupportedOperationException("Not supported yet.");
    }
}