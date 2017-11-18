package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.stream.Stream;

@EnableDiscoveryClient
@SpringBootApplication
public class TestResultApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestResultApplication.class, args);
	}

}

@RepositoryRestResource
interface ResultRepository extends JpaRepository <Student, Long>{
	@RestResource(path = "by-name")
	Collection<Student> findByStudentName(@Param("sn") String name);
}

@Entity
class Student {

	@Id
	@GeneratedValue
	private Long Id;
	private String studentName;
	private String Grade;

	public String getGrade() {
		return Grade;
	}

	public Long getId() {
		return Id;
	}

	public String getStudentName() {
		return studentName;
	}

	public Student(String studentName, String Grade) {
		this.studentName = studentName;
		this.Grade = Grade;
	}

	public Student() {
		//JPA

	}

	@Override
	public String toString() {
		return "Student{" +
				"Id=" + Id +
				", studentName='" + studentName + '\'' +
				", Grade='" + Grade + '\'' +
				'}';
	}
}

@Component
class SampleCLR implements CommandLineRunner{

	@Autowired
	private ResultRepository resultRepository ;

	@Override
	public void run(String... strings) throws Exception {

		Stream.of(new Student("A","A"),new Student("B","B"))
				.forEach( st -> resultRepository.save(st));
	}
}

@RefreshScope
@RestController
class HelloMessageController{

	@Value("${message}")
	private String msg;

	@RequestMapping("/message")
	String message() {
		return this.msg;
	}
}



