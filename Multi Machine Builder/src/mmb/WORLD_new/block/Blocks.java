/**
 * 
 */
package mmb.WORLD_new.block;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import mmb.SECURITY.permissions.RegistryPermission;
import mmb.WORLD_new.inventory.ItemType;
import mmb.WORLD_new.inventory.Items;
import mmb.debug.Debugger;

import java.util.Map;

/**
 * @author oskar
 *
 */
public class Blocks {
	private static Debugger debug = new Debugger("BLOCKS");
	private static final HashMap<String, BlockType> blocks = new HashMap<String, BlockType>();
	
	private static class BoolVar{
		boolean bol = false;
		public void set(boolean b) {bol = b;}
		public boolean get() {return bol;}
	}
	public static BlockType register(BlockType type) {
		Objects.requireNonNull(type, "The block must not be null");
		Objects.requireNonNull(type.id, "The block's ID can't be null");
		StringBuilder sb = new StringBuilder();
		sb.append("Adding ").append(type.id).append(" with title ").append(type.title).append(" and descripion:\n").append(type.description);
		
		debug.printl("Adding "+sb.toString());
		return blocks.put(type.id, type);
	}
	@SuppressWarnings({"unlikely-arg-type"}) //already declared
	public static boolean remove(BlockType typ) {
		Items.remove(typ);
		return null != blocks.remove(typ);
		
	}
	/**
	 * Get a block with following ID, or null if block with given ID is not found
	 * @param name ID of the block
	 * @return a block with given name, or null if not found
	 */
	public static BlockType get(String name) {
		return blocks.get(name);
	}
	/**
	 * Returns a dictionary view of the block list. The dictionary is backed by the registry, so changes to the registry are reflected in the dictionary, and vice-versa.
	 * In the 'put' method, you can leave the first input null, if block has non-null name.
	 * @return dictionary view of block registry
	 */
	public static Dictionary<String, BlockType> dictionaryBlocks(){
		return new Dictionary<String, BlockType>(){

			@Override
			public Enumeration<BlockType> elements() {
				return Collections.enumeration(blocks.values());
			}

			@Override
			public BlockType get(Object arg0) {
				return blocks.get(arg0);
			}

			@Override
			public boolean isEmpty() {
				return blocks.isEmpty();
			}

			@Override
			public Enumeration<String> keys() {
				return Collections.enumeration(blocks.keySet());
			}

			@Override
			public BlockType put(String key, BlockType value) {
				if(key != null) value.id = key;
				return register(value);
			}

			@Override
			public BlockType remove(Object arg0) {
				if(arg0 instanceof String) {
					return blocks.remove(arg0);
				}else if(arg0 instanceof BlockType) {
					return blocks.remove(((BlockType) arg0).id);
				}else return null;
			}

			@Override
			public int size() {
				return blocks.size();
			}
			
		};
	}
	/**
	 * Returns a map view of the block list. The map is backed by the registry, so changes to the registry are reflected in the map, and vice-versa.
	 * In the 'put' method, you can leave the first input null, if block has non-null name.
	 * The 'clear' methods are protected by RegistryPermission('eraseContent') permission, which requires user consent.
	 * The keySet() is read only
	 * The retainAll() methods are not implemented, because they are too cumbersome.
	 * @return map view of block registry
	 */
	public static Map<String, BlockType> mapBlocks(){
		return new Map<String,BlockType>(){
			private Set<Entry<String, BlockType>> set = blocks.entrySet();
			private Set<String> keys = blocks.keySet();
			private Collection<BlockType> values = blocks.values();
			@Override
			public void clear() {
				SecurityManager security = System.getSecurityManager();
		        if(security != null){
		        	security.checkPermission(new RegistryPermission("eraseContent"));
		        }
		        blocks.clear();
			}

			@Override
			public boolean containsKey(Object arg0) {
				return blocks.containsKey(arg0);
			}

			@Override
			public boolean containsValue(Object arg0) {
				return blocks.containsValue(arg0);
			}

			@Override
			public Set<Entry<String, BlockType>> entrySet() {
				return new Set<Entry<String, BlockType>>(){
					@Override
					public boolean add(Entry<String, BlockType> e) {
						if(e.getKey() != null) e.getValue().id = e.getKey();
						try {
							return register(e.getValue()) == e.getValue();
						}catch(Exception ex) {
							return false;
						}
					}
					private BoolVar bv = new BoolVar();
					@Override
					public boolean addAll(Collection<? extends Entry<String, BlockType>> c) {
						bv.set(false);
						c.forEach((e) -> {
							bv.set(bv.bol | add(e));
						});
						return bv.bol;
					}

					@Override
					public void clear() {
						SecurityManager security = System.getSecurityManager();
				        if(security != null){
				        	security.checkPermission(new RegistryPermission("eraseContent"));
				        }
				        blocks.clear();
					}

					@Override
					public boolean contains(Object o) {
						if(o instanceof String) return blocks.containsKey(o);
						if(o instanceof BlockType) return blocks.containsKey(o);
						if(o instanceof Entry) {
							Entry<?, ?> ent = (Entry<?, ?>)o;
							Object key = ent.getKey();
							Object value = ent.getValue();
							if(key instanceof String) return blocks.containsKey(key);
							if(value instanceof BlockType) return blocks.containsValue(value);
						}
						return false;
					}

					@Override
					public boolean containsAll(Collection<?> c) {
						return blocks.keySet().containsAll(c);
					}

					@Override
					public boolean isEmpty() {
						return blocks.isEmpty();
					}

					@Override
					public Iterator<Entry<String, BlockType>> iterator() {
						return set.iterator();
					}

					@Override
					public boolean remove(Object o) {
						if(o instanceof String) return Blocks.remove((String) o);
						if(o instanceof BlockType) return Blocks.remove((BlockType) o);
						if(o instanceof Entry) {
							Entry<?, ?> ent = (Entry<?, ?>)o;
							Object key = ent.getKey();
							Object value = ent.getValue();
							if(key instanceof String) return blocks.containsKey(key);
							if(value instanceof BlockType) return blocks.containsValue(value);
						}
						return false;
					}

					@Override
					public boolean removeAll(Collection<?> c) {
						bv.bol = false;
						c.forEach((i) -> {bv.set(bv.bol | remove(i));});
						return bv.bol;
					}

					@Override
					public boolean retainAll(Collection<?> c) {
						return false;
					}

					@Override
					public int size() {
						return blocks.size();
					}

					@Override
					public Object[] toArray() {
						return blocks.entrySet().toArray();
					}

					@Override
					public <T> T[] toArray(T[] a) {
						return blocks.entrySet().toArray(a);
					}
					
				};
			}

			@Override
			public BlockType get(Object arg0) {
				return blocks.get(arg0);
			}

			@Override
			public boolean isEmpty() {
				return blocks.isEmpty();
			}

			@Override
			public Set<String> keySet() {
				return Collections.unmodifiableSet(blocks.keySet());
			}

			@Override
			public BlockType put(String key, BlockType value) {
				if(key != null) value.id = key;
				return register(value);
			}

			@Override
			public void putAll(Map<? extends String, ? extends BlockType> data) {
				data.forEach(this::put);
			}

			@Override
			public BlockType remove(Object arg0) {
				if(arg0 instanceof String) {
					return blocks.remove(arg0);
				}else if(arg0 instanceof BlockType) {
					return blocks.remove(((BlockType) arg0).id);
				}else return null;
			}

			@Override
			public int size() {
				return blocks.size();
			}

			@Override
			public Collection<BlockType> values() {
				return new Collection<BlockType>() {
					private BoolVar bv = new BoolVar();
					@Override
					public boolean add(BlockType block) {
						return register(block) != null;
					}

					@Override
					public boolean addAll(Collection<? extends BlockType> blocks) {
						bv.bol = false;
						blocks.forEach((block) -> {bv.bol |= register(block) != null;});
						return bv.bol;
					}

					@Override
					public void clear() {
						SecurityManager security = System.getSecurityManager();
				        if(security != null){
				        	security.checkPermission(new RegistryPermission("eraseContent"));
				        }
				        blocks.clear();
					}

					@Override
					public boolean contains(Object block) {
						return blocks.containsValue(block);
					}

					@Override
					public boolean containsAll(Collection<?> arg0) {
						return blocks.values().containsAll(arg0);
					}

					@Override
					public boolean isEmpty() {
						return blocks.isEmpty();
					}

					@Override
					public Iterator<BlockType> iterator() {
						return values.iterator();
					}

					@Override
					public boolean remove(Object arg0) {
						return blocks.remove(((ItemType)arg0).id) != null;
					}

					@Override
					public boolean removeAll(Collection<?> arg0) {
						bv.bol = false;
						arg0.forEach((item) -> {bv.bol |= remove(item);});
						return bv.bol;
					}

					@Override
					public boolean retainAll(Collection<?> arg0) {
						return false;
					}

					@Override
					public int size() {
						return blocks.size();
					}

					@Override
					public BlockType[] toArray() {
						return blocks.values().toArray(new BlockType[blocks.size()]);
					}

					@Override
					public <T> T[] toArray(T[] arr) {
						return blocks.values().toArray(arr);
					}
					
				};
			}
			
		};
	}
	/**
	 * Remove given block by name
	 * @param s block name
	 * @return if block as removed
	 */
	public static boolean remove(String s) {
		Objects.requireNonNull(s, "Block name can't be null");
		debug.printl("Removing "+s);
		boolean bool = blocks.remove(s) != null;
				bool = Items.remove(s) != null || bool;
		return  bool;
	}
	public static void forEach(Consumer<BlockType> action) {
		blocks.forEach((s, b) -> action.accept(b));
	}
}
