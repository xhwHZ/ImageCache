package com.xhw.three.cache.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存
 * 
 * @author admin
 *
 */
public class MemoryCacheUtils
{
	//android2.3以后，软引用会被提前回收，即使内存够用
	//private Map<String, SoftReference<Bitmap>> memoryCache = new HashMap<String, SoftReference<Bitmap>>();

	//Lru Least recently use 最少最近使用算法
	private LruCache<String, Bitmap> memoryCache;
	
	public MemoryCacheUtils()
	{
		//获取手机分配给应用的最大内存，模拟器是16mb
		long maxMemory = Runtime.getRuntime().maxMemory();
		//LruCache要指定最大内存大小，若缓存超过此阀值，会自动回收
		memoryCache=new LruCache<String, Bitmap>((int)(maxMemory/8)){
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				//获取bitmap占用内存大小
				int size=value.getRowBytes()*value.getHeight();
				return size;
			}
		};
	}
	
	public void setBitmapToMemory(String url, Bitmap bitmap)
	{
//		SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);
//		memoryCache.put(url, softReference);
		
		memoryCache.put(url, bitmap);
	}

	public Bitmap getBitmapFromMemory(String url)
	{
//		SoftReference<Bitmap> softReference = memoryCache.get(url);
//		if (softReference != null)
//		{
//			return softReference.get();
//		}
//		return null;
		return memoryCache.get(url);
	}
}
