/**
 * Copyright 2012 MARSEC-XL International Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.marssa.demonstrator.web_services;


import org.marssa.demonstrator.constants.Constants;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.Directory;

/**
 * @author Clayton Tabone
 *
 */
public class StaticFileServerApplication extends Application {
	/**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        return new Directory(getContext(), "file:///" + Constants.SYSTEM.ROOT_URI.getContents());
    }

}
