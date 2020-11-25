package gyrolike.util;

import javax.imageio.ImageIO;

import java.awt.image.*;
import java.io.*;

public class ResourceLoader {
	private ResourceLoader() {} // static-only

	public static InputStream getStream(String path) {
		return ResourceLoader.class.getResourceAsStream(path);
	}

	public static BufferedImage getImage(String path) {
		try(InputStream is = getStream(path)) {
			return ImageIO.read(is);
		} catch(IOException e) {
			throw new RuntimeException("Failed to load image ressource at path "+path, e);
		}
	}

	public static String getText(String path) {
		try(InputStream is = getStream(path)) {
			InputStreamReader rd = new InputStreamReader(is);
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[65536];
			while(true) {
				int a = rd.read(buf);
				if(a<=0) break;
				sb.append(buf, 0, a);
			}
			return sb.toString();
		} catch(IOException e) {
			throw new RuntimeException("Failed to load text ressource at path "+path, e);
		}
	}

	public static byte[] getBinary(String path) {
		try(InputStream is = getStream(path)) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buf = new byte[65536];
			while(true) {
				int a = is.read(buf);
				if(a<=0) break;
				os.write(buf, 0, a);
			}
			return os.toByteArray();
		} catch(IOException e) {
			throw new RuntimeException("Failed to load binary ressource at path "+path, e);
		}
	}
}
