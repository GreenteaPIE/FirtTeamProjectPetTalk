import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PaintlGame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

	private static void createAndShowGUI() {
		System.out.println("Created GUI on EDT?" + SwingUtilities.isEventDispatchThread());
		JFrame f = new JFrame("Swing Paint Demo");

		RectPanel rectPanel = new RectPanel();
		f.add(rectPanel, BorderLayout.NORTH);
		f.pack();
		f.setVisible(true);

	}

}

class RectPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

	String shapeString = ""; // 도형의 형태를 담는 변수

	Point firstPointer = new Point(0, 0);
	Point secondPointer = new Point(0, 0);

	BufferedImage bufferedImage;

	Color colors = Color.black;

	Float stroke = (float) 5;

	JComboBox<Color> colorComboBox;
	JComboBox<Float> strokeComboBox; // float로 설정해주는 이유는 setStroke에서 받는 인자 자료형이 float

	int width;
	int height;
	int minPointx;
	int minPointy;

	public RectPanel() {

		colorComboBox = new JComboBox<Color>();
		strokeComboBox = new JComboBox<Float>();

		RoundedButton lineButton = new RoundedButton("LINE");

		RoundedButton penButton = new RoundedButton("PEN");
		RoundedButton eraseButton = new RoundedButton("ERASER");

		colorComboBox.setModel(new DefaultComboBoxModel<Color>(new Color[] { Color.black, Color.red, Color.blue,
				Color.green, Color.yellow, Color.pink, Color.magenta }));

		ColorWithName[] colors = { new ColorWithName(Color.black, "검은색"), new ColorWithName(Color.red, "빨간색"),
				new ColorWithName(Color.blue, "파란색"), new ColorWithName(Color.green, "초록색"),
				new ColorWithName(Color.yellow, "노랑색"), new ColorWithName(Color.pink, "분홍색"),
				new ColorWithName(Color.magenta, "자홍색") };
		JComboBox<ColorWithName> colorComboBox = new JComboBox<>(colors);

		strokeComboBox.setModel(new DefaultComboBoxModel<Float>(
				new Float[] { (float) 5, (float) 10, (float) 15, (float) 20, (float) 25 }));

		add(penButton);
		add(lineButton);

		add(colorComboBox);
		add(strokeComboBox);
		add(eraseButton);

		/////////
		Dimension d = getPreferredSize();
		bufferedImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
		setImageBackground(bufferedImage); // save 할 때 배경이 default로 black이여서 흰색으로

		lineButton.addActionListener(this); // 선

		penButton.addActionListener(this); // 펜
		eraseButton.addActionListener(this); // 지우개~
		colorComboBox.addActionListener(this);
		strokeComboBox.addActionListener(this);

		addMouseListener(this);
		addMouseMotionListener(this);

	}

	public void mousePressed(MouseEvent e) {

		// 다시 클릭됐을경우 좌표 초기화
		firstPointer.setLocation(0, 0);
		secondPointer.setLocation(0, 0);

		firstPointer.setLocation(e.getX(), e.getY());

	}

	public void mouseReleased(MouseEvent e) {

		if (shapeString != "PEN") {
			secondPointer.setLocation(e.getX(), e.getY());
			updatePaint();
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().getClass().toString().contains("RoundedButton")) {
			shapeString = e.getActionCommand();
		}

		else if (e.getSource().equals(colorComboBox)) {
			colors = (Color) colorComboBox.getSelectedItem();
		}

		else if (e.getSource().equals(strokeComboBox)) {
			stroke = (float) strokeComboBox.getSelectedItem();
		}

	}

	public Dimension getPreferredSize() {
		return new Dimension(500, 700);
	}

	public void updatePaint() {

		width = Math.abs(secondPointer.x - firstPointer.x);
		height = Math.abs(secondPointer.y - firstPointer.y);

		minPointx = Math.min(firstPointer.x, secondPointer.x);
		minPointy = Math.min(firstPointer.y, secondPointer.y);

		Graphics2D g = bufferedImage.createGraphics();

		// draw on paintImage using Graphics
		switch (shapeString) {

		case ("LINE"):
			g.setColor(colors);
			g.setStroke(new BasicStroke(stroke));
			g.drawLine(firstPointer.x, firstPointer.y, secondPointer.x, secondPointer.y);

			break;

		case ("PEN"):
			g.setColor(colors);
			g.setStroke(new BasicStroke(stroke));
			g.drawLine(firstPointer.x, firstPointer.y, secondPointer.x, secondPointer.y);
			break;

		case ("ERASER"):
			g.setColor(Color.white);
			g.setStroke(new BasicStroke(stroke));
			g.drawLine(firstPointer.x, firstPointer.y, secondPointer.x, secondPointer.y);
			break;

		default:
			break;

		}

		g.dispose();

		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bufferedImage, 0, 0, null);

	}

	public void setImageBackground(BufferedImage bi) {
		this.bufferedImage = bi;
		Graphics2D g = bufferedImage.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, 500, 700);
		g.dispose();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

		width = Math.abs(secondPointer.x - firstPointer.x);
		height = Math.abs(secondPointer.y - firstPointer.y);

		minPointx = Math.min(firstPointer.x, secondPointer.x);
		minPointy = Math.min(firstPointer.y, secondPointer.y);

		if (shapeString == "PEN" | shapeString == "ERASER") {
			if (secondPointer.x != 0 && secondPointer.y != 0) {
				firstPointer.x = secondPointer.x;
				firstPointer.y = secondPointer.y;
			}
			secondPointer.setLocation(e.getX(), e.getY());
			updatePaint();
		} else if (shapeString == "LINE") {

			Graphics g = getGraphics();

			g.drawLine(firstPointer.x, firstPointer.y, secondPointer.x, secondPointer.y);
			secondPointer.setLocation(e.getX(), e.getY());
			repaint();
			g.dispose();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public class ColorWithName {
		private Color color;
		private String name;

		public ColorWithName(Color color, String name) {
			this.color = color;
			this.name = name;
		}

		public Color getColor() {
			return color;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

}