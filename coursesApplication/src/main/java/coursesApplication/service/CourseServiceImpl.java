package coursesApplication.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import coursesApplication.dto.CourseDTO;
import coursesApplication.exception.CourseValidationException;
import coursesApplication.exception.ExceptionMsg;
import coursesApplication.model.Course;
import coursesApplication.repository.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	CourseRepository courseRepository;

	private ModelMapper mapper = new ModelMapper();

	@Override
	public CourseDTO getCourseById(int id) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent()) {
			return mapper.map(course.get(), CourseDTO.class);
		} else {
			return null;
		}
	}

	/**
	 * Find courses by description. Throws an exception if the description
	 * parameter is null or empty.
	 */
	@Override
	public List<CourseDTO> findCourses(String description) throws CourseValidationException {
		if (StringUtils.isBlank(description)) {
			throw new CourseValidationException(ExceptionMsg.NULL_DESCRIPTION);
		}

		List<Course> courses = courseRepository.findCourseByDescription(description);
		return courses.stream().map(c -> mapper.map(c, CourseDTO.class)).collect(Collectors.toList());
	}

	/**
	 * Save the course record. Each field in the record is required, and an
	 * exception will be thrown if it is not provided.
	 */
	@Override
	public void saveCourse(CourseDTO course) throws CourseValidationException {

		if (StringUtils.isBlank(course.getSubject())) {
			throw new CourseValidationException(ExceptionMsg.NULL_SUBJECT);
		}
		if (StringUtils.isBlank(course.getDescription())) {
			throw new CourseValidationException(ExceptionMsg.NULL_DESCRIPTION);
		}
		if (StringUtils.isBlank(course.getCourseNumber()) || course.getCourseNumber().length() > 3) {
			throw new CourseValidationException(ExceptionMsg.NULL_COURSENUM);
		} else {

			// Validate that the courseNumber is all digits
			Pattern pattern = Pattern.compile("^\\d+$");
			Matcher matcher = pattern.matcher(course.getCourseNumber());
			if (!matcher.matches()) {
				throw new CourseValidationException(ExceptionMsg.INVALID_COURSENUM);
			}

			// Zero pad the courseNumber
			course.setCourseNumber(zeroPadCourseNum(course.getCourseNumber()));
		}

		// Now validate that there are no existing records with the same subject
		// and number, as the combination of the two fields is unique
		List<Course> existingCourses = courseRepository.findCourseBySubjectAndNumber(course.getSubject(),
				course.getCourseNumber());
		if (!CollectionUtils.isEmpty(existingCourses)) {
			throw new CourseValidationException(ExceptionMsg.NON_UNIQUE_SUBJ_AND_COURSENUM);
		}

		courseRepository.save(mapper.map(course, Course.class));
	}

	/**
	 * Delete the course record from the database. An exception will be thrown
	 * if the record does not exist.
	 */
	@Override
	public void deleteCourse(CourseDTO courseDTO) throws CourseValidationException {
		Course course = mapper.map(courseDTO, Course.class);

		// Validate that the course exists before trying to delete
		Optional<Course> existingRecord = courseRepository.findById(course.getId());
		if (!existingRecord.isPresent()) {
			throw new CourseValidationException(ExceptionMsg.COURSE_NOT_FOUND);
		}

		courseRepository.delete(mapper.map(course, Course.class));
	}

	/**
	 * Zero pad the course number value. If the course number is less than 3
	 * characters, it must be zero padded on the left. Because of validation in
	 * the calling method, it is known that the courseNum variable is not null,
	 * and has a length between 1 and 3.
	 * 
	 * @param courseNum
	 * @return
	 */
	private String zeroPadCourseNum(String courseNum) {
		String courseNumZeroPad = courseNum;

		// Zero pad the courseNumber
		if (courseNum.length() < 3) {
			courseNumZeroPad = "00" + courseNum;
			courseNumZeroPad = courseNumZeroPad.substring(courseNumZeroPad.length() - 3);
		}
		return courseNumZeroPad;
	}

}
