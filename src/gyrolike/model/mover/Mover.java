package gyrolike.model.mover;

import gyrolike.model.Game;
import gyrolike.model.exceptions.GameEnd;

import java.lang.reflect.*;

import java.util.List;

public interface Mover {
    int getInterval();
    void move(Game gameStatus) throws GameEnd;

	public static Mover get(String name, List<String> params) {
		try {
			Class<?> clazz = Class.forName("gyrolike.model.mover."+name);
			for(Constructor<?> ctor: clazz.getConstructors()) {
				if(ctor.getParameterCount() != params.size()) continue;

				Class<?>[] aclazzList = ctor.getParameterTypes();
				Object[] args = new Object[params.size()];
				for(int i=0; i<args.length; i++) {
					Class<?> aclazz = aclazzList[i];
					String param = params.get(i);
					Object arg;
					if(aclazz == int.class) arg = Integer.parseInt(param);
					else if(aclazz == double.class) arg = Double.parseDouble(param);
					else if(aclazz == boolean.class) arg = Boolean.parseBoolean(param);
					else throw new IllegalArgumentException("Invalid type for argument "+i+" of "+name+": "+aclazz);
					args[i] = arg;
				}
				return (Mover) ctor.newInstance(args);
			}
			throw new IllegalArgumentException("Invalid number of arguments for constructor of "+name);
		} catch(RuntimeException e) {
			throw e;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
