package com.risksense.converters;

import java.io.File;
import java.io.IOException;

/**
 * Factory class for creating instances of {@link XMLJSONConverterI}.
 */
public final class ConverterFactory {

    /**
     * You should implement this method having it return your version of
     * {@link com.risksense.converters.XMLJSONConverterI}.
     *
     * @return {@link com.risksense.converters.XMLJSONConverterI} implementation you created.
     */
    public static final XMLJSONConverterI createXMLJSONConverter() {
    	XMLJSONConverterI jsonConverter = new XMLJSONConverterImpl();
    	File inputFile = new File("C:\\Users\\amithrajeev\\Desktop\\example.json");
    	File outputFile = new File("C:\\Users\\amithrajeev\\Desktop\\output.xml");
    	try {
			jsonConverter.convertJSONtoXML(inputFile, outputFile);
		} catch (IOException e) {
			throw new UnsupportedOperationException("Not a valid json!");
		}
    	return jsonConverter;
    }
    
//    public static void main(String[] args) {
//		createXMLJSONConverter();
//	}
}
