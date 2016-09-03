package com.cyl.simpleprogressbar;


import com.example.simpleprogressbar.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

public class ColorProgressBar extends ProgressBar {

	private Paint mPaint;

	private int mTextHeight = sp2px(15);

	private int mFinishHeight = dp2px(2);

	private int mUnFinishHeight = mFinishHeight;

	private int mOffSetWidth = dp2px(3);

	private int mFinishColor = Color.parseColor("#ffff0000");

	private int mTextColor = Color.parseColor("#ffff0000");

	private int mUnFinishColor = Color.parseColor("#ffff0000");
	
	private boolean isHorizontalStyle=true;
	
	private int mRadius=dp2px(25);
	
	private int mMaxPaintWidth=mUnFinishHeight*2;
	
	public ColorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		obtain(attrs);
		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(mTextHeight);
		if(isHorizontalStyle){
			setHorizontalPaint();
		}else{
			setCircularPaint();
			mFinishHeight=mUnFinishHeight*2;
		}
	}

	private void setCircularPaint() {
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeCap(Cap.ROUND);
	}

	private void setHorizontalPaint() {
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));
	}

	private void obtain(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.progress);
		
		isHorizontalStyle = a.getBoolean(R.styleable.progress_isHorizontalStyle, isHorizontalStyle);

		mFinishColor = a.getColor(R.styleable.progress_finishColor, mFinishColor);
		mTextColor = a.getColor(R.styleable.progress_textColor, mTextColor);
		mUnFinishColor = a.getColor(R.styleable.progress_unFinishColor, mUnFinishColor);

		mFinishHeight = (int) a.getDimension(R.styleable.progress_finishHeight, mFinishHeight);
		mUnFinishHeight = (int) a.getDimension(R.styleable.progress_unFinishHeight, mUnFinishHeight);
		mTextHeight = (int) a.getDimension(R.styleable.progress_testHeight, mTextHeight);
		mTextHeight=sp2px(mTextHeight);
		
		mRadius = (int) a.getDimension(R.styleable.progress_radius, mRadius);
		
		a.recycle();
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public int sp2px(int sp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
	}

	public int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

	public ColorProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColorProgressBar(Context context) {
		this(context, null);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		canvas.save();
		if(isHorizontalStyle){
			drawHorizontal(canvas);
		}else{
			drawCircular(canvas);
		}
		canvas.restore();
	}

	private void drawCircular(Canvas canvas) {
		canvas.translate(getPaddingLeft()+mFinishHeight/2, getPaddingTop()+mFinishHeight / 2);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(mUnFinishHeight);
		mPaint.setColor(mUnFinishColor);
		canvas.drawCircle(mRadius,mRadius, mRadius, mPaint);
		float radio = getProgress() * 1.0f / getMax()*360;
		mPaint.setStrokeWidth(mFinishHeight);
		mPaint.setColor(mFinishColor);
		canvas.drawArc(new RectF(0, 0, mRadius*2,mRadius*2), 0, radio, false, mPaint);
		mPaint.setColor(mTextColor);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextSize(mTextHeight);
		String proGress = getProgress() + "%";
		float textWidth = mPaint.measureText(proGress);
		float textHeight=(mPaint.ascent()-mPaint.descent())/2;
		canvas.drawText(proGress, mRadius-textWidth/2, mRadius-textHeight/2, mPaint);
		
	}

	private void drawHorizontal(Canvas canvas) {
		canvas.translate(getPaddingLeft(), getHeight() / 2);
		float radio = getProgress() * 1.0f / getMax();

		float mFinishLine = getWidth() * radio;

		String proGress = getProgress() + "%";

		float mTextWidth = mPaint.measureText(proGress);

		float mMaxLineLength = getWidth() - mTextWidth - mOffSetWidth - getPaddingLeft() - getPaddingRight();

		if (mFinishLine > mMaxLineLength) {
			mFinishLine = mMaxLineLength;
		}

		float mTextPosition = mFinishLine + mOffSetWidth;

		float mUnFinishLine = mTextPosition + mOffSetWidth + mTextWidth;

		mPaint.setStrokeWidth(mFinishHeight);
		mPaint.setColor(mFinishColor);
		canvas.drawLine(0, 0, mFinishLine, 0, mPaint);

		mPaint.setColor(mTextColor);
		int mY = (int) (-(mPaint.ascent() + mPaint.descent()) / 2);
		canvas.drawText(proGress, mTextPosition, mY, mPaint);

		if (mTextPosition + mOffSetWidth < getWidth()) {
			mPaint.setColor(mUnFinishColor);
			canvas.drawLine(mUnFinishLine, 0, getWidth(), 0, mPaint);
		}

	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(isHorizontalStyle){
			horizontalStyle(widthMeasureSpec, heightMeasureSpec);
		}else{
			circularStyle(widthMeasureSpec,heightMeasureSpec);
		}
	}

	private void circularStyle(int widthMeasureSpec, int heightMeasureSpec) {
		mFinishHeight=Math.max(mUnFinishHeight, mMaxPaintWidth);
		int expect=mRadius*2+mFinishHeight+getPaddingLeft()+getPaddingRight();
		int width = resolveSize(expect, widthMeasureSpec);
		int height = resolveSize(expect, heightMeasureSpec);
		int finalWidth=Math.min(width, height);
		mRadius=(finalWidth-getPaddingRight()-getPaddingLeft()-mFinishHeight)/2;
		setMeasuredDimension(finalWidth, finalWidth);
	}

	private void horizontalStyle(int widthMeasureSpec, int heightMeasureSpec) {
		int mViewWidth = getViewWidth(widthMeasureSpec);
		int mViewHeight = getViewHeight(heightMeasureSpec);
		setMeasuredDimension(mViewWidth, mViewHeight);
	}

	private int getViewHeight(int heightMeasureSpec) {
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int height = 0;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = MeasureSpec.getSize(heightMeasureSpec) + getPaddingTop() + getPaddingBottom();
		} else {
			height = mTextHeight;
		}
		return height;
	}

	private int getViewWidth(int widthMeasureSpec) {
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int width = 0;
		if (mode == MeasureSpec.EXACTLY) {
			width = MeasureSpec.getSize(widthMeasureSpec) + getPaddingLeft() + getPaddingRight();
		} else {
			throw new IllegalArgumentException("You Need Set ProGress width");
		}
		return width;
	}

}
