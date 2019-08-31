package com.risksense.converters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class XMLJSONConverterImpl implements XMLJSONConverterI {

	@Override
	public void convertJSONtoXML(File json, File xml) throws IOException {
		try(BufferedReader reader = new BufferedReader(new FileReader(json))) {
			StringBuilder builder = new StringBuilder();
			StringBuilder xmlBuilder = new StringBuilder();
			xmlBuilder.append("<object>");
			String line = reader.readLine();
			while(null != line) {
				builder.append(line);
				line = reader.readLine();
			}
			JsonFactory factory = new JsonFactory();

			ObjectMapper mapper = new ObjectMapper(factory);
			JsonNode rootNode = mapper.readTree(builder.toString());  

			Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry<String,JsonNode> field = fieldsIterator.next();
				String key = field.getKey();
				Object jsonobj = field.getValue();
				checkInstance(key,jsonobj,xmlBuilder);
			}
			xmlBuilder.append("</object>");
			System.out.println(xmlBuilder.toString());
			FileWriter writer = new FileWriter(xml);
			writer.write(xmlBuilder.toString());
			writer.close();
		}

	}

	private void checkInstance(String key,Object jsonObj, StringBuilder xmlBuilder) {
		JsonNode node = (JsonNode)jsonObj;
		if(node.isBoolean()) {
			xmlBuilder.append("<boolean name=\"").append(key).append("\">").append(jsonObj).append("</boolean>");
		} else if(node.isNull()) {
			xmlBuilder.append("<null name=\"").append(key).append("\"/>");
		} else if(node.isTextual()) {
			xmlBuilder.append("<string name=\"").append(key).append("\">").append(jsonObj.toString().replace("\"", "")).append("</string>");
		}  else if(node.isInt() || node.isFloat() || node.isLong() || node.isDouble()) {
			xmlBuilder.append("<number name=\"").append(key).append("\">").append(jsonObj).append("</number>");
		} else if(node.isArray()) {
			xmlBuilder.append("<array name=\"").append(key).append("\">");
			checkJsonArray(node,xmlBuilder);
			xmlBuilder.append("</array>");
		} else if(node.isObject()) {
			xmlBuilder.append("<object name=\"").append(key).append("\">");
			checkJsonObject(node,xmlBuilder);
			xmlBuilder.append("</object>");
		}
	}

	private void checkJsonObject(JsonNode jsonObj, StringBuilder xmlBuilder) {
		Iterator<Map.Entry<String,JsonNode>> fieldsIterator = jsonObj.fields();
		while (fieldsIterator.hasNext()) {
			Map.Entry<String,JsonNode> field = fieldsIterator.next();
			String key = field.getKey();
			Object jsonobj = field.getValue();
			checkInstance(key,jsonobj,xmlBuilder);
		}
	}

	private void checkJsonArray(JsonNode jsonObj, StringBuilder xmlBuilder) {
		for(JsonNode arrayObj : jsonObj) {
			if(arrayObj.isNull()) {
				xmlBuilder.append("<null/>");
			}else if(arrayObj.isTextual()) {
				xmlBuilder.append("<string>").append(arrayObj.toString().replace("\"", "")).append("</string>");
			} else if(arrayObj.isInt() || arrayObj.isFloat() || arrayObj.isLong() || arrayObj.isDouble()) {
				xmlBuilder.append("<number>").append(arrayObj).append("</number>");
			} else if(arrayObj.isBoolean()) {
				xmlBuilder.append("<boolean>").append(arrayObj).append("</boolean>");
			} else if(arrayObj.isArray()) {
				xmlBuilder.append("<array>");
				checkJsonArray(arrayObj,xmlBuilder);
				xmlBuilder.append("</array>");
			} else if(arrayObj.isObject()) {
				xmlBuilder.append("<object>");
				checkJsonObject(arrayObj,xmlBuilder);
				xmlBuilder.append("</object>");
			}
		}
	}
}
