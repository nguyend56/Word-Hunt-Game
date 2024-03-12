
package cs260.Model; 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class emojiParser{
    private final File jsonFile;

    /**
     * initializes parser obj with the emoji json file
     */
    public emojiParser(){
        this.jsonFile = new File("emoji.json");
    }  


    /**
     * creates a map that connects the keyword (word input) to the corresponding
     * unicode (emoji 'key') in the json file
     */
    private Map<String, String[]> parse() throws IOException{
        Map<String, String[]> map = new HashMap<String, String[]>();
        ObjectMapper om = new ObjectMapper();

        //jsonobject
        JsonNode rootNode = om.readTree(jsonFile);
        Iterator<String> fieldNames = rootNode.fieldNames();
        
        while(fieldNames.hasNext()){
            String name  = fieldNames.next();
            JsonNode emojiNode = rootNode.get(name);
            String unicode = emojiNode.get("unicode").asText();
            JsonNode keywordNode = emojiNode.get("keywords");

            String[] keywords = new String[keywordNode.size()];
            for (int i = 0; i < keywordNode.size(); i++){
                keywords[i] = keywordNode.get(i).asText();
            }

            map.put(unicode, keywords);
        }
        
        return map;
    }

    /**
     * returns the string unicode of the input word
     * return null if no emoji
     */
    public String findEmoji(String input){
        Map<String, String[]> map;
        try {
            map = this.parse();
        } catch (IOException e) {
            return null;
        }
        for (Map.Entry<String, String[]> entry : map.entrySet()){
            for(String keyword: entry.getValue()){ //get value is array of strings 
                if(keyword.equalsIgnoreCase(input)){
                    String jsonUnicode = entry.getKey();
                    return unicodeConversion(jsonUnicode);
                }
            }
        }
        return null;
    }

    /**
     * converts the unicode to a hex number readabel by java 
     * @param entry the entry value of the map or the keyword to find the emoji
     * unicode for
     * @return string version of the unicode 
     */
    private String unicodeConversion(String entry){
        int codePoint = Integer.parseInt(entry, 16);
        String unicodeChar = new String(Character.toChars(codePoint));
        return unicodeChar;
    }

    public static void main(String[] args) throws IOException {
        emojiParser parser = new emojiParser();
        String keyword = "house"; // Example keyword
        String emojiUnicode = parser.findEmoji(keyword);
        if (emojiUnicode != null){
            System.out.println("Unicode for '" + keyword + "': " + emojiUnicode);
        } else {
            System.out.println("No unicode found for " + keyword);
        }

    }

}