package architect.example4.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "any_entity")
public class ProjectDataEntity {
	String name;
	double grade;
}