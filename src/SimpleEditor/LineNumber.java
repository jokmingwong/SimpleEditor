package SimpleEditor;

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
	public final Color D_BACKGROUD = new Color(220, 225, 214);
	private final Font D_FONT = new Font("黑体", Font.PLAIN, 22);
	public final Color D_FOREGROUD = Color.BLACK;
	private int fontLineHeight;
	private FontMetrics fontMetrics;
	private int lineHeight;
	public final int MARGIN = 2;
	public final int maxHEIGHT = Integer.MAX_VALUE - 999999;
	private int nowRowWidth;
	private final int STARTOFFSET = 4;

	public LineNumber() {
		setFont(D_FONT);
		setForeground(D_FOREGROUD);
		setBackground(D_BACKGROUD);
		setPreferredSize(9999);
	}

	public int getLineHeight() {
		if (lineHeight == 0) {
			return fontLineHeight;
		}
		return lineHeight;
	}

	public int getStartOffset() {
		return STARTOFFSET;
	}

	@Override
	protected void paintComponent(Graphics g) {
		int nowLineHeight = getLineHeight();
		int nowStartOffset = getStartOffset();
		Rectangle drawHere = g.getClipBounds();
		g.setColor(getBackground());
		g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);
		g.setColor(getForeground());
		int startLineNum = (drawHere.y / nowLineHeight) + 1;
		int endLineNum = startLineNum + (drawHere.height / nowLineHeight);
		int start = (drawHere.y / nowLineHeight) * nowLineHeight + nowLineHeight - nowStartOffset;
		for (int i = startLineNum; i <= endLineNum; ++i) {
			String lineNum = String.valueOf(i);
			int width = fontMetrics.stringWidth(lineNum);
			g.drawString(lineNum + " ", MARGIN + nowRowWidth - width - 1, start);
			start += nowLineHeight;
		}
		setPreferredSize(endLineNum);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		fontMetrics = getFontMetrics(getFont());
		fontLineHeight = fontMetrics.getHeight();
	}

	public void setLineHeight(int lineHeight) {
		if (lineHeight > 0) {
			this.lineHeight = lineHeight;
		}
	}

	public void setPreferredSize(int row) {
		int width = fontMetrics.stringWidth(String.valueOf(row));
		if (nowRowWidth < width) {
			nowRowWidth = width;
			setPreferredSize(new Dimension(2 * MARGIN + width + 1, maxHEIGHT));
		}
	}
}
