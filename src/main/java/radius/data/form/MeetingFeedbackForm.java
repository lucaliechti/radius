package radius.data.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MeetingFeedbackForm {

	@NotEmpty
	private String nextState;

	private boolean confirmed;

}
