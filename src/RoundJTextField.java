import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JTextField;

public class RoundJTextField extends JTextField {
       private Shape shape;
       public RoundJTextField(int size) {
           super(size);
           setOpaque(false); // As suggested by @AVD in comment.
       }
       protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            super.paintComponent(g);
       }
       protected void paintBorder(Graphics g) {
            g.setColor(getForeground());
            g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
       }
       public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
            return shape.contains(x, y);
       }
   
public RoundJTextField() {
    this(10); // 기본 길이 10으로 설정
}
public void setLineWrap(boolean b) {
	// TODO Auto-generated method stub
	
}
public void setWrapStyleWord(boolean b) {
	// TODO Auto-generated method stub
	
}
}
