package coursesApplication.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import coursesApplication.model.Course;

public class CourseRepositoryImpl implements CourseRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * Searches for courses by the description field, using a 'LIKE' comparison.
	 * For example, 'bio' will match descriptions containing 'biology'
	 * 
	 * @param description
	 *            The description to search by.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Course> findCourseByDescription(String description) {
		Query query = entityManager.createNativeQuery(
				"Select * from course where description LIKE ? order by subject, course_number", Course.class);
		query.setParameter(1, "%" + description + "%");

		return query.getResultList();
	}

	/**
	 * Searches for courses by the subject and courseNumber fields, as the
	 * combination of the two should be unique.
	 * 
	 * @param subject
	 *            The subject field to match
	 * @param courseNumber
	 *            The course number field to match
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Course> findCourseBySubjectAndNumber(String subject, String courseNumber) {
		Query query = entityManager.createNativeQuery("Select * from course where subject = ? and course_number = ?",
				Course.class);
		query.setParameter(1, subject);
		query.setParameter(2, courseNumber);

		return query.getResultList();
	}

}
