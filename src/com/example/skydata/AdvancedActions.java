package com.example.skydata;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

public class AdvancedActions {

	public static void shareElement(View element, Activity activity, String filename){
		if(element == null){
			element = activity.findViewById(R.id.drawer_layout);
		}else{
			element.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		element.setDrawingCacheEnabled(true);
		element.getRootView();
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) 
		{
			File picDir  = new File(Environment.getExternalStorageDirectory()+ "/DCIM/100ANDRO");
			if (!picDir.exists())
			{
				picDir.mkdir();
			}
			element.buildDrawingCache();
			Bitmap bitmap = element.getDrawingCache();
			String fileName = filename + ".jpeg";
			File picFile = new File(picDir + "/" + fileName);
			try 
			{
				picFile.createNewFile();
				FileOutputStream picOut = new FileOutputStream(picFile);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
				@SuppressWarnings("unused")
				boolean saved = bitmap.compress(CompressFormat.JPEG, 100, picOut);
				picOut.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			element.destroyDrawingCache();
		} 
		element.setBackgroundColor(Color.TRANSPARENT);
		Intent sharingIntent = new Intent();
		sharingIntent.setAction(Intent.ACTION_SEND);
		sharingIntent.setType("image/*");
		String uri = Environment.getExternalStorageDirectory()+ "/DCIM/100ANDRO/" + filename +".jpeg";
		File imageFileToShare = new File(uri);
		Uri uril = Uri.fromFile(imageFileToShare);
		sharingIntent.putExtra(Intent.EXTRA_STREAM, uril);
		activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}


}
