package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/cactus")
@CrossOrigin(origins = "*")
public class CactusController {

    @Autowired
    private CactusRepository repository;

    private final List<String> phrases = Arrays.asList(
            "Ні",
            "Потужно!",
            "Гарний варіант",
            "Спробуй ще раз",
            "Авжеж",
            "Навряд чи",
            "Це база",
            "Не сьогодні",
            "Йди вчи лабу"
    );

    @GetMapping
    public List<CactusRecord> getHistory() {
        return repository.findAll();
    }

    @PostMapping
    public CactusRecord saveMood(@RequestBody String mood) {
        CactusRecord record = new CactusRecord();
        record.setMood(mood);
        return repository.save(record);
    }

    @PostMapping("/ask")
    public CactusRecord askQuestion(@RequestBody String question) {
        String randomPhrase = phrases.get(new Random().nextInt(phrases.size()));

        CactusRecord record = new CactusRecord();
        record.setQuestion(question);
        record.setAnswer(randomPhrase);

        return repository.save(record);
    }

    @DeleteMapping
    public void deleteHistory() {
        repository.deleteAll();
    }
}