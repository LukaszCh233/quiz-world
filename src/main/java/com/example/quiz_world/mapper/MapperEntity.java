package com.example.quiz_world.mapper;

import com.example.quiz_world.account.user.AdminDTO;
import com.example.quiz_world.account.user.User;
import com.example.quiz_world.account.user.UserDTO;
import com.example.quiz_world.quiz.question.AnswerToQuiz;
import com.example.quiz_world.quiz.question.AnswerToQuizDTO;
import com.example.quiz_world.quiz.question.Question;
import com.example.quiz_world.quiz.question.QuestionDTO;
import com.example.quiz_world.quiz.quiz.Quiz;
import com.example.quiz_world.quiz.quiz.QuizDTO;
import com.example.quiz_world.quiz.quizCategory.QuizCategory;
import com.example.quiz_world.quiz.quizCategory.QuizCategoryDTO;
import com.example.quiz_world.quiz.reslult.QuizResultDTO;
import com.example.quiz_world.quiz.reslult.Result;
import com.example.quiz_world.words.word.Word;
import com.example.quiz_world.words.word.WordDTO;
import com.example.quiz_world.words.wordSet.WordSet;
import com.example.quiz_world.words.wordSet.WordSetDTO;
import com.example.quiz_world.words.wordSet.WordSetResultDTO;
import com.example.quiz_world.words.wordSetCategory.WordSetCategory;
import com.example.quiz_world.words.wordSetCategory.WordSetCategoryDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperEntity {


    public QuizDTO mapQuizToQuizDTO(Quiz quiz) {
        return new QuizDTO(quiz.getId(), quiz.getTitle(), quiz.getQuizCategory().getName());
    }

    public List<QuizDTO> mapQuizzesToQuizzesDTO(List<Quiz> quizList) {
        return quizList.stream()
                .map(this::mapQuizToQuizDTO)
                .collect(Collectors.toList());
    }

    public List<QuestionDTO> mapQuestionsToQuestionsDTO(List<Question> questionsList) {
        return questionsList.stream()
                .map(this::mapQuestionToQuestionDTO)
                .collect(Collectors.toList());
    }

    public List<QuizCategoryDTO> mapQuizCategoriesToQuizCategoriesDTO(List<QuizCategory> quizCategoryList) {
        return quizCategoryList.stream()
                .map(this::mapQuizCategoryToQuizCategoryDTO)
                .collect(Collectors.toList());
    }

    public WordSetCategoryDTO mapWordSetCategoryToWordSSetCategoryDTO(WordSetCategory wordSetCategory) {
        return new WordSetCategoryDTO(wordSetCategory.getName());
    }

    public List<WordSetCategoryDTO> mapWordSetCategoriesToWordSetCategoriesDTO(List<WordSetCategory> wordSetCategoryList) {
        return wordSetCategoryList.stream()
                .map(this::mapWordSetCategoryToWordSSetCategoryDTO)
                .collect(Collectors.toList());
    }

    public QuizCategoryDTO mapQuizCategoryToQuizCategoryDTO(QuizCategory quizCategory) {
        return new QuizCategoryDTO(quizCategory.getName());
    }

    public QuestionDTO mapQuestionToQuestionDTO(Question question) {
        List<AnswerToQuizDTO> answerToQuizDTOList = mapAnswersToAnswerDTO(question.getAnswerToQuiz());

        return new QuestionDTO(question.getQuestionNumber(), question.getContent(), answerToQuizDTOList);
    }

    public List<AnswerToQuizDTO> mapAnswersToAnswerDTO(List<AnswerToQuiz> answerToQuizList) {
        List<AnswerToQuizDTO> answerToQuizDTOList = new ArrayList<>();

        for (AnswerToQuiz answerToQuiz : answerToQuizList) {
            AnswerToQuizDTO answerToQuizDTO = new AnswerToQuizDTO(answerToQuiz.getAnswerNumber(), answerToQuiz.getContent());
            answerToQuizDTOList.add(answerToQuizDTO);
        }
        return answerToQuizDTOList;
    }

    public QuizResultDTO mapQuizResultToQuizResultDTO(Result result) {
        return new QuizResultDTO(result.getUser().getName(), result.getQuiz().getTitle(), result.getScore());
    }

    public List<QuizResultDTO> mapQuizResultsToQuizResultsDTO(List<Result> results) {
        return results.stream()
                .map(this::mapQuizResultToQuizResultDTO)
                .collect(Collectors.toList());
    }

    public WordSetResultDTO mapWordSetResultToWordSetResultDTO(Result result) {
        return new WordSetResultDTO(result.getUser().getName(), result.getWordSet().getTitle(), result.getScore());
    }

    public List<WordSetResultDTO> mapWordSetResultsToWordSetResultsDTO(List<Result> results) {
        return results.stream()
                .map(this::mapWordSetResultToWordSetResultDTO)
                .collect(Collectors.toList());
    }

    public UserDTO mapUserToUserDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    public AdminDTO mapAdminToAdminDTO(User admin) {
        return new AdminDTO(admin.getId(), admin.getName(), admin.getEmail());
    }

    public List<AdminDTO> mapAdminsToAdminsDTO(List<User> adminList) {
        return adminList.stream()
                .map(this::mapAdminToAdminDTO)
                .collect(Collectors.toList());
    }

    public WordSetDTO mapWordSetToWordSetDTO(WordSet wordSet) {
        return new WordSetDTO(wordSet.getId(), wordSet.getTitle(), wordSet.getWordSetCategory().getName());
    }

    public List<WordSetDTO> mapWordSetListToWordSetListDTO(List<WordSet> wordSetList) {
        return wordSetList.stream()
                .map(this::mapWordSetToWordSetDTO)
                .collect(Collectors.toList());
    }

    public WordDTO mapWordToWordDTO(Word word) {
        return new WordDTO(word.getWordNumber(), word.getWord());
    }

    public List<WordDTO> mapWordListToWordListDTO(List<Word> wordList) {
        return wordList.stream()
                .map(this::mapWordToWordDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> mapUserListToUserDTOList(List<User> userList) {
        return userList.stream()
                .map(this::mapUserToUserDTO)
                .collect(Collectors.toList());
    }
}
