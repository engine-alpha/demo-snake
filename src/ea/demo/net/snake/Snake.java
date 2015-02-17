package ea.demo.net.snake;

import ea.Knoten;
import ea.Rechteck;
import ea.Vektor;

import java.awt.*;
import java.util.ArrayList;

public class Snake extends Knoten {
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	
	private ArrayList<Rechteck> parts;
	private boolean alive;
	private Color color;
	private int direction;
	private int x, y;
	private int hue;

	public Snake (int x, int y, int hue) {
		this.x = x * 10;
		this.y = y * 10;
		this.hue = hue;
		this.alive = true;
		this.color = Color.getHSBColor(hue / 255f, .9f, .9f);
		this.direction = (int) (Math.random() * 4);

		Rechteck rechteck = new Rechteck(x + 2, y + 2, 6, 6);
		rechteck.farbeSetzen(color);

		parts = new ArrayList<>();
		parts.add(rechteck);
		add(rechteck);
	}
	
	public void add(int x, int y, boolean add) {
		Rechteck r;
		
		if(!add) {
			r = parts.remove(0);
			r.positionSetzen(x + 2, y + 2);
		} else {
			r = new Rechteck(x + 2, y + 2, 6, 6);
			r.farbeSetzen(color);
			add(r);
		}
		
		parts.add(r);
		
		this.x = x;
		this.y = y;
	}

	public void next (boolean add) {
		Vektor v;

		if (direction == UP) {
			v = new Vektor(x, y - 10);
		} else if (direction == RIGHT) {
			v = new Vektor(x + 10, y);
		} else if (direction == DOWN) {
			v = new Vektor(x, y + 10);
		} else {
			v = new Vektor(x - 10, y);
		}

		this.add((int) v.x, (int) v.y, add);
	}

	public void setDirection(int i) {
		direction = i;
	}

	public Rechteck getHead () {
		return parts.get(parts.size() - 1);
	}

	public Rechteck getTail () {
		return parts.get(0);
	}

	public Knoten getNonHead () {
		Knoten k = new Knoten();
		for(int i = 0; i < parts.size() - 1; i++)
			k.add(parts.get(i));
		return k;
	}

	public int getHue () {
		return hue;
	}

	public void setAlive (boolean value) {
		this.alive = value;
	}

	public boolean isAlive () {
		return alive;
	}
}
