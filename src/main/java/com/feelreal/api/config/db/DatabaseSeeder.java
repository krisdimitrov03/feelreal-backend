package com.feelreal.api.config.db;

import com.feelreal.api.dto.article.ArticleCreateRequest;
import com.feelreal.api.model.Article;
import com.feelreal.api.model.Job;
import com.feelreal.api.model.User;
import com.feelreal.api.model.enumeration.ArticleType;
import com.feelreal.api.model.enumeration.Intensity;
import com.feelreal.api.model.enumeration.Role;
import com.feelreal.api.repository.ArticleRepository;
import com.feelreal.api.repository.JobRepository;
import com.feelreal.api.repository.UserRepository;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseSeeder(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JobRepository jobRepository,
            ArticleRepository articleRepository
    ) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.passwordEncoder = passwordEncoder;
        this.articleRepository = articleRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        List<Job> jobs = seedJobs();
        seedUsers(jobs);
        seedArticles();
    }

    private List<Job> seedJobs() {
        if (jobRepository.count() > 0) {
            return jobRepository.findAll();
        }

        Job softwareEngineer = new Job("Software Engineer", Intensity.HIGH);
        Job dataAnalyst = new Job("Data Analyst", Intensity.MEDIUM);
        Job productManager = new Job("Product Manager", Intensity.LOW);

        List<Job> jobs = List.of(softwareEngineer, dataAnalyst, productManager);

        jobRepository.saveAll(jobs);
        jobRepository.flush();

        return jobs;
    }

    private void seedUsers(List<Job> jobs) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = new User("admin_user", "admin@gmail.com", "John", "Doe",
                passwordEncoder.encode("Doe@1234"), LocalDate.of(1990, 1, 23), Role.ADMIN, jobs.get(0), LocalDate.now());

        User user = new User("normal_user", "user@gmail.com", "Matt", "Henson",
                passwordEncoder.encode("Henson@1234"), LocalDate.of(2002, 5, 12), Role.USER, jobs.get(1), LocalDate.now());

        userRepository.save(admin);
        userRepository.save(user);
        userRepository.flush();
    }

    private void seedArticles() {
        if (articleRepository.count() > 0) {
            return;
        }

        try {
            String json = new String(Files.readAllBytes(Paths.get("src/main/resources/articles.json")));

            Gson gson = new Gson();

            List<Article> articles = Arrays.stream(gson.fromJson(json, ArticleCreateRequest[].class))
                    .map(a -> new Article(
                            ArticleType.values()[a.getType()],
                            a.getTitle(),
                            a.getContent()
                    ))
                    .toList();

            articleRepository.saveAll(articles);
            articleRepository.flush();
        } catch (IOException e) {
            System.out.println("Error reading articles.json file");
        }
    }
}
