package gogood.gogoodapi.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static String manterApenasNumericos(String texto){

        String[] split = texto.split("[a-z]");
        StringBuilder apenasNumericos = new StringBuilder();

        Arrays.stream(split).toList().forEach(
                s -> apenasNumericos.append(s.replace(" ", ""))
        );

        return apenasNumericos.toString();

    }
}
