package net.cc.qbaseframework.coreutils;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

public class CloseableObjectUtils {
	public static void close(Closeable closeable){
		try{
			if(closeable != null){
				closeable.close();
			}
		} catch(IOException ex){
			Log.e("CloseableObjectUtils", ex.getMessage());
		}
	}
}
