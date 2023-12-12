package game;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class Cell extends JLabel {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private Icon hidden;
	private Icon revealed;
	
	public Cell(int id, BufferedImage imageRevealed, BufferedImage imageHidden) {
		this.id = id;

		this.hidden = new ImageIcon(imageHidden.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
		this.revealed = new ImageIcon(imageRevealed.getScaledInstance(200, 200, Image.SCALE_SMOOTH));

		setIcon(hidden);
		
		this.setBorder(new LineBorder(new Color(0, 0, 0)));
//		this.setHorizontalAlignment(SwingConstants.CENTER);
		
		
	}
	
	public void reveal() {
		setIcon(revealed);
	}
	
	public void hide() {
		setIcon(hidden);
	}
	
	public void cleanListener() {
		MouseListener listener = this.getMouseListeners()[0];
		this.removeMouseListener(listener);
	}
	
	public int getId() {
		return this.id;
	}
}