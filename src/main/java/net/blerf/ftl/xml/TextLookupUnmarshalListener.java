package net.blerf.ftl.xml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.Unmarshaller;
import java.util.HashMap;
import java.util.Map;


/**
 * Substitutes string values using an existing lookup Map.
 *
 * After each DeferredText field is unmarshalled, this will call
 * setResolvedText() on it, unescaping newlines in the process.
 *
 * @see net.blerf.ftl.xml.DeferredText
 */
public class TextLookupUnmarshalListener extends Unmarshaller.Listener {

	private static final Logger log = LogManager.getLogger();

	private Map<String, String> lookupMap = new HashMap<String, String>();


	public TextLookupUnmarshalListener() {
	}

	/**
	 * After construction, get this Map and put entries into it.
	 */
	public Map<String, String> getLookupMap() {
		return lookupMap;
	}

	@Override
	public void afterUnmarshal( Object target, Object parent ) {

		if ( target instanceof DeferredText ) {
			DeferredText deferredText = (DeferredText)target;

			if ( deferredText.getTextId() != null ) {
				String textId = deferredText.getTextId();

				if ( lookupMap.containsKey( textId ) ) {
					String resolvedText = lookupMap.get( textId ).replaceAll( "\\n", "\n" );
					deferredText.setResolvedText( resolvedText );
				}
				else {
					log.warn( "Text lookup failed for id: "+ textId );
				}
			}
		}
	}
}
