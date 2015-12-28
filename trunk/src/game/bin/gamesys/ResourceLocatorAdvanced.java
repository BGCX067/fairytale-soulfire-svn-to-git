package game.bin.gamesys;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

public class ResourceLocatorAdvanced implements ResourceLocator{

	protected URI baseDir;

    public ResourceLocatorAdvanced(URI baseDir) {
        if (baseDir == null) {
            throw new NullPointerException("baseDir can not be null.");
        }
        this.baseDir = baseDir;
    }

    public ResourceLocatorAdvanced(URL baseDir) throws URISyntaxException {
        if (baseDir == null) {
            throw new NullPointerException("baseDir can not be null.");
        }
        this.baseDir = baseDir.toURI();
    }
    
    public URL locateResource(String resourceName) {
        // Trim off any prepended local dir.
        while (resourceName.startsWith("./") && resourceName.length() > 2) {
            resourceName = resourceName.substring(2);
        }
        while (resourceName.startsWith(".\\") && resourceName.length() > 2) {
            resourceName = resourceName.substring(2);
        }

        // Try to locate using resourceName as is.
        try {
            String spec = URLEncoder.encode( resourceName, "UTF-8" );
            //this fixes a bug in JRE1.5 (file handler does not decode "+" to spaces)
            spec = spec.replaceAll( "\\+", "%20" );

            URL rVal = new URL( baseDir.toURL(), spec );
            // open a stream to see if this is a valid resource
            // XXX: Perhaps this is wasteful?  Also, what info will determine validity?
            rVal.openStream().close();
            return rVal;
        } catch (IOException e) {
            // URL wasn't valid in some way, so try up a path.
        } catch (IllegalArgumentException e) {
            // URL wasn't valid in some way, so try up a path.
        }
    
        resourceName = trimResourceName(resourceName);
        if (resourceName == null) {
            return null;
        } else {
            return locateResource(resourceName);
        }
    }

    protected String trimResourceName(String resourceName) {
        // we are sure this is part of a URL so using slashes only is fine:
        final int firstSlashIndex = resourceName.indexOf( '/' );
        if ( firstSlashIndex >= 0 && firstSlashIndex < resourceName.length() - 1 )
        {
            return resourceName.substring( firstSlashIndex + 1 );
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResourceLocatorAdvanced) {
            return baseDir.equals(((ResourceLocatorAdvanced)obj).baseDir);
        }
        return false;
    }
}
