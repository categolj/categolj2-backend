package am.ik.categolj2.core.message;

import java.io.*;
import java.nio.file.Files;
import java.util.regex.Pattern;

public class MessageKeysGen {
    public static void main(String[] args) throws IOException {
        // message properties file
        InputStream inputStream = new FileInputStream("backend-api/src/main/resources/messages.properties");

        Class<?> targetClazz = MessageKeys.class;
        File output = new File("backend-api/src/main/java/"
                + targetClazz.getName().replaceAll(Pattern.quote("."), "/")
                + ".java");
        System.out.println("write " + output.getAbsolutePath());


        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
             PrintWriter pw = new PrintWriter(Files.newOutputStream(output.toPath()))) {
            pw.println("package " + targetClazz.getPackage().getName() + ";");
            pw.println("/**");
            pw.println(" * Message Id");
            pw.println(" */");
            pw.println("public class " + targetClazz.getSimpleName() + " {");

            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split("=", 2);
                if (vals.length > 1) {
                    String key = vals[0].trim();
                    String value = vals[1].trim();
                    pw.println("    /** " + key + "=" + value + " */");
                    pw.println("    public static final String "
                            + key.toUpperCase().replaceAll(Pattern.quote("."),
                            "_").replaceAll(Pattern.quote("-"), "_")
                            + " = \"" + key + "\";");
                }
            }
            pw.println("}");
            pw.flush();
        }
    }
}
