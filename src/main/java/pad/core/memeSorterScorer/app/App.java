package pad.core.memeSorterScorer.app;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

/***************************************************************************
 * Read in a list of internet memes from a json file on the classpath.
 * Create one method which takes the list of memes and sorts them by name.
 * Create a second method which associates a "lulz" score (from 1-10)
 * with each meme and writes the updated values to the same json file.
 *
 * @author Jermaine R. deFerrante
 * @since February 8, 2016
 ***************************************************************************/
public class App {

	/** Class members **/
	private static Logger log = LogManager.getLogger(App.class.getName());
	private static ObjectMapper objectMapper = new ObjectMapper();
	private static String inputFile = "Memes.json";
	private static String jsonKeyOrgMemeList = "originalMemeList";
	private static String jsonKeySortedMemeList = "sortedMemeList";

	public static void main(String[] args) {
		// Read input file
		JsonNode input = readJson(inputFile);
		log.info("Input file: " + input.toString());
		// Sort names and add score
		List<String> sortedList = sortKeys(input.get(jsonKeyOrgMemeList));
		log.info("Sorted list: " + sortedList.toString());
		// Add list to JSON object
		((ObjectNode) input).putPOJO(jsonKeySortedMemeList, sortedList);
		log.info("Output json: " + input.toString());
		// Write to file
		writeJson(inputFile, input);
	}

	/**
	 * Reads the contents of 'fileName' and returns JsonNode representation.
	 *
	 * @param fileName : input file name
	 * @return JsonNode : represents the data read in from the JSON file
	 **/
	private static JsonNode readJson(String fileName) {
		JsonNode jsonObject = null;
		InputStream inputStream = null;
		try {
			 inputStream = App.class.getClassLoader()
					.getResourceAsStream(fileName);
		} catch (Exception e) {
			log.info("Exception thrown when reading file.", e);
		}
		try {
			jsonObject = objectMapper.readTree(inputStream);
		} catch (Exception e) {
            log.info("Exception thrown when constructing a JsonNode.", e);
		}
		if (jsonObject == null) {
			System.exit(0);
		}
		return jsonObject;
	}

	/**
	 * Writes the contents of 'obj' to the file - 'fileName'
	 *
	 * @param fileName : output file name
	 * @param obj : json object to write to the file
	 **/
	private static void writeJson(String fileName, JsonNode obj) {
		try {
			objectMapper.writeValue(new File(App.class.getClassLoader().getResource(fileName).getFile()), obj);
		} catch (Exception e) {
            log.info("Exception thrown when writing a JsonNode to file.", e);
		}
	}

	/**
	 * Appends a random number between [1-10] to 'key'
	 *
	 * @param key : key to append to
	 * @return int : random number between [1-10]
	 **/
	private static String addRandomScore(String key) {
        Random rand = new Random();
        int randomNum = rand.nextInt(10);
        return key + "_lulzScore=" + (randomNum + 1);
    }

    /**
     * Sorts the keys of the JsonNode 'obj'. Returns a sorted list.
     *
     * @param obj : JSON object
     * @return List<String> : sorted list of key names
     **/
	private static List<String> sortKeys(JsonNode obj) {
    	List<String> list = new ArrayList<String>();
    	Iterator<String> keys = obj.getFieldNames();
    	while (keys.hasNext()) {
    		list.add(addRandomScore(keys.next()));
    	}
    	Collections.sort(list);
    	return list;
    }
}