package org.codinglife.jackson.demo;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.codinglife.jackson.demo.User.Gender;
import org.codinglife.jackson.demo.User.Name;
import org.junit.Test;

public class StreamAPI {
	@Test
	public void test1() throws JsonGenerationException, IOException {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(new File(
				"src/main/resources/user6.json"), JsonEncoding.UTF8);

		g.writeStartObject();
		g.writeObjectFieldStart("name");
		g.writeStringField("first", "Joe");
		g.writeStringField("last", "Sixpack");
		g.writeEndObject(); // for field 'name'
		g.writeStringField("gender", Gender.MALE.toString());
		g.writeBooleanField("verified", false);
		g.writeFieldName("userImage"); // no 'writeBinaryField' (yet?)
		byte[] binaryData = new byte[3];
		g.writeBinary(binaryData);
		g.writeEndObject();
		g.close(); // important: will force flushing of output, close underlying
					// output stream
	}

	@Test
	public void test2() throws JsonParseException, IOException {
		JsonFactory f = new JsonFactory();
		JsonParser jp = f.createJsonParser(new File("src/main/resources/user6.json"));
		User user = new User();
		jp.nextToken(); // will return JsonToken.START_OBJECT (verify?)
		while (jp.nextToken() != JsonToken.END_OBJECT) {
			String fieldname = jp.getCurrentName();
			jp.nextToken(); // move to value, or START_OBJECT/START_ARRAY
			if ("name".equals(fieldname)) { // contains an object
				Name name = new Name();
				while (jp.nextToken() != JsonToken.END_OBJECT) {
					String namefield = jp.getCurrentName();
					jp.nextToken(); // move to value
					if ("first".equals(namefield)) {
						name.setFirst(jp.getText());
					} else if ("last".equals(namefield)) {
						name.setLast(jp.getText());
					} else {
						throw new IllegalStateException("Unrecognized field '"
								+ fieldname + "'!");
					}
				}
				user.setName(name);
			} else if ("gender".equals(fieldname)) {
				user.setGender(User.Gender.valueOf(jp.getText()));
			} else if ("verified".equals(fieldname)) {
				user.setVerified(jp.getCurrentToken() == JsonToken.VALUE_TRUE);
			} else if ("userImage".equals(fieldname)) {
				user.setUserImage(jp.getBinaryValue());
			} else {
				throw new IllegalStateException("Unrecognized field '"
						+ fieldname + "'!");
			}
		}
		System.out.println(user);
		jp.close(); // ensure resources get cleaned up timely and properly
	}
	
	@Test
	public void test3() throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = "[{\"foo\": \"bar\"},{\"foo\": \"biz\"}]";
		
		JsonFactory f = new JsonFactory();
		JsonParser jp = f.createJsonParser(json);
		// advance stream to START_ARRAY first:
		jp.nextToken();
		// and then each time, advance to opening START_OBJECT
		while (jp.nextToken() == JsonToken.START_OBJECT) {
			Foo foobar = mapper.readValue(jp, Foo.class);
			// process
			// after binding, stream points to closing END_OBJECT
			
			System.out.println(foobar);
		}
	}
}
