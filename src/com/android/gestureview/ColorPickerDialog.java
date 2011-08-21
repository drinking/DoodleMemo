/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.gestureview;

import com.android.drawmemo.R;

import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerDialog extends Dialog {

    public interface OnPaintChangedListener {
        void paintChanged(Paint paint);
    }

    private OnPaintChangedListener mListener;
    private int mInitialColor;
    private Paint mInitialPaint=null;
    private static class ColorPickerView extends View {
        private Paint mPaint;
        private Paint mCenterPaint;
        private final int[] mColors;
        private float previousx;
        private Rect picrect=new Rect(); 
        private OnPaintChangedListener mListener;
        Bitmap paintguide=BitmapFactory.decodeResource(getResources(), R.drawable.paintguide);
        ColorPickerView(Context c, OnPaintChangedListener l, int color) {
            super(c);
            mListener = l;
            mColors = new int[] {
                0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
                0xFFFFFF00, 0xFFFF0000,
            };//ԭʼ���캯��
                  
            Shader s = new SweepGradient(0, 0, mColors, null);//������ɫ�ݶ�
            
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(s);
            mPaint.setStyle(Paint.Style.STROKE);//����Բ��ģʽ
            mPaint.setStrokeWidth(30);//���Ŀ��
            
            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            //bit mask for the flag enabling antialiasing(�����)
            mCenterPaint.setColor(color);
            mCenterPaint.setStrokeWidth(5);//�м�Բ��ɫ�ܲ����ķ�Χ
            
        }//ԭʼ���캯��
        ColorPickerView(Context c, OnPaintChangedListener l, Paint inpaint) {
            super(c);
            mListener = l;
            mColors = new int[] {
            		 0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
                     0xFFFFFF00,0xFF000000,0xFFFFFFFF,0xFFFF0000,
            };
                    
            Shader s = new SweepGradient(0, 0, mColors, null);//������ɫ�ݶ�
            
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(s);
            mPaint.setStyle(Paint.Style.STROKE);//����Բ��ģʽ
            mPaint.setStrokeWidth(30);//���Ŀ��
            
            mCenterPaint=new Paint(inpaint);
            
        }
        
        private boolean mTrackingCenter;
        private boolean mHighlightCenter;

        @Override 
        protected void onDraw(Canvas canvas) {
        	//CENTER_X=this.getWidth()/2;
            float r = CENTER_X - mPaint.getStrokeWidth()*0.5f;
            //���Ǻ���⣬һ������Ǵӻ���������}�߻�����������
            //canvas.translate(CENTER_X, CENTER_X);//�ı����ϵ
            canvas.translate(this.getWidth()/2, CENTER_X);
            //canvas.drawOval(new RectF(-r, -r, r, r), mPaint); //��ԭ��о���������⣬��Ч����һ���
            canvas.drawOval(new RectF(-r,r,r,-r), mPaint);
            canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);
            
            if (mTrackingCenter) {//���Բ���Ķ�̬Ч��
                int c = mCenterPaint.getColor();
                mCenterPaint.setStyle(Paint.Style.STROKE);
                
                if (mHighlightCenter) {
                    mCenterPaint.setAlpha(0xFF);//��Բ�ϵ���͸���
                } else {
                    mCenterPaint.setAlpha(0x80);//����Բ��
                }
                canvas.drawCircle(0, 0,
                                  CENTER_RADIUS + mCenterPaint.getStrokeWidth(),
                                  mCenterPaint);
                
                mCenterPaint.setStyle(Paint.Style.FILL);//�ָ�ȫ���ģʽ
                mCenterPaint.setColor(c);
                
            }
            //���ʴ�ϸ��������
            canvas.translate(-this.getWidth()/2, -CENTER_X);
            canvas.drawLine(0, 240, this.getWidth(), 240, mCenterPaint);
            picrect.left=0;
            picrect.right=this.getWidth();
            picrect.top=250;
            picrect.bottom=400;
            canvas.drawBitmap(paintguide, null, picrect,mCenterPaint);
            
            //canvas.translate(CENTER_X, CENTER_X);
        }
        
     //   @Override
     //   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
     //       //setMeasuredDimension(CENTER_X*2, CENTER_Y*2);//����view��С������)ɢ
      //  }
        
        //private static final int CENTER_X = 100;
        //private static final int CENTER_Y = 100;
        public  static int CENTER_X = 100;
        public  static int CENTER_Y = 100;
        private static final int CENTER_RADIUS = 32;
        //��ʼ���λ�ƣ�������ɫԲ�뾶�趨
        private int floatToByte(float x) {
            int n = java.lang.Math.round(x);//(int) Math.floor(f+0.5). 

            return n;
        }
        private int pinToByte(int n) {
            if (n < 0) {
                n = 0;
            } else if (n > 255) {
                n = 255;
            }
            return n;
        }
        
        private int ave(int s, int d, float p) {
            return s + java.lang.Math.round(p * (d - s));
        }
        
        private int interpColor(int colors[], float unit) {
            if (unit <= 0) {
                return colors[0];
            }
            if (unit >= 1) {
                return colors[colors.length - 1];
            }
            
            float p = unit * (colors.length - 1);
            int i = (int)p;
            p -= i;

            // now p is just the fractional part [0...1) and i is the index
            int c0 = colors[i];
            int c1 = colors[i+1];
            int a = ave(Color.alpha(c0), Color.alpha(c1), p);
            int r = ave(Color.red(c0), Color.red(c1), p);
            int g = ave(Color.green(c0), Color.green(c1), p);
            int b = ave(Color.blue(c0), Color.blue(c1), p);
            
            return Color.argb(a, r, g, b);
        }
        
        private int rotateColor(int color, float rad) {
            float deg = rad * 180 / 3.1415927f;
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            
            ColorMatrix cm = new ColorMatrix();
            ColorMatrix tmp = new ColorMatrix();

            cm.setRGB2YUV();
            tmp.setRotate(0, deg);
            cm.postConcat(tmp);
            tmp.setYUV2RGB();
            cm.postConcat(tmp);
            
            final float[] a = cm.getArray();

            int ir = floatToByte(a[0] * r +  a[1] * g +  a[2] * b);
            int ig = floatToByte(a[5] * r +  a[6] * g +  a[7] * b);
            int ib = floatToByte(a[10] * r + a[11] * g + a[12] * b);
            
            return Color.argb(Color.alpha(color), pinToByte(ir),
                              pinToByte(ig), pinToByte(ib));
        }
        
        private static final float PI = 3.1415926f;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX() - this.getWidth()/2;//�ı����ĵ�λ�ã�ԭֵΪCENTER_X
            float y = event.getY() - CENTER_Y;
            boolean inCenter = java.lang.Math.sqrt(x*x + y*y) <= CENTER_RADIUS;
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	previousx=event.getX();
                    mTrackingCenter = inCenter;
                    if (inCenter) {
                        mHighlightCenter = true;
                        invalidate();
                        break;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (mTrackingCenter) {
                        if (mHighlightCenter != inCenter) {
                            mHighlightCenter = inCenter;
                            invalidate();
                        }
                    } else if(event.getY()<2*CENTER_X){
                        float angle = (float)java.lang.Math.atan2(y, x);
                        // need to turn angle [-PI ... PI] into unit [0....1]
                        float unit = angle/(2*PI);
                        if (unit < 0) {
                            unit += 1;
                        }
                        mCenterPaint.setColor(interpColor(mColors, unit));
                        invalidate();
                    }
                    else
                    {
                    	//mCenterPaint.setStrokeWidth(event.getX()/10);
                    	if(event.getX()-previousx>10)
                    	{
                    		mCenterPaint.setStrokeWidth(mCenterPaint.getStrokeWidth()+1);
                    		previousx=event.getX();
                    	}
                    	else if(event.getX()-previousx<-10)
                    	{
                    		mCenterPaint.setStrokeWidth(mCenterPaint.getStrokeWidth()-1);
                    		previousx=event.getX();
                    	}
                    	invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mTrackingCenter) {
                        if (inCenter) {
                        	
                            mListener.paintChanged(mCenterPaint);
                        }
                        mTrackingCenter = false;    // so we draw w/o halo
                        invalidate();
                    }
                    break;
            }
            return true;
        }
    }

    public ColorPickerDialog(Context context,
                             OnPaintChangedListener listener,
                             int initialColor) {
        super(context);
        
        mListener = listener;
        mInitialColor = initialColor;
    }
    public ColorPickerDialog(Context context,
            OnPaintChangedListener listener,
            Paint paint) {
    	super(context);

    	mListener = listener;
    	mInitialPaint=new Paint(paint);
    	
    	
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnPaintChangedListener l = new OnPaintChangedListener() {
            public void paintChanged(Paint paint) {
                mListener.paintChanged(paint);
                dismiss();
            }
        };

        setContentView(new ColorPickerView(getContext(), l, mInitialPaint));
        setTitle("画笔颜色选取，按圆心保存画笔");
    }
}
