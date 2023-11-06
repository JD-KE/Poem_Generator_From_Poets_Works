import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class ReadingPoems {

    public static void main(String[] args) throws Exception {
        File poemFile = new File("");
        Map<String,Map<String,Long>> nextWordDict = new TreeMap<>();
        
        if (args.length > 0){
            poemFile = new File(args[0]);
        } else {
            System.out.println("No input detected.");
            System.exit(1);
        }

        System.out.println(poemFile.getName());

        if (!poemFile.exists() || poemFile.getName() == "") {
            System.out.println("File does not exist.");
            System.exit(1);
        } else if (poemFile.isFile()) {
            nextWordDict = ReadingPoem.readPoem(poemFile);
            

        } else if (poemFile.isDirectory()) {
            System.out.println("File is directory, reading files inside");
            File[] files = poemFile.listFiles();
            Map<String,Map<String,Long>> tempDict = new TreeMap<>();

            for (File file : files) {
                
                Map<String, Map<String, Long>> nextDict = ReadingPoem.readPoem(file);
                nextDict.forEach((word, nextWordMap) -> 
                    tempDict.merge(word, nextWordMap, (v1, v2) -> 
                        {
                            v2.forEach((key, value) ->
                                v1.merge(key, value, (u1, u2) -> u1 + u2 ));
                            return v1;
                        }));

            }

            nextWordDict = tempDict;
        }

        

        for (String word : nextWordDict.keySet()) {
            System.out.printf("%s:%n", word);
            Map<String, Long> nextWordMap = nextWordDict.get(word);
            for(String nextWord: nextWordMap.keySet()) {
                System.out.printf("\t%s: %d times%n", nextWord, nextWordMap.get(nextWord));
            }   
        }
        
    }
    
}
