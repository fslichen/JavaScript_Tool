package architect.example4.entity;

import lombok.Data;

@Data
public class MultipleFieldsResultEntity {
	// You don't need to put @Id above gender or age.
	String gender;
	String age;
	Object averageGrade;
}