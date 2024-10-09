package com.example.KECGAMEPLAY.Controller;

import com.example.KECGAMEPLAY.Model.Question;
import com.example.KECGAMEPLAY.Services.GameService;
import com.example.KECGAMEPLAY.WebSockets.GameWebSocketHandler;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameWebSocketHandler webSocketHandler;

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @PostMapping("/start-game")
    public String startGame(@RequestParam("playerName") String playerName) {
        gameService.setCurrentPlayer(playerName);
        return "redirect:/game";
    }

    @GetMapping("/game")
    public String showGame(Model model) {
        if (gameService.hasMoreQuestions()) {
            Question question = gameService.getCurrentQuestion();
            model.addAttribute("question", question);
            model.addAttribute("playerName", gameService.getCurrentPlayer().getName());
            return "game";
        } else {
            model.addAttribute("winnerName", gameService.getCurrentPlayer().getName());
            return "result";
        }
    }

    @PostMapping("/submit-answer")
    public String submitAnswer(@RequestParam("answer") String answer, Model model) throws IOException {
        boolean isCorrect = gameService.checkAnswer(answer); // Check if the answer is correct
        model.addAttribute("playerName", gameService.getCurrentPlayer().getName());


        Question currentQuestion = gameService.getCurrentQuestion();
        model.addAttribute("question", currentQuestion);

        if (isCorrect) {
            webSocketHandler.broadcast("Congratulations " + gameService.getCurrentPlayer().getName() + "!");
            model.addAttribute("isCorrect", true); // Indicate that the answer is correct


            if (gameService.hasMoreQuestions()) {

                return "game";
            } else {

                model.addAttribute("winnerName", gameService.getCurrentPlayer().getName());
                return "thank-you";
            }
        } else {
            model.addAttribute("errorMessage", "Incorrect answer, please try again!"); // Add error message
            model.addAttribute("isCorrect", false); // Indicate that the answer is incorrect
        }

        return "game";
    }

    @GetMapping("/player")
    public String showPlayerPage(Model model) {
        if (gameService.hasMoreQuestions()) {
            Question question = gameService.getCurrentQuestion();
            model.addAttribute("question", question);
            return "player";
        }
        return "no-questions";
    }
    @GetMapping("/generate-qr")
    public void generateQRCode(HttpServletResponse response) throws WriterException, IOException {
        String qrCodeUrl = "http://localhost:8080/player";
        int size = 200;
        BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter().encode(qrCodeUrl, BarcodeFormat.QR_CODE, size, size);

        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        outputStream.flush();
    }








}

