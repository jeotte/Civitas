package coursesApplication;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import coursesApplication.dto.CourseDTO;
import coursesApplication.exception.CourseValidationException;
import coursesApplication.exception.ExceptionMsg;
import coursesApplication.model.Course;
import coursesApplication.repository.CourseRepository;
import coursesApplication.service.CourseService;
import coursesApplication.service.CourseServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceTest {

	@Mock
	CourseRepository repository;

	@InjectMocks
	CourseService service = new CourseServiceImpl();

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findCourses_nullDescription() {
		try {
			service.findCourses(null);
			fail();
		} catch (CourseValidationException exception) {
			assertEquals(exception.getMessage(), ExceptionMsg.NULL_DESCRIPTION.toString());
		}
	}

	@Test
	public void findCourses_emptyDescription() {
		try {
			service.findCourses("");
			fail();
		} catch (CourseValidationException exception) {
			assertEquals(exception.getMessage(), ExceptionMsg.NULL_DESCRIPTION.toString());
		}
	}

	@Test
	public void findCourses() throws Exception {
		List<Course> returnList = new ArrayList<>();
		Course course1 = new Course();
		course1.setCourseNumber("123");
		course1.setDescription("This is a test description");
		course1.setId(1);
		course1.setSubject("ENG");
		returnList.add(course1);

		// Mockup what the repo will return; testing the repo/database is not
		// part of this test
		when(repository.findCourseByDescription(Mockito.anyString())).thenReturn(returnList);

		List<CourseDTO> courseList = service.findCourses("test");
		assertEquals(courseList.get(0).getId(), course1.getId());
		assertEquals(courseList.get(0).getDescription(), course1.getDescription());
	}

	@Test
	public void saveCourse_emptySubject() throws Exception {
		CourseDTO courseDto = new CourseDTO();
		courseDto.setSubject("");
		courseDto.setDescription("test");
		courseDto.setCourseNumber("123");

		try {
			service.saveCourse(courseDto);
			fail();
		} catch (CourseValidationException exception) {
			assertEquals(exception.getMessage(), ExceptionMsg.NULL_SUBJECT.toString());
		}
	}

	@Test
	public void saveCourse_emptyDescription() throws Exception {
		CourseDTO courseDto = new CourseDTO();
		courseDto.setSubject("ENG");
		courseDto.setDescription("");
		courseDto.setCourseNumber("123");

		try {
			service.saveCourse(courseDto);
			fail();
		} catch (CourseValidationException exception) {
			assertEquals(exception.getMessage(), ExceptionMsg.NULL_DESCRIPTION.toString());
		}
	}

	@Test
	public void saveCourse_emptyCourseNum() throws Exception {
		CourseDTO courseDto = new CourseDTO();
		courseDto.setSubject("ENG");
		courseDto.setDescription("test");
		courseDto.setCourseNumber("");

		try {
			service.saveCourse(courseDto);
			fail();
		} catch (CourseValidationException exception) {
			assertEquals(exception.getMessage(), ExceptionMsg.NULL_COURSENUM.toString());
		}
	}

	@Test
	public void saveCourse_courseNum_tooLarge() throws Exception {
		CourseDTO courseDto = new CourseDTO();
		courseDto.setSubject("ENG");
		courseDto.setDescription("test");
		courseDto.setCourseNumber("1234");

		try {
			service.saveCourse(courseDto);
			fail();
		} catch (CourseValidationException exception) {
			assertEquals(exception.getMessage(), ExceptionMsg.NULL_COURSENUM.toString());
		}
	}

	@Test
	public void saveCourse_courseNum_containsLetters() throws Exception {
		CourseDTO courseDto = new CourseDTO();
		courseDto.setSubject("ENG");
		courseDto.setDescription("test");
		courseDto.setCourseNumber("2ab");

		try {
			service.saveCourse(courseDto);
			fail();
		} catch (CourseValidationException exception) {
			assertEquals(exception.getMessage(), ExceptionMsg.INVALID_COURSENUM.toString());
		}
	}

	@Test
	public void saveCourse_subjAndCourseNum_notUnique() throws Exception {
		List<Course> returnList = new ArrayList<>();
		Course course1 = new Course();
		course1.setCourseNumber("123");
		course1.setDescription("This is a test description");
		course1.setId(1);
		course1.setSubject("ENG");
		returnList.add(course1);

		// Mockup what the repo will return; testing the repo/database is not
		// part of this test
		when(repository.findCourseBySubjectAndNumber(Mockito.anyString(), Mockito.anyString())).thenReturn(returnList);

		CourseDTO courseDto = new CourseDTO();
		courseDto.setSubject("ENG");
		courseDto.setDescription("Another description");
		courseDto.setCourseNumber("123");

		try {
			service.saveCourse(courseDto);
			fail();
		} catch (CourseValidationException exception) {
			assertEquals(exception.getMessage(), ExceptionMsg.NON_UNIQUE_SUBJ_AND_COURSENUM.toString());
		}
	}

	@Test
	public void saveCourse() throws Exception {
		// Mockup what the repo will return; testing the repo/database is not
		// part of this test
		when(repository.findCourseBySubjectAndNumber(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		when(repository.save(Mockito.anyObject())).thenReturn(null);

		CourseDTO courseDto = new CourseDTO();
		courseDto.setSubject("ENG");
		courseDto.setDescription("Another description");
		courseDto.setCourseNumber("123");

		service.saveCourse(courseDto);
	}

	@Test
	public void deleteCourse_courseNotFound() throws Exception {
		when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

		CourseDTO courseDto = new CourseDTO();
		courseDto.setSubject("ENG");
		courseDto.setDescription("Another description");
		courseDto.setCourseNumber("123");

		try {
			service.deleteCourse(courseDto);
			fail();
		} catch (CourseValidationException exception) {
			assertEquals(exception.getMessage(), ExceptionMsg.COURSE_NOT_FOUND.toString());
		}
	}

	@Test
	public void deleteCourse() throws Exception {
		CourseDTO courseDto = new CourseDTO();
		courseDto.setSubject("ENG");
		courseDto.setDescription("Another description");
		courseDto.setCourseNumber("123");

		when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(new Course()));
		doNothing().when(repository).delete(Mockito.anyObject());

		service.deleteCourse(courseDto);
	}

	@Test
	public void zeroPadCourseNum_singleDigit() throws Exception {
		Method method = service.getClass().getDeclaredMethod("zeroPadCourseNum", new Class[] { String.class });
		method.setAccessible(true);

		String num = "1";

		String result = (String) method.invoke(service, num);
		assertEquals("001", result);
	}

	@Test
	public void zeroPadCourseNum_twoDigits() throws Exception {
		Method method = service.getClass().getDeclaredMethod("zeroPadCourseNum", new Class[] { String.class });
		method.setAccessible(true);

		String num = "12";

		String result = (String) method.invoke(service, num);
		assertEquals("012", result);
	}

	@Test
	public void zeroPadCourseNum_threeDigits() throws Exception {
		Method method = service.getClass().getDeclaredMethod("zeroPadCourseNum", new Class[] { String.class });
		method.setAccessible(true);

		String num = "123";

		String result = (String) method.invoke(service, num);
		assertEquals("123", result);
	}
}
