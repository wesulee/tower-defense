package towerdefense.ui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.LinkedList;

/**
 * Draw text inside a rectangle.
 */
public class TextBox
{
	private Font font = new Font("SansSerif", Font.BOLD, 12);
	private final float drawX;
	private final float drawWidth;
	private final float drawYStart;
	private LineBreakMeasurer lineMeasurer;
	private AttributedString description;
	private int paragraphStart;
	private int paragraphEnd;
	private LinkedList<TextLayout> layouts;
	
	public TextBox(String text, int x1, int x2, int y) {
		this.description = new AttributedString(text);
		description.addAttribute(TextAttribute.FONT, font);
		this.drawX = (float)x1;
		this.drawWidth = (float)(x2 - x1);
		this.drawYStart = (float)y;
		layouts = new LinkedList<TextLayout>();
	}
	
	public void draw(Graphics2D g) {
		float drawY = drawYStart;
		
		if (lineMeasurer == null) {
			AttributedCharacterIterator paragraph = 
					description.getIterator();
			paragraphStart = paragraph.getBeginIndex();
			paragraphEnd = paragraph.getEndIndex();
			FontRenderContext frc = g.getFontRenderContext();
			lineMeasurer = new LineBreakMeasurer(paragraph, frc);
			
			lineMeasurer.setPosition(paragraphStart);
			while (lineMeasurer.getPosition() < paragraphEnd) {
				TextLayout layout = lineMeasurer.nextLayout(drawWidth);
				layouts.add(layout);
				drawY += layout.getAscent();
				layout.draw(g, drawX, drawY);
				drawY += layout.getDescent() + layout.getLeading();
			}
		}
		else {
			lineMeasurer.setPosition(paragraphStart);
			for (TextLayout layout : layouts) {
				drawY += layout.getAscent();
				layout.draw(g, drawX, drawY);
				drawY += layout.getDescent() + layout.getLeading();
			}
		}
	}
}
