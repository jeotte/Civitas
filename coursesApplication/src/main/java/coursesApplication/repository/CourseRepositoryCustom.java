package coursesApplication.repository;

import java.util.List;

import coursesApplication.model.Course;

public interface CourseRepositoryCustom {

	public List<Course> findCourseByDescription(String description);

	public List<Course> findCourseBySubjectAndNumber(String subject, String courseNumber);
}
