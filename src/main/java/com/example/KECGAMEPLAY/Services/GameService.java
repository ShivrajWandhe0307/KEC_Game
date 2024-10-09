package com.example.KECGAMEPLAY.Services;


import com.example.KECGAMEPLAY.Model.Player;
import com.example.KECGAMEPLAY.Model.Question;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
;

@Service
public class GameService {
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = -1;
    private Player currentPlayer;


    public GameService() {

        questions.add(new Question("What is the capital of France?", "Paris", new String[]{"Paris", "London", "Berlin", "Madrid"}));
        questions.add(new Question("What is 2 + 2?", "4", new String[]{"3", "4", "5", "6"}));
        questions.add(new Question("What is the largest ocean on Earth?", "Pacific", new String[]{"Atlantic", "Indian", "Pacific", "Arctic"}));
        questions.add(new Question("What is the chemical symbol for gold?", "Au", new String[]{"Ag", "Au", "Pb", "Fe"}));
        questions.add(new Question("Who wrote 'Romeo and Juliet'?", "Shakespeare", new String[]{"Shakespeare", "Dickens", "Hemingway", "Tolkien"}));
    }

    public void setCurrentPlayer(String playerName) {
        this.currentPlayer = new Player(playerName);
        this.currentQuestionIndex = 0;
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < questions.size();
    }

    public Question getCurrentQuestion() {
        if (hasMoreQuestions()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean checkAnswer(String answer) {
        if (hasMoreQuestions()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            boolean isCorrect = currentQuestion.getCorrectAnswer().equalsIgnoreCase(answer);
            if (isCorrect) {
                currentQuestionIndex++;
                return true;
            }
            return false;
        }
        return false;
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
