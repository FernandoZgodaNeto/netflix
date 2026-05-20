package reducer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class StatsReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        String keyStr = key.toString();

        if ("LONGEST-SHORTEST".equals(keyStr)) {
            Map<Integer, List<String>> sizeToTitles = new TreeMap<>(Collections.reverseOrder());

            for (Text value : values) {
                String[] parts = value.toString().split("\t", 2);
                if (parts.length >= 2) {
                    String title = parts[0];
                    int wordCount = Integer.parseInt(parts[1]);

                    sizeToTitles.computeIfAbsent(wordCount, k -> new ArrayList<>()).add(title);
                }
            }

            if (!sizeToTitles.isEmpty()) {
                int maxSize = sizeToTitles.keySet().iterator().next();
                String longestTitle = String.join(", ", sizeToTitles.get(maxSize));
                context.write(new Text("LONGEST_DESCRIPTION"),
                        new Text(longestTitle + " (" + maxSize + " palavras)"));

                int minSize = (Integer) sizeToTitles.keySet().toArray()[sizeToTitles.size() - 1];
                String shortestTitle = String.join(", ", sizeToTitles.get(minSize));
                context.write(new Text("SHORTEST_DESCRIPTION"),
                        new Text(shortestTitle + " (" + minSize + " palavras)"));
            }
        } else if ("TOTAL_WORDS".equals(keyStr)) {
            long totalWords = 0;
            for (Text value : values) {
                totalWords += Long.parseLong(value.toString());
            }
            context.write(new Text("TOTAL_WORDS_COUNT"), new Text(String.valueOf(totalWords)));
        }
    }
}