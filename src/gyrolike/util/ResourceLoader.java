package gyrolike.util;

import javax.imageio.ImageIO;

import java.awt.image.*;
import java.io.*;
import java.util.*;

public class ResourceLoader {
	private ResourceLoader() {} // static-only

	public static InputStream getStream(String path) {
		System.out.println("getStream("+path+")");
		return ResourceLoader.class.getResourceAsStream(path);
	}

	public static BufferedImage getImage(String path) {
		try(InputStream is = getStream(path)) {
			return ImageIO.read(is);
		} catch(IOException e) {
			throw new RuntimeException("Failed to load image resource at path "+path, e);
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
			throw new RuntimeException("Failed to load text resource at path "+path, e);
		}
	}

	public static List<String> getLines(String path) {
		try(InputStream is = getStream(path)) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			List<String> lines = new ArrayList<>();
			String line;
			while((line = rd.readLine()) != null) {
				lines.add(line);
			}
			return lines;
		} catch(IOException e) {
			throw new RuntimeException("Failed to load line resource at path "+path, e);
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
			throw new RuntimeException("Failed to load binary resource at path "+path, e);
		}
	}
}
