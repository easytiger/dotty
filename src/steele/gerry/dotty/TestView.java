/**
 * 
 */
package steele.gerry.dotty;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author gerry
 *
 */
public class TestView extends View {

	public class MPoint {
		
		private float x;
		private float y;
		private int id;
		private int pressWeight;
		private int pCol;
		
		public int getpCol() {
			return pCol;
		}

		public int getPressWeight() {
			return pressWeight;
		}

		public void setPressWeight(int pressWeight) {
			this.pressWeight = pressWeight;
		}
		
		public void setPressWeightFromNative(float pressWeight) {
			this.pressWeight = (int)pressWeight*670;
		}

		public MPoint(float x, float y) {
			this.x = x;
			this.y = y;
		}
		
		public MPoint(float x, float y, int id) {
			this.x = x;
			this.y = y;
			this.id = id;
			//Random rGen = new Random();
			this.pCol = Color.rgb(rGen.nextInt(254), rGen.nextInt(254), rGen.nextInt(254));
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public float getX() {
			return x;
		}
		public void setX(float x) {
			this.x = x;
		}
		public float getY() {
			return y;
		}
		public void setY(float y) {
			this.y = y;
		}

		@Override
		public String toString() {
			return "[id=" + id + ", x=" + x + ", y=" + y + "]";
		}
		
		
	}

	
	void touchRec(MotionEvent mEvt) {
		// iterate over all multi touch points and add them to the vector
		for (int i = 0; i < mEvt.getPointerCount(); i++){
			MPoint point = new MPoint(mEvt.getX(i), mEvt.getY(i), i);
			point.setPressWeightFromNative(mEvt.getPressure(i));
			mtPoints.add(point);
			
		}
		
		xMsg = "X: " + mEvt.getX();
		x = mEvt.getX();
		
		yMsg = "Y: " + mEvt.getY();
		y = mEvt.getY();
		
		pMsg = "P: " + mEvt.getPressure();
		p = mEvt.getPressure();
		
		tMsg = "#: " + mEvt.getPointerCount() + " pointers";
		this.invalidate();
		
	}
	
	private void drawAxis(Canvas canvas) {
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.LTGRAY);
		
		
		for(int x = 0; x < canvas.getHeight(); x++){
			if (x%100 ==0){
				canvas.drawLine(1, x, 11, x, p);
				p.setTextSize(8);
				canvas.drawText(Integer.toString(x), 14, x + 3, p);
			} else if (x%50 == 0) {
				canvas.drawLine(1, x, 4, x, p);
				p.setTextSize(5);
				canvas.drawText(Integer.toString(x), 8, x + 2, p);
			} else if (x % 10 == 0) {
				canvas.drawLine(1, x, 2, x, p);
			}
		}
		
		for(int x = 0; x < canvas.getWidth(); x++){
			if (x%100 ==0){
				canvas.drawLine(x, 1, x, 11, p);
				p.setTextSize(8);
				canvas.drawText(Integer.toString(x), x - 8, 20, p);
			} else if (x%50 == 0) {
				canvas.drawLine(x, 1, x, 4, p);
				p.setTextSize(5);
				canvas.drawText(Integer.toString(x), x - 5, 13, p);
			} else if (x%10 == 0){
				canvas.drawLine(x, 1, x, 2, p);
			}
		}
		
		canvas.drawLine(0, 0, 0, canvas.getHeight(), p);
		canvas.drawLine(0, 0, this.getWidth(), 0, p);
		
	}
	
	private void drawCrossHair(Canvas canvas, MPoint mPoint){
		Paint p = new Paint();
		Paint pDas = new Paint();
		float x = mPoint.getX();
		float y = mPoint.getY();
		
		p.setAntiAlias(true);
		
		pDas.setColor(Color.LTGRAY);
		DashPathEffect dashPath = new DashPathEffect(new float[]{5,3}, 1);
		pDas.setPathEffect(dashPath);
		
		p.setAlpha(150);
		canvas.drawLine(0, y, canvas.getWidth(), y , pDas);
		canvas.drawLine(x, 0, x, canvas.getHeight(), pDas);
		

		p.setStyle(Paint.Style.FILL);
		p.setColor(Color.RED);
		
		p.setColor(mPoint.getpCol());
		canvas.drawCircle(x , y, 30, p);
		
		p.setTextSize(10);
		p.setColor(Color.GREEN);
		canvas.drawText(mPoint.toString(), x - 70, y - 40, p);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		
		//make the canvas white
		paint.setColor(Color.BLACK);
		canvas.drawPaint(paint);
		
		paint.setColor(Color.WHITE);
		
		// draw some text using FILL style
		paint.setStyle(Paint.Style.FILL);
		//turn antialiasing on
		paint.setAntiAlias(true);
		paint.setTextSize(14);
		canvas.drawText(pMsg, getWidth() / 2,canvas.getHeight() - 10, paint);
		canvas.drawText(tMsg, getWidth() / 2,canvas.getHeight() - 10 - 14, paint);
		canvas.drawText(yMsg, 45,canvas.getHeight() - 10, paint);
		canvas.drawText(xMsg, 45,canvas.getHeight() - 10 - 14, paint);
		
		Iterator<MPoint> iPts = mtPoints.iterator();
		while (iPts.hasNext()){
			MPoint p = iPts.next();
			drawCrossHair(canvas, p );
		}
		drawAxis(canvas);
		mtPoints.clear();
	}
	
	/**
	 * @param context
	 */
	public TestView(Context context) {
		super(context);
		xMsg = "X:";
		yMsg = "Y:";
		pMsg = "P:";
		tMsg = "T:";
		
		mtPoints = new Vector<MPoint>();
		rGen = new Random();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public TestView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	String xMsg;
	String yMsg;
	String pMsg;
	String tMsg;
	float x;
	float y;
	float p;
	Random rGen;
	
	Vector<MPoint> mtPoints;
}
