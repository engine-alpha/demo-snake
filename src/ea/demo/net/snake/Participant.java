package ea.demo.net.snake;

import ea.Empfaenger;

public class Participant implements Empfaenger {
	private ParticipantManager manager;
	private String remoteIp;

	public Participant (String ip, ParticipantManager participantManager) {
		this.remoteIp = ip;
		this.manager = participantManager;
	}

	@Override
	public void empfangeString (String s) {

	}

	@Override
	public void empfangeInt (int i) {

	}

	@Override
	public void empfangeByte (byte b) {

	}

	@Override
	public void empfangeDouble (double v) {

	}

	@Override
	public void empfangeChar (char c) {

	}

	@Override
	public void empfangeBoolean (boolean b) {

	}

	@Override
	public void verbindungBeendet () {
		manager.remove(this);
	}
}
