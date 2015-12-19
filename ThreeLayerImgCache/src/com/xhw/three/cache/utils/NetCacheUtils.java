package com.xhw.three.cache.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class NetCacheUtils
{

	private LocalCacheUtils localCacheUtils;
	private MemoryCacheUtils memoryCacheUtils;

	public NetCacheUtils(LocalCacheUtils localCacheUtils,
			MemoryCacheUtils memoryCacheUtils)
	{
		this.localCacheUtils = localCacheUtils;
		this.memoryCacheUtils = memoryCacheUtils;
	}

	/**
	 * 从网络获取图片
	 * 
	 * @param view
	 * @param url
	 */
	public void getBitmapFromNet(ImageView view, String url)
	{
		new BitmapTask().execute(view, url);// 启动AsyncTask,参数能在doInBackground中获取
	}

	/**
	 * 下载图片
	 * 
	 * @param downloadUrl
	 * @return
	 */
	private Bitmap downloadBitmap(String downloadUrl)
	{
		HttpURLConnection conn = null;
		try
		{
			URL url = new URL(downloadUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			conn.connect();
			int responseCode = conn.getResponseCode();
			if (responseCode == 200)
			{
				InputStream is = conn.getInputStream();
				// 图片压缩
//				BitmapFactory.Options options = new Options();
//				options.inSampleSize = 2;// 宽高压缩为原来的二分之一
//				options.inPreferredConfig = Bitmap.Config.RGB_565;// 设置图片格式
//				Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
				
				//不压缩写法
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				return bitmap;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			conn.disconnect();
		}
		return null;
	}

	class BitmapTask extends AsyncTask<Object, Void, Bitmap>
	{
		private ImageView view;
		private String url;

		/**
		 * 后台耗时方法，在此执行,在子线程执行
		 */
		@Override
		protected Bitmap doInBackground(Object... params)
		{
			view = (ImageView) params[0];
			url = (String) params[1];
			view.setTag(url);// 将控件和url绑定
			return downloadBitmap(url);
		}

		// 更新进度，在主线程调用
		@Override
		protected void onProgressUpdate(Void... values)
		{
			super.onProgressUpdate(values);
		}

		// 耗时方法结束后，执行此方法，在主线程调用
		@Override
		protected void onPostExecute(Bitmap result)
		{
			if (result != null)
			{
				String bindUrl = (String) view.getTag();
				if (url.equals(bindUrl))// 保证图片设置给了正确的控件
				{
					view.setImageBitmap(result);
					System.out.println("从网络读取图片...");
					// 保存在本地
					localCacheUtils.setBitmapToLocal(url, result);
					// 内存保存一份
					memoryCacheUtils.setBitmapToMemory(url, result);
				}
			}
		}
	}

	// class BitmapTask extends AsyncTask<X, Y, Z>
	// {
	// /**
	// * 后台耗时方法，在此执行,在子线程执行
	// */
	// @Override
	// protected Z doInBackground(X... params)
	// {
	// return null;
	// }
	//
	// //更新进度，在主线程调用
	// @Override
	// protected void onProgressUpdate(Y... values)
	// {
	// super.onProgressUpdate(values);
	// }
	//
	// //耗时方法结束后，执行此方法，在主线程调用
	// @Override
	// protected void onPostExecute(Z result)
	// {
	// super.onPostExecute(result);
	// }
	// }

}
