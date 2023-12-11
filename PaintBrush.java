import java.applet.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 * The main class representing a simple paint program using Java applet.
 */

public class PaintBrush extends Applet{

    private int x1, y1, x2, y2;
    private Color currentColor = Color.BLACK;
	
/**
 * ArrayList to store the different shapes drawn by the user.
 */	
    private ArrayList<Shapes> shapesList = new ArrayList<>();
	private Shapes currentShape;
	
/**
 * Flag indicating whether a shape should be solid or just an outline.
 * If true, the shape is filled; otherwise, it's an outline.
 */	
	boolean isSolid = false;
	
/**
 * Constants representing different drawing modes: line, rectangle, oval, eraser.
 */	
	int mode;
	public static final int lineMode = 0;
	public static final int rectangleMode = 1;
	public static final int ovalMode = 2;
	public static final int freeDrawMode = 3;
	public static final int eraMode = 4;

/**
 * Initialize the paint program by setting up buttons, event listeners,
 * and initializing the list of shapes. Each button corresponds to a color
 * or a drawing mode.
 */
    public void init(){
		
		shapesList.add(new Lines(x1, y1, x2, y2, currentColor, false));
		shapesList.add(new Rectangles(x1, y1, x2, y2, currentColor, false));
		shapesList.add(new Ovals(x1, y1, x2, y2, currentColor, false));
		
        Button rBut = new Button("Red");
        rBut.setBackground(Color.RED);
        Button gBut = new Button("Green");
        gBut.setBackground(Color.GREEN);
        Button bBut = new Button("Blue");
        bBut.setBackground(Color.BLUE);
		Button kBut = new Button("Black");
		kBut.setBackground(Color.BLACK);
		kBut.setForeground(Color.WHITE);
        Button lineBut = new Button("Line");
        Button rectBut = new Button("Rectangle");
        Button ovalBut = new Button("Oval");
		Button freeDrawBut = new Button("Free Drawing");
        Button eraBut = new Button("Eraser");
		Button undoBut = new Button("Undo");
        Button eraAllBut = new Button("Clear All");
        Checkbox solid = new Checkbox("Solid");

        add(lineBut);
        add(rectBut);
        add(ovalBut);
		add(freeDrawBut);
        add(eraBut);
		add(kBut);
        add(rBut);
        add(gBut);
        add(bBut);
        add(solid);
		add(undoBut);
        add(eraAllBut);


        addMouseListener(new MyListener());
        addMouseMotionListener(new MyListener());
		
		
		solid.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e){

                isSolid = solid.getState();
                repaint();
            }
        });
/**
 * ActionListeners for color buttons (Red, Green, Blue, and Black) to set the current drawing color.
 */		
		kBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae){

                currentColor = Color.BLACK;
                repaint();
            }
        });

        rBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae){

                currentColor = Color.RED;
                repaint();
            }
        });
		
		gBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae){

                currentColor = Color.GREEN;
                repaint();
            }
        });
		
		bBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae){

                currentColor = Color.BLUE;
                repaint();
            }
        });
		
/**
 * ActionListeners for mode buttons (Line, Rectangle, Oval, Free Drawing, and Eraser) to set the current drawing mode.
 */			
		lineBut.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ev) {
				mode = lineMode;
			}
		});

        rectBut.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ev) {
				mode = rectangleMode;
			}
		});
		
		
		ovalBut.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ev) {
				mode = ovalMode;
			}
		});
		
		freeDrawBut.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ev) {
				mode = freeDrawMode;
			}
		});
		
		eraBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae){

                mode = eraMode;
            }
        });
/**
 * ActionListeners for mode buttons (Erase ALl, and Undo) to clear the screen or commit undo.
 */		
		eraAllBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae){

                shapesList.clear();
				mode = -1;
				repaint();
            }
        });
		
		undoBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae){
				
				if(shapesList.size()>0){
					shapesList.remove(shapesList.size()-1);
					repaint();
				}
            }
        });

    }
	
/**
 * Override the paint method to handle drawing on the applet.
 * Draws shapes stored in the shapesList and handles manual drawing based on the current drawing mode.
 */ 
	public void paint(Graphics g){
		
		for (int i=0; i<shapesList.size(); i++){
			
			Shapes currentShape = shapesList.get(i);
			currentShape.draw(g);
		}
		
		switch(mode){
			case lineMode:
					g.drawLine(x1, y1, x2, y2);
			break;
			
			case rectangleMode:
				g.drawRect(x1, y1, x2-x1, y2-y1);
			break;
			
			case ovalMode:
				g.drawOval(x1, y1, x2-x1, y2-y1);
			break;

		}
		
	}
	
