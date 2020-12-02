package gyrolike.view;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_KP_DOWN;
import static java.awt.event.KeyEvent.VK_KP_LEFT;
import static java.awt.event.KeyEvent.VK_KP_RIGHT;
import static java.awt.event.KeyEvent.VK_KP_UP;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_Z;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import gyrolike.model.Key;
import static gyrolike.model.Key.*;

public class Gamepad extends KeyAdapter {

	@FunctionalInterface
	public static interface KeyListener {
		void accept(Key key, boolean state);

		default BiConsumer<Key, Boolean> toBiConsumer() {
			return (k, v) -> this.accept(k, v);
		}
	}

	@FunctionalInterface
	public static interface BooleanListener {
		void accept(boolean state);

		default Consumer<Boolean> toConsumer() {
			return (v) -> this.accept(v);
		}
	}

	private Map<Integer, Key> keybinds = new HashMap<>();
	{
		keybinds.put(VK_UP, UP);
		keybinds.put(VK_KP_UP, UP);
		keybinds.put(VK_Z, UP);

		keybinds.put(VK_DOWN, DOWN);
		keybinds.put(VK_KP_DOWN, DOWN);
		keybinds.put(VK_S, DOWN);

		keybinds.put(VK_LEFT, LEFT);
		keybinds.put(VK_KP_LEFT, LEFT);
		keybinds.put(VK_Q, LEFT);

		keybinds.put(VK_RIGHT, RIGHT);
		keybinds.put(VK_KP_RIGHT, RIGHT);
		keybinds.put(VK_D, RIGHT);

		keybinds.put(VK_A, RED);
		keybinds.put(VK_E, BLUE);
		keybinds.put(VK_R, CUP);
		keybinds.put(VK_F, CDOWN);
	}

	private Map<Key, Boolean> keys = new HashMap<>();
	{
		for(Key k: Key.values()) keys.put(k, false);
	}

	private List<BiConsumer<Key, Boolean>> listeners = new ArrayList<>();
	private List<Consumer<Map<Key, Boolean>>> stateListeners = new ArrayList<>();

	private synchronized void set(Key k, boolean val) {
		keys.put(k, val);
		for(BiConsumer<Key, Boolean> listener: listeners) listener.accept(k, val);
		if(!stateListeners.isEmpty()) {
			Map<Key, Boolean> state = get();
			for(Consumer<Map<Key, Boolean>> listener: stateListeners) listener.accept(state);
		}
	}
	private void set(KeyEvent e, boolean value) {
		Key key = keybinds.get(e.getKeyCode());
		if(key != null) set(key, value);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		set(e, true);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		set(e, false);
	}

	public void addListener(BiConsumer<Key, Boolean> listener) {
		this.listeners.add(listener);
	}
	public void addListener(KeyListener listener) {
		this.addListener(listener.toBiConsumer());
	}

	public void addListener(Key key, Consumer<Boolean> listener) {
		this.listeners.add((k, v) -> {
			if(k == key) listener.accept(v);
		});
	}
	public void addListener(Key key, BooleanListener listener) {
		this.addListener(key, listener.toConsumer());
	}

	public void addListener(Key key, Runnable listener) {
		this.listeners.add((k, v) -> {
			if(k == key && v) listener.run();
		});
	}

	public void addListener(Consumer<Map<Key, Boolean>> listener) {
		this.stateListeners.add(listener);
	}

	public synchronized Map<Key, Boolean> get() {
		Map<Key, Boolean> m = new HashMap<>();
		m.putAll(keys);
		return Collections.unmodifiableMap(m);
	}
	public synchronized boolean get(Key k) {
		return keys.get(k);
	}
}
