package com.escapedoom.gamesession;




public class CodeSniptes {

    private static final String javaClassBeing =
            "import java.util.*;\n" +
            "public class app {\n" +
            "\n" +
            "    public static void main(String[] args) {\n" +
            "         System.out.println(solve());\n" +
            "    }\n";

    public static final String javaClassEnding =
            "}";


    public static String javaClassGenerator(String userCode) {
        return javaClassBeing + userCode + javaClassEnding;
    }
}
