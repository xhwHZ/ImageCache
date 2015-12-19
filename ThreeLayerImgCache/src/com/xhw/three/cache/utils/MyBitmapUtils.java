package com.xhw.three.cache.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyBitmapUtils
{
	private NetCacheUtils netCacheUtils;
	private LocalCacheUtils localCacheUtils;
	private MemoryCacheUtils memoryCacheUtils;
	
	
	public MyBitmapUtils()
	{
		memoryCacheUtils=new MemoryCacheUtils();
		localCacheUtils=new LocalCacheUtils();
		netCacheUtils=new NetCacheUtils(localCacheUtils,memoryCacheUtils);
	}
	
	public void display(ImageView view,String url)
	{
		//从内存读,若无，读下一级
		Bitmap bitmapFromMemory = memoryCacheUtils.getBitmapFromMemory(url);
		if(bitmapFromMemory!=null)
		{
			view.setImageBitmap(bitmapFromMemory);
			System.out.println("从内存读取图片...");
			return;
		}
		
		
		//从本地读(sd卡,手机存储),若无，读下一级
		Bitmap bitmapFromLocal = localCacheUtils.getBitmapFromLocal(url);
		if(bitmapFromLocal!=null)
		{
			view.setImageBitmap(bitmapFromLocal);
			System.out.println("从本地读取图片...");
			//内存也保存一份
			memoryCacheUtils.setBitmapToMemory(url, bitmapFromLocal);
			return;
		}
		//从网络读
		netCacheUtils.getBitmapFromNet(view,url);
	}
}
