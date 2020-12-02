package mmb.testing;

import mmb.DATA.lists.Lists3D;
import mmb.DATA.lists.ReadWriteList3D;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.InventoryUnstackable;
import mmb.debug.Debugger;

class Testing {
	private static Debugger debug = new Debugger("test");

	
	public static void main(String[] args) {
		ReadWriteList3D<String> list;
		String[] a = new String[] {"Home", "Industrial", "Commercial"};
		String[] b = new String[] {"Heating", "Electric", "Motor"};
		String[] c = new String[] {"Reactor", "Jet", "Line"};
		list = Lists3D.<String,String,String,String>crossover(a, b, c, (A, B, C) -> A+" "+B+" "+C);
		list.forEach((s) -> {debug.printl(s);});
	}

}
