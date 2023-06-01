import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final String regexOutput = "(|[^|]+)\\|([^|\\r\\n]+)\\|([^|\\r\\n]+)";
    private static final String regexDropChars = "\\r\\n";

    public static String readFileToString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }

    public static void main(String[] args) {
        String filePath = "src/main/resources/data.txt";

        try {
            String fileContent = readFileToString(filePath);
            String[] lines = fileContent.split("\n");

            StringBuilder outputBuilder = new StringBuilder();
            outputBuilder.append(Arrays.stream(lines)
                    .map(line -> line.contains("V6POOL") && !line.startsWith("|") ? "|" + line : line)
                    .collect(Collectors.joining("\n")));

            String outputString = outputBuilder.toString();

            List<String[]> output = new ArrayList<>();
            // Extract fields using regular expressions
            Pattern pattern = Pattern.compile(regexOutput);
            Matcher matcher = pattern.matcher(outputString);


            while (matcher.find()) {
                String[] fields = new String[3];
                fields[0] = matcher.group(1).replaceAll(regexDropChars, "");
                fields[1] = matcher.group(2).replaceAll(regexDropChars, "");
                fields[2] = matcher.group(3).replaceAll(regexDropChars, "");
                output.add(fields);
            }

            output.forEach(fields -> {
                Arrays.stream(fields).forEach(field -> System.out.print(field + " "));
                System.out.println(); // Move to the next line after printing each set of fields
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
