package mmb.menu.components;

import javax.swing.Icon;
import javax.swing.JLabel;

import mmb.Nil;
import mmb.menu.MenuHelper;

public class MultilineImageLabel extends JLabel {
	private static final long serialVersionUID = -4534631723178173988L;
	@Nil private String string = "";
	public MultilineImageLabel() {
		super();
	}
	public MultilineImageLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}
	public MultilineImageLabel(Icon image) {
		super(image);
	}
	public MultilineImageLabel(String text, Icon icon, int horizontalAlignment) {
		super(icon, horizontalAlignment);
		setText(text);
	}
	public MultilineImageLabel(String text, int horizontalAlignment) {
		setHorizontalAlignment(horizontalAlignment);
		setText(text);
	}
	public MultilineImageLabel(String text) {
		super();
		setText(text);
	}
	@Override
	public String getText() {
		return string;
	}
	/** @return current escaped text */
	public String getHTMLText() {
		return super.getText();
	}
	@Override
	public void setText(@Nil String text) {
		string = text;
		if(text != null && !text.startsWith("<html>") && text.contains("\n")) 
			super.setText(MenuHelper.htmlescape(text));
		else 
			super.setText(text);
	}
	
}
