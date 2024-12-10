package top.x1uc.codeforces.pojo;


import lombok.Data;

import java.util.List;

@Data
public class ProblemTemplate {
    private String name;
    private String group;
    private String url;
    private boolean interactive;
    private int memoryLimit;
    private int timeLimit;
    private List<Test> tests;
    private String testType;
    private Input input;
    private Output output;
    private Languages languages;
    private Batch batch;

    @Data
    public static class Test {
        private String input;
        private String output;
    }

    @Data
    public static class Input {
        private String type;
    }

    @Data
    public static class Output {
        private String type;
    }

    @Data
    public static class Languages {
        private Java java;
    }

    @Data
    public static class Java {
        private String mainClass;
        private String taskClass;
    }

    @Data
    public static class Batch {
        private String id;
        private int size;
    }
}
