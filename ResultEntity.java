package architect.example4.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class ResultEntity {
	// Do not forget to add @Id, otherwise the result will be like ResultEntity(gender=null, averageGrade=3.586).
	// You can't use @Field to replace @Id
	@Id
	String gender;
	Object averageGrade;
}