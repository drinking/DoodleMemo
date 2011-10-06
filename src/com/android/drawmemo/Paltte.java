package com.android.drawmemo;

import com.android.drawmemo.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
public class Paltte extends Activity implements OnClickListener,OnCheckedChangeListener{

	ImageView []paltte=new ImageView[10];
	int []colors_id={R.id.color_red,R.id.color_orange,R.id.color_yellow,
					 R.id.color_green,R.id.color_light_blue,R.id.color_blue,
					 R.id.color_purple,R.id.color_gray,R.id.color_white,
					 R.id.color_black};
	int []colors_drawable={R.drawable.paltte_red,R.drawable.paltte_orange,R.drawable.paltte_yellow,
						   R.drawable.paltte_green,R.drawable.paltte_lightblue,R.drawable.paltte_blue,
						   R.drawable.paltte_purple,R.drawable.paltte_gray,R.drawable.paltte_white,
						   R.drawable.paltte_black};
	int []colors={Color.RED,0xffff7e00,Color.YELLOW,Color.GREEN,0xff00e4ff,Color.BLUE,0xfffc00ff,Color.GRAY,Color.WHITE,Color.BLACK};
	int []strokes_width={5,10,20,40};
	ImageView stroke_width_1;
	Button confirm;
	Button cancel;
	int result_color=-1;
	int result_width=-1;
	
	RadioButton stroke_width5;
	RadioButton stroke_width10;
	RadioButton stroke_width20;
	RadioButton stroke_width40;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.paltte);
		
		for(int i=0;i<colors_id.length;i++) {
			
			paltte[i]=(ImageView)findViewById(colors_id[i]);
			paltte[i].setOnClickListener(this);
		}

		confirm=(Button)findViewById(R.id.confirm);
		cancel=(Button)findViewById(R.id.cancel);
		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		stroke_width5=(RadioButton)findViewById(R.id.stroke_width_5);
		stroke_width10=(RadioButton)findViewById(R.id.stroke_width_10);
		stroke_width20=(RadioButton)findViewById(R.id.stroke_width_20);
		stroke_width40=(RadioButton)findViewById(R.id.stroke_width_40);

		stroke_width5.setOnCheckedChangeListener(this);
		stroke_width10.setOnCheckedChangeListener(this);
		stroke_width20.setOnCheckedChangeListener(this);
		stroke_width40.setOnCheckedChangeListener(this);
		
	}
	@Override
	public void onClick(View v) {
			
			int index=getColorIndex(v.getId());
			if(index!=-1) {
			 clearPaltte();
			 Bitmap	color = BitmapFactory.decodeResource(getResources(),colors_drawable[index]);
	         Bitmap picked = BitmapFactory.decodeResource(getResources(),R.drawable.color_picked);
	         Drawable []array=new Drawable[2];
	         array[0]=new BitmapDrawable(color);
	         array[1]=new BitmapDrawable(picked);
	         LayerDrawable la=new LayerDrawable(array);
	         la.setLayerInset(0,0,0,0,0);
	         la.setLayerInset(1, 0,0,0,0);
			 paltte[index].setImageDrawable(la);
			 result_color=colors[index];
			 return;
			}
			if(v.getId()==R.id.confirm){
				Intent data=new Intent();
				Bundle bundle=new Bundle();
				bundle.putInt("Color", result_color);
				bundle.putInt("Width", result_width);
				data.putExtras(bundle);
				Paltte.this.setResult(Activity.RESULT_OK, data);
				Paltte.this.finish();

			}else if(v.getId()==R.id.cancel){
				Paltte.this.setResult(Activity.RESULT_CANCELED);
				Paltte.this.finish();
			}
	}
	private int getColorIndex(int id) {
		
		for(int i=0;i<colors_id.length;i++) {
			if(colors_id[i]==id)
				return i;
		}
		return -1;
	}

	private void clearPaltte(){
	
		for(int i=0;i<10;i++){
			paltte[i].setImageResource(colors_drawable[i]);
		}
	}
	
	private Bitmap getStrokeImage(int width,int color) {
		
        final Bitmap bitmap = Bitmap.createBitmap(400,100 , Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();
        
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(width);
        Path path=new Path();
        path.moveTo(20, 50);
        path.lineTo(380, 50);
        canvas.drawPath(path, paint);
        return bitmap;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		clearRadioButton();		
		buttonView.setChecked(true);
		switch(buttonView.getId()){
		
		case R.id.stroke_width_5:
			result_width=5;
			break;
		case R.id.stroke_width_10:
			result_width=10;
			break;
		case R.id.stroke_width_20:
			result_width=20;
			break;
		case R.id.stroke_width_40:
			result_width=40;
			break;
		}
		
	}
	public void clearRadioButton(){
		stroke_width5.setChecked(false);
		stroke_width10.setChecked(false);
		stroke_width20.setChecked(false);
		stroke_width40.setChecked(false);
	}

}
