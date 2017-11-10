package com.boilerplate.framework;

import java.io.IOException;
import java.io.Writer;

import com.boilerplate.java.collections.BoilerplateList;
/**
 * This is the utility of write any data into csv files.
 * @author love
 *
 */
public class CSVUtils {
	/**
	 * This is the default seperator for csv data.
	 */
	private static final char DEFAULT_SEPARATOR = ',';
	/**
	 * This method write a row with default seperator
	 * @param writer The writer instance
	 * @param valuesList  The values list
	 * @throws IOException The IO exception
	 */
    public static void writeLine(Writer writer, BoilerplateList<String> valuesList) throws IOException {
        writeLine(writer, valuesList, DEFAULT_SEPARATOR, ' ');
    }
    /**
	 * This method write a row with particular seperator
	 * @param writer The writer instance
	 * @param valuesList  The values list
	 * @throws IOException The IO exception
	 */
    public static void writeLine(Writer writer, BoilerplateList<String> valuesList, char separators) throws IOException {
        writeLine(writer, valuesList, separators, ' ');
    }

    //https://tools.ietf.org/html/rfc4180
    private static String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }
   
    /**
     * This method write data into csv files.
     * @param writer The writer
     * @param values The values
     * @param separators The seperator
     * @param customQuote The customQuote
     * @throws IOException The IO exception.
     */
    public static void writeLine(Writer writer, BoilerplateList<String> values, char separators, char customQuote) throws IOException {

        boolean first = true;

        //default customQuote is empty

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<values.size();i++){
        	
	        if (!first) {
	            sb.append(separators);
	        }
	        if (customQuote == ' ') {
	            sb.append(followCVSformat((String)values.get(i)));
	        } else {
	            sb.append(customQuote).append(followCVSformat((String)values.get(i))).append(customQuote);
	        }
	
	        first = false;
        }
        sb.append("\n");
        writer.append(sb.toString());


    }

}
