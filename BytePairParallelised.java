import java.util.*;
import java.util.concurrent.*;

public class BytePairParallelised {
    // Parallelises the process of encoding
    public String encode(String input, HashSet<Character> charSet) throws InterruptedException, ExecutionException{
        int coreCount = 4;

        // Load input into input buffer
        ArrayList<String> inputBuffer = new ArrayList<>();
        int i = 0, j = 3;
        while (j < input.length()){
            inputBuffer.add(input.substring(i, j));
            j+=4; i+=4;
        }

        ExecutorService pool = Executors.newFixedThreadPool(coreCount);
        List<Callable<String>> tasks = new ArrayList<>();
        for (String chunk: inputBuffer){
            tasks.add(() -> {
                Map<String, Integer> map = new HashMap<>();
                populateMap(map, chunk);
                String mostFrequent = findMostFrequentPair(map);
                String replacement = findReplacement(chunk, charSet);
                String newChunk = replaceString(chunk, mostFrequent, replacement);
                return newChunk;
            });
        }

        // Invoke all the tasks and collect the results 
        List<Future<String>> results;
        try {
            results = pool.invokeAll(tasks);
        } catch (InterruptedException e)
        {throw new InterruptedException("pool.invokeAll failed to invoke");}
        
        // Combine all strings and return encoded result
        StringBuilder encodedString = new StringBuilder();
        for (Future<String> result: results){
            String resultChunk;
            try {
                resultChunk = result.get();
            } catch (InterruptedException e)
            {throw new InterruptedException("result.get() failed to invoke");}

            encodedString.append(resultChunk);
        }
        
        pool.shutdown();   
        return encodedString.toString();
    }

    // Populates map with pairs
    private static void populateMap(Map<String, Integer> map, String input){
        if (input.length() < 2){
            return;
        }

        int j = 0, i = 1;
        while (i <= input.length()){
            String substr = input.substring(j, i);
            if (map.containsKey(substr)){
                map.put(substr, map.get(substr) + 1);
                i++; j++;
                continue;
            }

            map.put(substr, 0);
            i++; j++;
        }
    }
    
    // Checks the most frequent pair
    private static String findMostFrequentPair(Map<String, Integer> map){
        Map.Entry<String, Integer> mostFrequentKeyValuePair = null;

        for (Map.Entry<String, Integer> entry: map.entrySet()){
            if (mostFrequentKeyValuePair == null || mostFrequentKeyValuePair.getValue() < entry.getValue()){
                mostFrequentKeyValuePair = entry;
            }
        }

        return mostFrequentKeyValuePair.getKey();
    }

    private static String replaceString(String input, String mostFrequentPair, String replacement){
        int i = 0, j = 1;
        StringBuilder stringWithReplacement = new StringBuilder();

        while (j < input.length()){
            if (input.substring(i, j).equals(mostFrequentPair)){
                stringWithReplacement.append(replacement);
                i+=2; j+=2;
                continue;
            }

            stringWithReplacement.append(input.charAt(i));
            i++; j++;

        }
        return stringWithReplacement.toString();
    }

    // Decides on a replacement character based on characters not in the input
    private static String findReplacement(String input, HashSet<Character> charSet){
        // Convert the input to a set, then perform set difference on the set of all possible replacements
        HashSet<Character> tempSet = new HashSet<>();
        for (char inputChar: input.toCharArray()){
            tempSet.add(inputChar);
        }

        HashSet<Character> copyCharSet = new HashSet<>(charSet);
        copyCharSet.removeAll(tempSet);

        // Now just choose random index
        Random random = new Random();
        int index = random.nextInt(copyCharSet.size());
        
        // Type gymnastics here, can be reworked, probably
        return String.valueOf(new ArrayList<>(copyCharSet).get(index));
    }
}