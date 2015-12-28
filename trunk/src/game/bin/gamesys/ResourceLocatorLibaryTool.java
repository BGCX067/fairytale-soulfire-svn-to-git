package game.bin.gamesys;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceLocatorLibaryTool {
	 private static final Logger logger = Logger.getLogger(ResourceLocatorLibaryTool.class
	            .getName());

	    public static final String TYPE_TEXTURE = "texture";
	    public static final String TYPE_EFFECT = "effect";
	    public static final String TYPE_MODEL = "model";
	    public static final String TYPE_PARTICLE = "particle";
	    public static final String TYPE_AUDIO = "audio";
	    public static final String TYPE_SOUND = "sound";
	    public static final String TYPE_SHADER = "shader";

	    private static final Map<String, ArrayList<ResourceLocator>> locatorMap = new HashMap<String, ArrayList<ResourceLocator>>();

	    public static URL locateResource(String resourceType, String resourceName) {
	        if (resourceName == null) {
	            return null;
	        }
	        synchronized (locatorMap) {
	            ArrayList<ResourceLocator> bases = locatorMap.get(resourceType);
	            if (bases != null) {
	                for (int i = bases.size(); --i >= 0; ) {
	                    ResourceLocator loc = bases.get(i);
	                    URL rVal = loc.locateResource(resourceName);
	                    if (rVal != null) {
	                        return rVal;
	                    }
	                }
	            }
	            // last resort...
	            try {
	                URL u = ResourceLocatorLibaryTool.class.getResource(resourceName);
	                if (u != null) {
	                    return u;
	                }
	            } catch (Exception e) { 
	                logger.logp(Level.WARNING, ResourceLocatorLibaryTool.class.getName(),
	                        "locateResource(String, String)", e.getMessage(), e);
	            }

	            logger.warning("Unable to locate: "+resourceName);
	            return null;
	        }
	    }

	    public static void addResourceLocator(String resourceType,
	            ResourceLocator locator) {
	        if (locator == null) return;
	        synchronized (locatorMap) {
	            ArrayList<ResourceLocator> bases = locatorMap.get(resourceType);
	            if (bases == null) {
	                bases = new ArrayList<ResourceLocator>();
	                locatorMap.put(resourceType, bases);
	            }

	            if (!bases.contains(locator)) {
	                bases.add(locator);
	            }
	        }
	    }

	    public static boolean removeResourceLocator(String resourceType,
	            ResourceLocator locator) {
	        synchronized (locatorMap) {
	            ArrayList<ResourceLocator> bases = locatorMap.get(resourceType);
	            if (bases == null) {
	                return false;
	            }
	            return bases.remove(locator);
	        }
	    }
}