/**
 * A custom mouse listener class to handle user input (mouse clicks and drags).
 * Tracks the coordinates of the mouse to draw shapes dynamically.
 */	
	class MyListener extends MouseAdapter{
		

        public void mousePressed(MouseEvent mp){

            x1 = mp.getX();
            y1 = mp.getY();
			
        }
	
        public void mouseDragged(MouseEvent md){

            x2 = md.getX();
            y2 = md.getY();	
			
			switch (mode){
/** 
*Free drawing mode: draw lines as the mouse is dragged.
*/					
				case freeDrawMode:
					currentShape = new Lines(x1, y1, x2, y2, currentColor, isSolid);
					shapesList.add(currentShape);
					repaint();
					x1 = x2;
					y1 = y2;
				break;
/** 
*Eraser mode: draw a white rectangle as the mouse is dragged.
*/					
				case eraMode:
					currentShape = new Rectangles(x1, y1, x2, y2, Color.WHITE, true);
					shapesList.add(currentShape);
					x1=x2;
					y1=y2;
				break;
				
			}
			
			repaint();
        }
/**
 * Called when the mouse is released.
 * Handles different drawing modes and adds the drawn shape to the list.
 */		
        public void mouseReleased(MouseEvent mr){

            x2 = mr.getX();
            y2 = mr.getY();
			
			switch (mode) {
				case lineMode:
					currentShape = new Lines(x1, y1, x2, y2, currentColor, false);
				break;
				
				case rectangleMode:
					currentShape = new Rectangles(x1, y1, x2, y2, currentColor, isSolid);
				break;
				
				case ovalMode:
					currentShape = new Ovals(x1, y1, x2, y2, currentColor, isSolid);
				break;
				
/*				case freeDrawMode:
					if(currentShape != null){
						shapesList.add(currentShape);
					}
				break;
*/	//it did not work as I expected it. I wanted to avoid the issue of getting a null poiter exception when I just do one click on the free hand.	
			}
			
			shapesList.add(currentShape);
            repaint();
			currentShape = null;
        }
		
    }

	
}

/**
 * Abstract class representing a generic shape. Provides a common interface
 * for different types of shapes to implement.
 */
abstract class Shapes{

	protected int x1;
	protected int x2;
	protected int y1;
	protected int y2;
	protected Color shapeColor;
	protected boolean isSolid;



	public Shapes(int x1, int y1, int x2, int y2, Color color, boolean isSolid){

		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.shapeColor = color;
		this.isSolid = isSolid;
	}

	public abstract void draw(Graphics g);



}

/**
 * Subclasses of Shapes representing specific types of shapes.
 * Implements the draw method to render the respective shape.
 */
class Lines extends Shapes{

	public Lines(int x1, int y1, int x2, int y2, Color color, boolean isSolid){

		super(x1, y1, x2, y2, color,false);
	}

	public void draw(Graphics g){

		g.setColor(shapeColor);
		g.drawLine(x1, y1, x2, y2);
	}
}


class Rectangles extends Shapes{

	public Rectangles(int x1, int y1, int x2, int y2, Color color, boolean isSolid){

		super(x1, y1, x2, y2, color, isSolid);
		
	}

	public void draw(Graphics g){

		g.setColor(shapeColor);
/**
*Setting the scenarios of the possible drawing option (in different coordinates) to get the best user experiance.
*/		
		int startPx, startPy, width, height;

        if (x1 < x2) {
            startPx = x1;
            width = x2 - x1;
        } else {
            startPx = x2;
            width = x1 - x2;
        }

        if (y1 < y2) {
            startPy = y1;
            height = y2 - y1;
        } else {
            startPy = y2;
            height = y1 - y2;
        }
		
		if (isSolid) {
			g.fillRect(startPx, startPy, width, height);
		} else {
			g.drawRect(startPx, startPy, width, height);
		}
	}
}


class Ovals extends Shapes{

	public Ovals(int x1, int y1, int x2, int y2, Color color, boolean isSolid){

		super(x1, y1, x2, y2, color, isSolid);
		
	}

	public void draw(Graphics g){

		g.setColor(shapeColor);
/**
*Setting the scenarios of the possible drawing option (in different coordinates) to get the best user experiance.
*/		
		int startPx, startPy, width, height;

        if (x1 < x2) {
            startPx = x1;
            width = x2 - x1;
        } else {
            startPx = x2;
            width = x1 - x2;
        }

        if (y1 < y2) {
            startPy = y1;
            height = y2 - y1;
        } else {
            startPy = y2;
            height = y1 - y2;
        }		

		if (isSolid) {
			g.fillOval(startPx, startPy, width, height);
		} else {
			g.drawOval(startPx, startPy, width, height);
		}
	}
	
}
