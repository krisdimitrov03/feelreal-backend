package com.feelreal.api.config;

import com.feelreal.api.model.Job;
import com.feelreal.api.model.User;
import com.feelreal.api.model.enumeration.Intensity;
import com.feelreal.api.model.enumeration.Role;
import com.feelreal.api.repository.JobRepository;
import com.feelreal.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        List<Job> jobs = seedJobs();
        seedUsers(jobs);
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
}
