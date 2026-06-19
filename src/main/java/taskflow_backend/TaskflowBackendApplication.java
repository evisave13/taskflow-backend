package taskflow_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
    basePackages = "com.taskflow_backend.repository")
@EntityScan(
    basePackages = "com.taskflow_backend.entity")
@ComponentScan(basePackages = {
    "taskflow_backend",
    "com.taskflow_backend"
})
public class TaskflowBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            TaskflowBackendApplication.class, args);
    }
}