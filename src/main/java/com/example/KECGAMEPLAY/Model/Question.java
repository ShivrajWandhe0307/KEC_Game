package com.example.KECGAMEPLAY.Model;


public class Question {
    private String questionText;
    private String correctAnswer;
    private String[] options;


    public Question(String questionText, String correctAnswer, String[] options) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getOptions() {
        return options;
    }
}
