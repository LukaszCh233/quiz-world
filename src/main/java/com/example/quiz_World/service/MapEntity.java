package com.example.quiz_World.service;

import com.example.quiz_World.entities.*;
import com.example.quiz_World.entities.quizEntity.*;
import com.example.quiz_World.entities.wordSetEntity.*;
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

    public List<QuizDTO> mapQuizzesToQuizzesDTO(List<Quiz> quizList) {
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

    public List<QuizCategoryDTO> mapQuizCategoriesToQuizCategoriesDTO(List<QuizCategory> quizCategoryList) {
        List<QuizCategoryDTO> quizCategoryDTOS = new ArrayList<>();

        for (QuizCategory quizCategory : quizCategoryList) {
            QuizCategoryDTO quizCategoryDTO = mapQuizCategoryToQuizCategoryDTO(quizCategory);
            quizCategoryDTOS.add(quizCategoryDTO);

        }
        return quizCategoryDTOS;
    }

    WordSetCategoryDTO mapWordSetCategoryToWordSSetCategoryDTO(WordSetCategory wordSetCategory) {
        WordSetCategoryDTO wordSetCategoryDTO = new WordSetCategoryDTO();
        wordSetCategoryDTO.setName(wordSetCategory.getName());
        return wordSetCategoryDTO;
    }

    public List<WordSetCategoryDTO> mapWordSetCategoriesToWordSetCategoriesDTO(List<WordSetCategory> wordSetCategoryList) {
        List<WordSetCategoryDTO> wordSetCategoryDTOS = new ArrayList<>();

        for (WordSetCategory wordSetCategory : wordSetCategoryList) {
            WordSetCategoryDTO wordSetCategoryDTO = mapWordSetCategoryToWordSSetCategoryDTO(wordSetCategory);
            wordSetCategoryDTOS.add(wordSetCategoryDTO);

        }
        return wordSetCategoryDTOS;
    }

    QuizCategoryDTO mapQuizCategoryToQuizCategoryDTO(QuizCategory quizCategory) {
        QuizCategoryDTO quizCategoryDTO = new QuizCategoryDTO();
        quizCategoryDTO.setName(quizCategory.getName());
        return quizCategoryDTO;
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

    QuizResultDTO mapQuizResultToQuizResultDTO(Result result) {
        QuizResultDTO quizResultDTO = new QuizResultDTO();

        quizResultDTO.setUser(result.getUser().getName());
        quizResultDTO.setQuiz(result.getQuiz().getTitle());
        quizResultDTO.setScore(result.getScore());

        return quizResultDTO;
    }

    public List<QuizResultDTO> mapQuizResultsToQuizResultsDTO(List<Result> results) {
        List<QuizResultDTO> quizResultDTOS = new ArrayList<>();

        for (Result result : results) {
            QuizResultDTO quizResultDTO = mapQuizResultToQuizResultDTO(result);
            quizResultDTOS.add(quizResultDTO);
        }
        return quizResultDTOS;
    }

    WordSetResultDTO mapWordSetResultToWordSetResultDTO(Result result) {
        WordSetResultDTO wordSetResultDTO = new WordSetResultDTO();

        wordSetResultDTO.setUser(result.getUser().getName());
        wordSetResultDTO.setWordSet(result.getWordSet().getTitle());
        wordSetResultDTO.setScore(result.getScore());

        return wordSetResultDTO;
    }

    List<WordSetResultDTO> mapWordSetResultsToWordSetResultsDTO(List<Result> results) {
        List<WordSetResultDTO> wordSetResultDTOS = new ArrayList<>();

        for (Result result : results) {
            WordSetResultDTO wordSetResultDTO = mapWordSetResultToWordSetResultDTO(result);
            wordSetResultDTOS.add(wordSetResultDTO);
        }
        return wordSetResultDTOS;
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

    List<UserDTO> mapUserListToUserDTOList(List<User> userList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            UserDTO userDTO = mapUserToUserDTO(user);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }
}
