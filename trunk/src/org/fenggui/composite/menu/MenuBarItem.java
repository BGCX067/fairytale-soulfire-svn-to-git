package org.fenggui.composite.menu;



public class MenuBarItem 
{
	private Menu menu = null;
	private String text = "";
	
	protected MenuBarItem(Menu menu, String name)
	{
		this.menu = menu;
		this.text = name;
	}
	
	public Menu getMenu()
	{
		return menu;
	}


	public String getName()
	{
		return text;
	}
}
