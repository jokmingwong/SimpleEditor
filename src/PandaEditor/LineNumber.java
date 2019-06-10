package PandaEditor;


/*
 * LineNumber.java
 * @author LiNan
 *设置行号
 *UI中请在
 *JScrollPane scrollPane = new JScrollPane(textArea);
 *后加入
 *LineNumber lineNumber = new LineNumber();
 *scrollPane.setRowHeaderView(lineNumber);
 *更改字体的fontType.addActionListener(里面也要改  目前是加一句
 *lineNumber.setFont(new Font(p, Font.PLAIN, s));
 *下面更改字号的fontSize.addActionListener(同理
 * */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class LineNumber extends javax.swing.JComponent {

	private static final long serialVersionUID = 1L;
	private final Color BACKGROUD = new Color(220, 225, 214);
	private final Color FOREGROUD = Color.BLACK;
	private final Font FONT = new Font("黑体", Font.BOLD, 22);
	private final int MARGIN = 3;
	private final int maxHEIGHT = Integer.MAX_VALUE - 999999;
	private final int STARTOFFSET = 4;
	private FontMetrics fontMetrics;
	private int lineHeight;
	private int rowWidth;

	public LineNumber() {
		setFont(FONT);
		setForeground(FOREGROUD);
		setBackground(BACKGROUD);
		setSize("8192");
	}

	private int getLineHeight() {
        if (lineHeight <= 0) {
            return fontMetrics.getHeight();
        }
        return lineHeight;
    }

	@Override
	protected void paintComponent(Graphics graphics) {
		graphics.setColor(getBackground());
		Rectangle rectangle = graphics.getClipBounds();
		graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		graphics.setColor(getForeground());
		graphics.setFont(FONT);//如果要更改字体请注释掉此句
		int nowheight = getLineHeight();
		int beginLine = (rectangle.y / nowheight) + 1;
		int endLine = beginLine + (rectangle.height / nowheight);
		int where = beginLine * nowheight - STARTOFFSET;
		for (int i = beginLine; i <= endLine; i++) {
			String lineNum = String.valueOf(i);
			int width = fontMetrics.stringWidth(lineNum);
			graphics.drawString(lineNum + " ", MARGIN + rowWidth - width - 1, where);
			where += nowheight;
		}
		setSize(String.valueOf(endLine));
	}
	
	/**
	 * @author Linan
	 * @param font construct a font by "new Font(name, bold or not bold, int size)"
	 */
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		fontMetrics = getFontMetrics(getFont());
		int height = fontMetrics.getHeight();
		if (height  > 0) {
			this.lineHeight = height;
		}
	}


	private void setSize(String row) {
		int width = fontMetrics.stringWidth(row);
		if (rowWidth < width) {
			rowWidth = width;
			setPreferredSize(new Dimension(2 * MARGIN + width + 1, maxHEIGHT));
		}
	}
}
