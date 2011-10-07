package com.drinking.drawmemo.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	int mGalleryItemBackground;
	private Context mContext;
//	private ImageView[] mImages;
	private List<ImageView> mImages;
	private List<String> path;

	public ImageAdapter(Context c,List<String>li){
		mContext = c;
		path=li; 
		mImages = new ArrayList<ImageView>();
	}

	public boolean createReflectedImages() {
		final int reflectionGap = 4;
		int index = 0;

		for (int i=0;i<path.size();i++) {
			Bitmap originalImage = BitmapFactory.decodeFile(path.get(i).toString());
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();

			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);

			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
					height / 2, width, height / 2, matrix, false);

			Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
					(height + height / 2), Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmapWithReflection);

			canvas.drawBitmap(originalImage, 0, 0, null);

			Paint deafaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + reflectionGap,
					deafaultPaint);

			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

			Paint paint = new Paint();
			LinearGradient shader = new LinearGradient(0, originalImage
					.getHeight(), 0, bitmapWithReflection.getHeight()
					+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);

			paint.setShader(shader);

			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
					+ reflectionGap, paint);

			ImageView imageView = new ImageView(mContext);
			imageView.setImageBitmap(bitmapWithReflection);
			imageView.setLayoutParams(new GalleryFlow.LayoutParams(180, 240));
//			imageView.setScaleType(ScaleType.MATRIX);
			mImages.add(imageView);
		}
		return true;
	}

	private Resources getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCount() {
		return mImages.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return mImages.get(position);
	}

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}
	public void delete(int index){
		mImages.remove(index);
	}

}
