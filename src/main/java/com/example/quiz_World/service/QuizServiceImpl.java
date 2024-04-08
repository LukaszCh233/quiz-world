package com.example.quiz_World.service;

import com.example.quiz_World.entities.*;
import com.example.quiz_World.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceImpl {
    private final QuizRepository quizRepository;
    private final AnswersRepository answersRepository;
    private final QuestionsRepository questionsRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;

    public QuizServiceImpl(QuizRepository quizRepository, AnswersRepository answersRepository,
                           QuestionsRepository questionsRepository, CategoryRepository categoryRepository,
                           UserRepository userRepository, QuizResultRepository quizResultRepository) {
        this.quizRepository = quizRepository;
        this.answersRepository = answersRepository;
        this.questionsRepository = questionsRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.quizResultRepository = quizResultRepository;
    }

    public Quiz createQuiz(String title, Long categoryId, Status status, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Customer not found"));


        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Quiz quiz = new Quiz();
        quiz.setUserId(user.getId());
        quiz.setTitle(title);
        quiz.setCategory(category);
        quiz.setStatus(status);


        return quizRepository.save(quiz);
    }

    @Transactional
    public void addQuestionsToQuiz(Long quizId, Question question) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        question.setQuiz(quiz);

        question.getAnswers().forEach(answer -> answer.setQuestion(question));

        quiz.getQuestions().add(question);

        quizRepository.save(quiz);

    }


    public List<QuizDTO> findAllPublicQuizzes() {
        List<Quiz> quizList = quizRepository.findByStatus(Status.PUBLIC);
        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        return mapQuizzesToQuizzesDTO(quizList);
    }

    public List<QuizDTO> findYourQuizzes(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        List<Quiz> quizList = quizRepository.findQuizzesByUserId(user.getId());
        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("Not found quizzes");
        }
        return mapQuizzesToQuizzesDTO(quizList);

    }

    public QuizDTO findQuizById(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        return mapQuizToQuizDTO(quiz);
    }

    public List<QuestionDTO> findQuestionsByQuizId(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        List<Question> questionsList = quiz.getQuestions();
        if (questionsList.isEmpty()) {
            throw new EntityNotFoundException("Quiz is empty");
        }

        return mapQuestionsToQuestionDTO(questionsList);
    }

    public void deleteAllQuiz(Principal principal) {
        List<Quiz> quizList = findQuizzesByUserPrincipal(principal);

        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        quizRepository.deleteAll(quizList);
    }

    public void deleteQuizById(Long id, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quizToDelete = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz nor found"));
        if (!quizToDelete.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this quiz");
        }
        quizRepository.delete(quizToDelete);
    }

    public void updateQuizById(Long id, Quiz quiz, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quizToUpdate = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        if (!quizToUpdate.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this quiz");
        }
        if (quiz.getCategory() == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        Category category = categoryRepository.findById(quiz.getCategory().getId()).orElseThrow(() -> new EntityNotFoundException("category not exists"));

        quizToUpdate.setTitle(quiz.getTitle());
        quizToUpdate.setCategory(category);

        quizRepository.save(quizToUpdate);
    }

    public void updateQuestion(Long quizId, Long questionId, Question question, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        if (!quiz.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this quiz");
        }
        Question questionToUpdate = questionsRepository.findById(questionId).orElseThrow(() -> new EntityNotFoundException("Not found"));

        questionToUpdate.setContent(question.getContent());

        questionToUpdate.getAnswers().clear();

        for (Answer answer : question.getAnswers()) {
            answer.setQuestion(questionToUpdate);
            questionToUpdate.getAnswers().add(answer);
        }

        questionsRepository.save(questionToUpdate);
    }

    public List<Quiz> findQuizzesByUserPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        return Optional.ofNullable(quizRepository.findQuizzesByUserId(user.getId())).orElseThrow(() -> new EntityNotFoundException("Not found quizzes"));

    }

    public double solveQuiz(Long quizId, List<Answer> userAnswers, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        List<Question> questions = quiz.getQuestions();
        if (userAnswers.size() != questions.size()) {
            throw new IllegalArgumentException("Number of answers user does not match with questions");
        }

        int correctAnswersCount = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            Answer userAnswer = userAnswers.get(i);
            if (isCorrectAnswer(question, userAnswer)) {
                correctAnswersCount++;
            }
        }
        double score = (double) correctAnswersCount / questions.size() * 100;

        QuizResult quizResult = new QuizResult();
        quizResult.setUser(user);
        quizResult.setScore(score);
        quizResult.setQuiz(quiz);

        quizResultRepository.save(quizResult);
        return score;
    }

    private boolean isCorrectAnswer(Question question, Answer userAnswer) {
        Answer correctAnswer = null;
        for (Answer answer : question.getAnswers()) {
            if (answer.isCorrect()) {
                correctAnswer = answer;
                break;

            }
        }
        if (correctAnswer == null) {
            return false;
        }
        return userAnswer.getAnswerNumber().equals(correctAnswer.getAnswerNumber());

    }

    public QuizDTO mapQuizToQuizDTO(Quiz quiz) {

        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setTitle(quiz.getTitle());
        quizDTO.setCategory(quiz.getCategory().getName());

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

    public List<QuestionDTO> mapQuestionsToQuestionDTO(List<Question> questionsList) {
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question questions : questionsList) {
            QuestionDTO questionDTO = mapQuestionToQuestionDTO(questions); // Mapuj każde pytanie na obiekt DTO
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }

    public QuestionDTO mapQuestionToQuestionDTO(Question questions) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestionContent(questions.getContent());

        List<AnswerDTO> answerDTOList = mapAnswersToAnswerDTO(questions.getAnswers());
        questionDTO.setAnswers(answerDTOList);

        return questionDTO;
    }

    public List<AnswerDTO> mapAnswersToAnswerDTO(List<Answer> answerList) {
        List<AnswerDTO> answerDTOList = new ArrayList<>();
        for (Answer answer : answerList) {
            AnswerDTO answerDTO = new AnswerDTO();
            answerDTO.setAnswerNumber(answer.getAnswerNumber());
            answerDTO.setAnswerContent(answer.getContent());
            answerDTOList.add(answerDTO);
        }
        return answerDTOList;
    }
}