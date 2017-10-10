package com.universal.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;

import com.universal.util.Utility;

public class CommonTest extends CommonTestClient{
	public List<NameValuePair> queryParams;
	public List<NameValuePair> headers;
	//Properties properties ;
	
	
	public CommonTest() throws Exception {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		queryParams = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Accept", "application/json"));
	}
	
	public static String getResponseBody(HttpResponse response) {
		try {
			return EntityUtils.toString(response.getEntity());

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getResponseStatusCode(HttpResponse response) {
		return response.getStatusLine().getStatusCode();

	}
	
	public static <T> T unmarshallJSON(InputStream is, Class<T> klass)
			throws Exception {
		try {

			ObjectMapper objectMapper = new ObjectMapper();
			AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
			objectMapper.setAnnotationIntrospector(introspector);
			objectMapper.setAnnotationIntrospector(introspector);
			return objectMapper.readValue(is, klass);
		} catch (Exception e) {
			throw e;
		}
	}
	

	/**
	 * 
	 * @param <T>
	 * @param data
	 * @param klass
	 * @return
	 * @throws JAXBException 
	 */
	public <T> T unmarshall(String data, final Class<T> klass) throws JAXBException {		
		return unmarshallJSON(data, klass);		
	}
	
	protected static <T> T unmarshallJSON(final String json, final Class<T> klass) {
		final ObjectMapper mapper = new ObjectMapper();
		final AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		// make deserializer use JAXB annotations (only)
		mapper.setAnnotationIntrospector(introspector);
		// make serializer use JAXB annotations (only)		
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			return mapper.readValue(json, klass);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public String marshall(final Object object) throws JAXBException {
		
		try {
			return marshallJSON(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String marshallJSON(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().withSerializationInclusion(Inclusion.NON_NULL); 
		AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		// make deserializer use JAXB annotations (only)
		mapper.setAnnotationIntrospector(introspector);
		// make serializer use JAXB annotations (only)
		mapper.setAnnotationIntrospector(introspector);
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, object);
		return writer.toString();
	}
}
