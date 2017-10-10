/**
 * 
 */
package com.elastica.beatle;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBException;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;

/**
 * @author anuvrath
 *
 */
public class MarshallingUtils {
	
	/**
	 * @param data
	 * @param klass
	 * @return
	 * @throws JAXBException
	 */
	public static <T> T unmarshall(String data, final Class<T> klass) throws JAXBException {		
		return unmarshallJSON(data, klass);		
	}

	/**
	 * @param json
	 * @param klass
	 * @return
	 */
	protected static <T> T unmarshallJSON(final String json, final Class<T> klass) {
		final ObjectMapper mapper = new ObjectMapper();
		final AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		mapper.setAnnotationIntrospector(introspector);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return mapper.readValue(json, klass);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	public static String marshall(final Object object) throws JAXBException {

		try {
			return marshallJSON(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static String marshallJSON(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		mapper.setAnnotationIntrospector(introspector);
		mapper.setAnnotationIntrospector(introspector);
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, object);
		return writer.toString();
	}
}
