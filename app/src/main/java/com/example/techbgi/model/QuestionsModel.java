package com.example.techbgi.model;

import java.util.List;
public class QuestionsModel {
    private String questionText;
    private List<String> options;
    private String correctAnswer;

    public QuestionsModel(){}

    public QuestionsModel(String group) {
    }

    public String getQuestionText() {
        return questionText;
    }

    public QuestionsModel(String questionText, List<String> options, String correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void addOption(String option) {
        options.add(option);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    public String toJSON() {
        StringBuilder jsonBuilder = new StringBuilder();
//    jsonBuilder.append("["); // Start with a "[" to indicate an array

        jsonBuilder.append("{\"questionText\": \"" + escapeQuotes(questionText) + "\",");
        jsonBuilder.append("\"options\": [");

        for (int i = 0; i < options.size(); i++) {
            jsonBuilder.append("\"" + escapeQuotes(options.get(i)) + "\"");
            if (i < options.size() - 1) {
                jsonBuilder.append(",");
            }
        }

        jsonBuilder.append("],");
        jsonBuilder.append("\"correctAnswer\": \"" + escapeQuotes(correctAnswer) + "\"");
        jsonBuilder.append("}");

//    jsonBuilder.append("]"); // End with a "]" to close the array

        return jsonBuilder.toString();
    }

    private String escapeQuotes(String value) {
        return value.replace("\"", "\\\"");
    }



    public void setOptions(List<String> options) {
        this.options = options;
    }
}
