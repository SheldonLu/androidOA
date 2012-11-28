package com.mzoneapp.zjjmb.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class FileUtil {
	public static  void openFile(Context ctx, File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		ctx.startActivity(intent);
	}

	private static String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else {
			type = "*";
		}
		type += "/*";
		
		if(end.equals("doc")){
			type="application/msword";
		}else if(end.equals("docx")){
			type="application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}
		return type;
	}
}
