package com.example.flashlearn;

public class Card {
    public String question;  // Change from private to public for Gson
    public String answer;    // Change from private to public for Gson

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public Card() {
        this.question = "";
        this.answer = "";
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}