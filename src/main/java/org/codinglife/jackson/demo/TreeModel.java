package org.codinglife.jackson.demo;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.TreeMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;

public class TreeModel {
	
	@Test
	public void test1() throws JsonParseException, IOException {
		TreeMapper treeMapper = new TreeMapper();
		ObjectNode userRoot = treeMapper.objectNode();
		ObjectNode  nameOb = userRoot.putObject("name");
		nameOb.put("first", "colin");
		nameOb.put("last", "liu");
		userRoot.put("gender", User.Gender.MALE.toString());
		userRoot.put("verified", false);
		byte[] imageData = new byte[3]; // or wherever it comes from
		
		treeMapper.writeTree(userRoot, new File("src/main/resources/user5.json"));
	}
}
