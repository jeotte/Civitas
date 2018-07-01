package coursesApplication.service;

import java.util.List;

import coursesApplication.dto.CourseDTO;
import coursesApplication.exception.CourseValidationException;

public interface CourseService {

	public CourseDTO getCourseById(int id);

	public List<CourseDTO> findCourses(String description) throws CourseValidationException;

	public void saveCourse(CourseDTO course) throws CourseValidationException;

	public void deleteCourse(CourseDTO course) throws CourseValidationException;
}
