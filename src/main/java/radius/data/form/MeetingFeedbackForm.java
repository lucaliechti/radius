package radius.data.form;

import javax.validation.constraints.NotEmpty;

public class MeetingFeedbackForm {
	
	private boolean confirmed;
	@NotEmpty
	private String nextState;
	
	public boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean meetingConfirmed) {
		this.confirmed = meetingConfirmed;
	}

	public String getNextState() {
		return nextState;
	}

	public void setNextState(String _nextState) {
		this.nextState = _nextState;
	}
}
