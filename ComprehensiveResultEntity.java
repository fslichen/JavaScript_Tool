package architect.example4.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
public class ComprehensiveResultEntity {
	Object maximumGrade;
	Object minimumGrade;
	Object averageGrade;
}
