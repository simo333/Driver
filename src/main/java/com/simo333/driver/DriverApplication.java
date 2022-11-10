package com.simo333.driver;

import com.simo333.driver.model.Question;
import com.simo333.driver.model.Role;
import com.simo333.driver.payload.answer.AnswerCreateRequest;
import com.simo333.driver.payload.question.QuestionCreateRequest;
import com.simo333.driver.service.AnswerService;
import com.simo333.driver.service.QuestionService;
import com.simo333.driver.service.RoleService;
import com.simo333.driver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@Slf4j
public class DriverApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverApplication.class, args);

    }

    @Bean
    CommandLineRunner run(RoleService roleService, UserService userService, QuestionService questionService,
                          AnswerService answerService) {
        return args -> {
            Role roleUser = new Role();
            roleUser.setName(Role.Type.ROLE_USER);
            Role roleAdmin = new Role();
            roleAdmin.setName(Role.Type.ROLE_ADMIN);

            roleService.save(roleUser);
            roleService.save(roleAdmin);

            List<AnswerCreateRequest> answers = createAnswers();

            for (int i = 1; i <= 30; i++) {
                QuestionCreateRequest questionRequest = new QuestionCreateRequest();
                questionRequest.setQuestionText("Question" + i);
                questionRequest.setAnswers(answers);
                Question saved = questionService.save(questionRequest);
                answerService.saveForQuestion(saved, answers);
            }

//            userService.save(User.builder()
//                    .username("szymon")
//                    .email("szymon.333@wp.pl")
//                    .password("12345678")
//                    .enabled(true)
//                    .roles(Set.of(roleService.findOne(Role.Type.ROLE_USER)))
//                    .build());
        };
    }

    List<AnswerCreateRequest> createAnswers() {
        AnswerCreateRequest answer1 = new AnswerCreateRequest();
        answer1.setText("Answer1");
        answer1.setIsCorrect(true);
        AnswerCreateRequest answer2 = new AnswerCreateRequest();
        answer2.setText("Answer2");
        answer2.setIsCorrect(false);
        AnswerCreateRequest answer3 = new AnswerCreateRequest();
        answer3.setText("Answer3");
        answer3.setIsCorrect(false);
        AnswerCreateRequest answer4 = new AnswerCreateRequest();
        answer4.setText("Answer4");
        answer4.setIsCorrect(false);
        return List.of(answer1, answer2, answer3, answer4);
    }

}
