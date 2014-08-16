package org.codinglife.jackson.demo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;

/**
 * ObjectMapper.readValue
 * ObjectMapper.writeValue
 * @author Administrator
 *
 */
public class DataBinding {
	private ObjectMapper mapper;
	
	@Before
	public void before() {
		mapper = new ObjectMapper(); // can reuse, share globally
	}
	
	@Test
	public void test1() throws JsonParseException, JsonMappingException, IOException {
		 User user = mapper.readValue(new File("src/main/resources/user1.json"), User.class);
		 System.out.println(user);
		 mapper.writeValue(new File("src/main/resources/user2.json"), user);
	}
	
	@Test
	public void test2() throws JsonParseException, JsonMappingException, IOException {
		Map<String,Object> userData = mapper.readValue(new File("src/main/resources/user1.json"), Map.class);
		System.out.println(userData);
	}
	
	@Test
	public void test3() throws JsonGenerationException, JsonMappingException, IOException {
		Map<String,Object> userData = new HashMap<String,Object>();
		Map<String,String> nameStruct = new HashMap<String,String>();
		nameStruct.put("first", "Joe");
		nameStruct.put("last", "Sixpack");
		userData.put("name", nameStruct);
		userData.put("gender", "MALE");
		userData.put("verified", Boolean.FALSE);
		userData.put("userImage", "Rm9vYmFyIQ==");
		mapper.writeValue(new File("src/main/resources/user3.json"), userData);
	}
	
	@Test
	public void test4() throws JsonGenerationException, JsonMappingException, IOException {
		String[] strs = new String[]{"hello", "world", "jackson"};
		System.out.println(mapper.writeValueAsString(strs));
		mapper.writeValue(System.out, strs);
	}
	
	@Test
	public void test5() throws JsonParseException, JsonMappingException, IOException {
		String json = "[\"hello\",\"world\",\"jackson\"]";
		String[] strs = mapper.readValue(json, String[].class);
		System.out.println(Arrays.toString(strs));
		strs = mapper.readValue(json, new TypeReference<String[]>(){});
		System.out.println(Arrays.toString(strs));
	}
	
	@Test
	public void test6() throws JsonProcessingException, IOException {
		ObjectMapper m = new ObjectMapper();
		// can either use mapper.readTree(source), or mapper.readValue(source, JsonNode.class);
		// ensure that "last name" isn't "Xmler"; if is, change to "Jsoner"
		// JsonNode rootNode = m.readTree(new File("src/main/resources/user1.json"));
		JsonNode rootNode = m.readValue(new File("src/main/resources/user1.json"), JsonNode.class);
		JsonNode nameNode = rootNode.path("name");
		String lastName = nameNode.path("last").getTextValue();
		if ("xmler".equalsIgnoreCase(lastName)) {
		  ((ObjectNode)nameNode).put("last", "Jsoner");
		}
		// and write it out:
		m.writeValue(new File("src/main/resources/user4.json"), rootNode);
	}
}


