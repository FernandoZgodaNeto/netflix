package reducer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class WordCountReducer extends Reducer<Text, Text, Text, LongWritable> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        String keyStr = key.toString();

        if ("WORD".equals(keyStr)) {
            Map<String, Long> wordFrequency = new HashMap<>();

            for (Text value : values) {
                String[] parts = value.toString().split("\t");
                if (parts.length == 2) {
                    String word = parts[0];
                    long count = Long.parseLong(parts[1]);
                    wordFrequency.put(word, wordFrequency.getOrDefault(word, 0L) + count);
                }
            }

            for (Map.Entry<String, Long> entry : wordFrequency.entrySet()) {
                context.write(new Text(entry.getKey()), new LongWritable(entry.getValue()));
            }
        }
    }
}