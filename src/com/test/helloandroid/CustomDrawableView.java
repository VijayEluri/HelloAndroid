package com.test.helloandroid;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.MotionEvent;
import android.view.View;

public class CustomDrawableView extends View{

	public class NovaPoint
	{
		Point origin;
		int time;
		int color;
		NovaPoint(Point start, int color)
		{
			this.color = color;
			this.origin = new Point(start);
			this.time = 0;
		}
	}
	
	class RedrawCanvas extends TimerTask
	{
		int i =0 ;
		
		View parent;
		RedrawCanvas(View par)
		{
			parent = par;
		}
		@Override
		public void run() {
			parent.postInvalidate();
		}
		
	}
	
    private ShapeDrawable mDrawable;
    
    public Timer caller;
    
    public int circ_x;
    public int circ_y;
    public Bitmap myBitmap;
    public Bitmap[][] bitmaps;
//    public Canvas[] canvases;
    public Vector<NovaPoint> novas;
    
    public Paint[] paints;
    
    public float[] matrix = {1.0f,		0,		0,		0,		0,
    	                     0,			1,		0,		0,		0,
    	                     0,			0,		1,		0,		0,
    	                     0,			0,		0,		1.0f,	0};
    
    
    public CustomDrawableView(Context context) {
        super(context);

        int x = 10;
        int y = 10;
        int width = 300;
        int height = 50;
        Canvas c = new Canvas();
        
        caller = new Timer();
        caller.scheduleAtFixedRate(new RedrawCanvas(this), 0, 20);
        
        //Find number of taps in the code, and then rotate through
        //color space
        
        //Prerender Redimages
        novas = new Vector<NovaPoint>();
		myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
		paints = new Paint[30];
		bitmaps = new Bitmap[2][30];
		for(int i= 0; i < 30;i++)
		{
			paints[i] = new Paint();
			bitmaps[0][i] = Bitmap.createBitmap(myBitmap.getWidth(),myBitmap.getHeight(),
												 Bitmap.Config.ARGB_8888);
			c.setBitmap(bitmaps[0][i]);
			paints[i].setColorFilter(new ColorMatrixColorFilter(matrix));
			matrix[18] *= .95;
			c.drawBitmap(myBitmap, 0,0, paints[i]);
			paints[i] = new Paint();
		}
		
		//Prerender Blue Images
		matrix[0] = .8f;
		matrix[18] = 1; //Reset intensity
		matrix[1] = .2f;
		matrix[6] = .8f;
		matrix[13] = 1.0f;
		for(int i= 0; i < 30;i++)
		{
			paints[i] = new Paint();
			bitmaps[1][i] = Bitmap.createBitmap(myBitmap.getWidth(),myBitmap.getHeight(),
												 Bitmap.Config.ARGB_8888);
			c.setBitmap(bitmaps[1][i]);
			paints[i].setColorFilter(new ColorMatrixColorFilter(matrix));
			matrix[18] *= .95; //Reduce intensity
			c.drawBitmap(myBitmap, 0,0, paints[i]);
			paints[i] = new Paint();
		}
		
		//("res/drawable-hdpi/icon.png");
        //new BitmapDrawable("res/drawable-/icon.png");
        
        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.getPaint().setColor(0xff74AC23);
        mDrawable.setBounds(x, y, x + width, y + height);
    }
    

    protected void onDraw(Canvas canvas) {
       // mDrawable.draw(canvas);
  //  	Paint p = paints[0];
    	//p.setColor(0xff74AC23);
        //canvas.drawCircle(circ_x, circ_y, 100, p);
    	//canvas.drawBitmap(myBitmap, circ_x, circ_y, p);
    	Vector<Integer> terminate = new Vector<Integer>();
    	for(int i = 0; i < novas.size();i++)
    	{
    		NovaPoint nova = novas.get(i);
    		//p.setColorFilter(new ColorMatrixColorFilter(matrix));
    		for(int burst = 0; burst < 6; burst++)
    		{
    			canvas.drawBitmap(bitmaps[nova.color][nova.time],
    					(int)(Math.cos((2*Math.PI)*burst/6.)*nova.time*3+nova.origin.x),
    					(int)(Math.sin((2*Math.PI)*burst/6.)*nova.time*3+nova.origin.y),
    					paints[0]);
    			
    		}
    		nova.time+=1;
			if(nova.time >= 30)
			{
				terminate.add(i);
			}
    	}
    	for(int i = 0; i < terminate.size();i++)
    	{
    		novas.remove(terminate.get(i)-i);
    	}
//    	canvas.draw, circ_x, circ_y, p)
    }

    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
    	int numPointers = event.getPointerCount();
    	for(int i = 0; i < numPointers; i++)
    	{
    		novas.add(new NovaPoint(new Point(
    					           (int)event.getX(i),
    							   (int)event.getY(i)),i));
    	}//circ_x = (int)event.getX();
		//circ_y = (int)event.getY();
		
		//invalidate();
		return true;
	}
    
//	@Override
	//public boolean
}
