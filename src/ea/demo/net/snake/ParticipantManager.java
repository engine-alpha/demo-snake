package ea.demo.net.snake;

import java.util.LinkedList;
import java.util.List;

public class ParticipantManager {
	private List<Participant> participants;

	public ParticipantManager () {
		participants = new LinkedList<>();
	}

	public ParticipantManager add (Participant participant) {
		participants.add(participant);

		return this;
	}

	public ParticipantManager remove (Participant participant) {
		participants.remove(participant);

		return this;
	}

	public int count () {
		return participants.size();
	}
}
