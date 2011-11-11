/**
 * 
 */
package mise.marssa.demonstrator.web_services;

import mise.marssa.demonstrator.constants.Constants;

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
