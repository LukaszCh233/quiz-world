package com.example.quiz_World.service;

import com.example.quiz_World.entities.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapEntity {
    QuizDTO mapQuizToQuizDTO(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();

        quizDTO.setTitle(quiz.getTitle());
        quizDTO.setCategory(quiz.getCategoryQuiz().getName());

        return quizDTO;
    }

    List<QuizDTO> mapQuizzesToQuizzesDTO(List<Quiz> quizList) {
        List<QuizDTO> quizDTOS = new ArrayList<>();

        for (Quiz quiz : quizList) {
            QuizDTO quizDTO = mapQuizToQuizDTO(quiz);
            quizDTOS.add(quizDTO);

        }
        return quizDTOS;
    }

    List<QuestionDTO> mapQuestionsToQuestionsDTO(List<Question> questionsList) {
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionsList) {
            QuestionDTO questionDTO = mapQuestionToQuestionDTO(question);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }

    QuestionDTO mapQuestionToQuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();

        questionDTO.setQuestionContent(question.getContent());
        questionDTO.setQuestionNumber(question.getQuestionNumber());
        List<AnswerDTO> answerDTOList = mapAnswersToAnswerDTO(question.getAnswerToQuizQuestions());
        questionDTO.setAnswers(answerDTOList);

        return questionDTO;
    }

    List<AnswerDTO> mapAnswersToAnswerDTO(List<AnswerToQuizQuestion> answerToQuizQuestionList) {
        List<AnswerDTO> answerDTOList = new ArrayList<>();

        for (AnswerToQuizQuestion answerToQuizQuestion : answerToQuizQuestionList) {
            AnswerDTO answerDTO = new AnswerDTO();
            answerDTO.setAnswerNumber(answerToQuizQuestion.getAnswerNumber());
            answerDTO.setAnswerContent(answerToQuizQuestion.getContent());
            answerDTOList.add(answerDTO);
        }
        return answerDTOList;
    }

    QuizResultDTO mapQuizResultToQuizResultDTO(Result result) {
        QuizResultDTO quizResultDTO = new QuizResultDTO();

        quizResultDTO.setUser(result.getUser().getName());
        quizResultDTO.setQuiz(result.getQuiz().getTitle());
        quizResultDTO.setScore(result.getScore());

        return quizResultDTO;
    }

    List<QuizResultDTO> mapQuizResultsToQuizResultsDTO(List<Result> results) {
        List<QuizResultDTO> quizResultDTOS = new ArrayList<>();

        for (Result result : results) {
            QuizResultDTO quizResultDTO = mapQuizResultToQuizResultDTO(result);
            quizResultDTOS.add(quizResultDTO);
        }
        return quizResultDTOS;
    }

    UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }

    AdminDTO mapAdminToAdminDTO(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();

        adminDTO.setId(admin.getId());
        adminDTO.setName(admin.getName());
        adminDTO.setEmail(admin.getEmail());
        return adminDTO;
    }

    WordSetDTO mapWordSetToWordSetDTO(WordSet wordSet) {
        WordSetDTO wordSetDTO = new WordSetDTO();
        wordSetDTO.setId(wordSet.getId());
        wordSetDTO.setTitle(wordSet.getTitle());
        wordSetDTO.setCategory(wordSet.getCategoryWordSet().getName());

        return wordSetDTO;
    }

    List<WordSetDTO> mapWordSetListToWordSetListDTO(List<WordSet> wordSetList) {
        List<WordSetDTO> wordSetDTOList = new ArrayList<>();
        for (WordSet wordSet : wordSetList) {
            WordSetDTO wordSetDTO = mapWordSetToWordSetDTO(wordSet);
            wordSetDTOList.add(wordSetDTO);
        }
        return wordSetDTOList;
    }

    WordDTO mapWordToWordDTO(Word word) {
        WordDTO wordDTO = new WordDTO();
        wordDTO.setWordNumber(word.getWordNumber());
        wordDTO.setWord(word.getWord());


        return wordDTO;
    }

    List<WordDTO> mapWordListToWordListDTO(List<Word> wordList) {
        List<WordDTO> wordDTOList = new ArrayList<>();
        for (Word word : wordList) {
            WordDTO wordDTO = mapWordToWordDTO(word);
            wordDTOList.add(wordDTO);
        }
        return wordDTOList;
    }
}
