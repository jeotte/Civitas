package coursesApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import coursesApplication.dto.CourseDTO;
import coursesApplication.exception.CourseValidationException;
import coursesApplication.service.CourseService;

@RestController
@RequestMapping("courses")
public class CourseController {
	
	@Autowired
	private CourseService courseService;

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public CourseDTO getCourseById(@PathVariable Integer id) {
		return courseService.getCourseById(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<CourseDTO> findCourses(@RequestParam(value = "desc", required = true) String description)
			throws CourseValidationException {
		return courseService.findCourses(description);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void saveCourse(@RequestBody CourseDTO courseDto) throws CourseValidationException {
		courseService.saveCourse(courseDto);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public void deleteCourse(@RequestBody CourseDTO courseDto) throws CourseValidationException {
		courseService.deleteCourse(courseDto);
	}

}
