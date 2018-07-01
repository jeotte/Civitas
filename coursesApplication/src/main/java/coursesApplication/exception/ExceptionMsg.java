package coursesApplication.exception;

public enum ExceptionMsg {

	NULL_SUBJECT("The subject must be provided."),

	NULL_DESCRIPTION("The description must be provided."),

	NULL_COURSENUM("The course number must be provided and be 1-999"),

	INVALID_COURSENUM("The course number can only contain digits."),

	NON_UNIQUE_SUBJ_AND_COURSENUM(
			"A record already exists for this subject and course number.  The combination must be unique."),

	COURSE_NOT_FOUND("The course record was not found.");

	private final String text;

	private ExceptionMsg(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

}
