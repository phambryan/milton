/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package com.ettrema.json;

import com.bradmcevoy.http.AbstractWrappingResponseHandler;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.Response;
import com.bradmcevoy.http.webdav.WebDavResponseHandler;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To enable ajax authentication we MUST NOT return 401 unauthorised, because
 * this will prompt for a browser login box
 *
 * Instead we respond 400 to allow the javascript to handle it.
 *
 * We only know that the authentication request came from javascript because
 * the resource is an AjaxLoginResource
 *
 * @author brad
 */
public class AjaxLoginResponseHandler extends AbstractWrappingResponseHandler {

    private static final Logger log = LoggerFactory.getLogger( AjaxLoginResponseHandler.class );
    private final List<ResourceMatcher> resourceMatchers;

    public AjaxLoginResponseHandler( WebDavResponseHandler responseHandler, List<ResourceMatcher> resourceMatchers ) {
        super( responseHandler );
        this.resourceMatchers = resourceMatchers;
        log.debug( "created" );
    }

    /**
     * Create with a single resource matcher, which matches on AjaxLoginResource's
     *
     * @param responseHandler
     */
    public AjaxLoginResponseHandler( WebDavResponseHandler responseHandler ) {
        super( responseHandler );
        this.resourceMatchers = new ArrayList<ResourceMatcher>();
        this.resourceMatchers.add( new TypeResourceMatcher( AjaxLoginResource.class ) );
        log.debug( "created" );
    }

    /**
     * if the resource is a AjaxLoginResource then return a 403
     *
     * otherwise just do a normal 401
     *
     * @param resource
     * @param response
     * @param request
     */
    @Override
    public void respondUnauthorised( Resource resource, Response response, Request request ) {
        if( log.isWarnEnabled() ) {
            log.warn( "respondUnauthorised: ", resource.getClass() );
        }
        if( matches( resource ) ) {
            log.warn( "unauthorised on wrapped ajax resource" );
            wrapped.respondForbidden( resource, response, request );
        } else {
            log.warn( "using normal unauth" );
            wrapped.respondUnauthorised( resource, response, request );
        }
    }

    private boolean matches( Resource r ) {
        for( ResourceMatcher rm : resourceMatchers ) {
            if( rm.matches( r ) ) return true;
        }
        return false;
    }
}
