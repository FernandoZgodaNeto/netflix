package mapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import utils.TextNormalizer;

import java.io.IOException;

public class DescriptionMapper extends Mapper<LongWritable, Text, Text, Text> {

    private Text outputKey = new Text();
    private Text outputValue = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        if (line.startsWith("show_id")) {
            return;
        }

        String[] fields = parseCSV(line);

        if (fields.length < 12) {
            return;
        }

        String title = fields[2].trim();
        String description = fields[11].trim();

        if (description == null || description.isEmpty()) {
            return;
        }

        String normalizedDesc = TextNormalizer.normalize(description);

        if (normalizedDesc.isEmpty()) {
            return;
        }

        String[] words = TextNormalizer.getWords(normalizedDesc);
        int wordCount = words.length;

        outputKey.set("LONGEST-SHORTEST");
        outputValue.set(title + "\t" + wordCount + "\t" + normalizedDesc);
        context.write(outputKey, outputValue);

        String[] wordsWithoutStopwords = TextNormalizer.getWordsWithoutStopwords(normalizedDesc);
        for (String word : wordsWithoutStopwords) {
            if (!word.isEmpty()) {
                outputKey.set("WORD");
                outputValue.set(word + "\t1");
                context.write(outputKey, outputValue);
            }
        }

        outputKey.set("TOTAL_WORDS");
        outputValue.set(String.valueOf(wordCount));
        context.write(outputKey, outputValue);
    }

    private String[] parseCSV(String line) {
        java.util.List<String> result = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());

        return result.toArray(new String[0]);
    }
}