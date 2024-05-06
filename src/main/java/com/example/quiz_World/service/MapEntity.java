package com.example.quiz_World.service;

import com.example.quiz_World.entities.*;
import com.example.quiz_World.entities.quizEntity.*;
import com.example.quiz_World.entities.wordSetEntity.Word;
import com.example.quiz_World.entities.wordSetEntity.WordDTO;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.entities.wordSetEntity.WordSetDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapEntity {
    QuizDTO mapQuizToQuizDTO(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();

        quizDTO.setTitle(quiz.getTitle());
        quizDTO.setCategory(quiz.getQuizCategory().getName());

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
        List<AnswerToQuizDTO> answerToQuizDTOList = mapAnswersToAnswerDTO(question.getAnswerToQuiz());
        questionDTO.setAnswers(answerToQuizDTOList);

        return questionDTO;
    }

    List<AnswerToQuizDTO> mapAnswersToAnswerDTO(List<AnswerToQuiz> answerToQuizList) {
        List<AnswerToQuizDTO> answerToQuizDTOList = new ArrayList<>();

        for (AnswerToQuiz answerToQuiz : answerToQuizList) {
            AnswerToQuizDTO answerToQuizDTO = new AnswerToQuizDTO();
            answerToQuizDTO.setAnswerNumber(answerToQuiz.getAnswerNumber());
            answerToQuizDTO.setAnswerContent(answerToQuiz.getContent());
            answerToQuizDTOList.add(answerToQuizDTO);
        }
        return answerToQuizDTOList;
    }

    ResultDTO mapQuizResultToQuizResultDTO(Result result) {
        ResultDTO resultDTO = new ResultDTO();

        resultDTO.setUser(result.getUser().getName());
        resultDTO.setQuiz(result.getQuiz().getTitle());
        resultDTO.setScore(result.getScore());

        return resultDTO;
    }

    List<ResultDTO> mapQuizResultsToQuizResultsDTO(List<Result> results) {
        List<ResultDTO> resultDTOS = new ArrayList<>();

        for (Result result : results) {
            ResultDTO resultDTO = mapQuizResultToQuizResultDTO(result);
            resultDTOS.add(resultDTO);
        }
        return resultDTOS;
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
        wordSetDTO.setCategory(wordSet.getWordSetCategory().getName());

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
