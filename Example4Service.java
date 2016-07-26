package architect.example4.service;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;

import architect.example4.entity.ComprehensiveResultEntity;
import architect.example4.entity.DataEntity;
import architect.example4.entity.MultipleFieldsResultEntity;
import architect.example4.entity.ProjectDataEntity;
import architect.example4.entity.ResultEntity;

@Service
public class Example4Service {
	@Autowired 
	MongoTemplate mongoTemplate;

	/**
	 * select gender, avg(grade) from any_table group by gender
	 */
	public void groupBy() {
		// The "gender" and "grade" correspond to the gender and grade fields in DataEntity
		// The "averageGrade" corresponds to the averageGrade field in ResultEntity
		TypedAggregation<DataEntity> aggregation = newAggregation(DataEntity.class, 
				group("gender").avg("grade").as("averageGrade"));
		List<ResultEntity> resultEntities = mongoTemplate.aggregate(aggregation, ResultEntity.class).getMappedResults();
		System.out.println(resultEntities);
	}
	
	/**
	 * select gender, avg(grade) from any_table group by gender order by avg(grade)
	 */
	public void groupBySort() {
		TypedAggregation<DataEntity> aggregation = newAggregation(DataEntity.class, 
				group("gender").avg("grade").as("averageGrade"),
				sort(Direction.DESC, "averageGrade"));
		List<ResultEntity> resultEntities = mongoTemplate.aggregate(aggregation, ResultEntity.class).getMappedResults();
		System.out.println(resultEntities);
	}
	
	/**
	 * select gender, avg(grade) from any_table group by gender having avg(grade) > 3.6
	 */
	public void groupByMatch() {
		TypedAggregation<DataEntity> aggregation = newAggregation(DataEntity.class, 
				group("gender").avg("grade").as("averageGrade"),
				match(Criteria.where("averageGrade").gt(3.6)));
		List<ResultEntity> resultEntities = mongoTemplate.aggregate(aggregation, ResultEntity.class).getMappedResults();
		System.out.println(resultEntities);
	}
	
	/**
	 * select gender, avg(grade) from any_table where gender <> '' group by gender
	 */
	public void groupByUnwind() {
		TypedAggregation<DataEntity> aggregation = newAggregation(DataEntity.class, 
				// Unwind filters out the fields where gender equals null.
				unwind("gender"),
				group("gender").avg("grade").as("averageGrade"));
		List<ResultEntity> resultEntities = mongoTemplate.aggregate(aggregation, ResultEntity.class).getMappedResults();
		System.out.println(resultEntities);
	}
	
	/**
	 * select gender, age, avg(grade) from any_table group by gender, age
	 */
	public void groupByMultipleFields() {
		TypedAggregation<DataEntity> aggregation = newAggregation(DataEntity.class, 
				group("gender", "age").avg("grade").as("averageGrade"));
		List<MultipleFieldsResultEntity> resultEntities = mongoTemplate.aggregate(aggregation, MultipleFieldsResultEntity.class).getMappedResults();
		System.out.println(resultEntities);
	}
	
	/**
	 * select gender, age, avg(grade) from any_table where gender <> '' and age <> 0 group by gender, age
	 */
	public void groupByMultipleFieldsAndUnwind() {
		TypedAggregation<DataEntity> aggregation = newAggregation(DataEntity.class, 
				unwind("gender"), unwind("grade"),
				group("gender", "age").avg("grade").as("averageGrade"));
		List<MultipleFieldsResultEntity> resultEntities = mongoTemplate.aggregate(aggregation, MultipleFieldsResultEntity.class).getMappedResults();
		System.out.println(resultEntities);
	}
	
	/**
	 * select max(grade), min(grade), avg(grade) from any_table
	 */
	public void comprehensive() {
		TypedAggregation<DataEntity> aggregation = newAggregation(DataEntity.class, 
				unwind("grade"),
				sort(Direction.DESC, "grade"),
				group().last("grade").as("minimumGrade")
				.first("grade").as("maximumGrade")
				.avg("grade").as("averageGrade"));
		List<ComprehensiveResultEntity> resultEntities = mongoTemplate.aggregate(aggregation, ComprehensiveResultEntity.class).getMappedResults();
		System.out.println(resultEntities);
	}
	
	/**
	 * select * from any_table order by grade desc, age desc
	 */
	public void multipleSort() {
		TypedAggregation<DataEntity> aggregation = newAggregation(DataEntity.class, 
				sort(Direction.DESC, "grade", "age"));
		List<DataEntity> resultEntities = mongoTemplate.aggregate(aggregation, DataEntity.class).getMappedResults();
		System.out.println(resultEntities);
	}
	
	/**
	 * select name, grade, grade + 1.05 as gradePlus, grade - 1.05 as gradeMinus, grade * 1.05 as gradeMultiply, grade / 1.05 as gradeDivide from any_table
	 */
	public void projection() {
		TypedAggregation<ProjectDataEntity> aggregation = newAggregation(ProjectDataEntity.class, 
				// You don't need to define a field called curvedGrade inside ProjectDataEntity.
				project("name", "grade")
				.and("grade").plus(1.05).as("gradePlus")
				.and("grade").minus(1.05).as("gradeMinus")
				.and("grade").multiply(1.05).as("gradeMultiply")
				.and("grade").divide(1.05).as("gradeDivide"));
		List<DBObject> DBObjects = mongoTemplate.aggregate(aggregation, DBObject.class).getMappedResults();
		System.out.println(DBObjects);
	}

}
