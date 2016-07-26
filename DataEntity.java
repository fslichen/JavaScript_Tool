package architect.example4.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "any_entity")
public class DataEntity {
	// It is always a good idea to add @Id above the id field.
	@Id
	String id;
	String name;
	String gender;
	double grade;
	int age;
}
