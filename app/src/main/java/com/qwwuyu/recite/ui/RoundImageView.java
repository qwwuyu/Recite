package com.qwwuyu.recite.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends ImageView {
	// （1）声明应用环境
	private Context mContext;
	// （1）声明圆环厚度
	private int mBorderThickness = 0;
	// （1）声明内外边框颜色
	private int mBorderInsideColor = 0;
	private int mBorderOutsideColor = 0;
	// （1）声明并初始化圆形的默认长宽
	private int mBorderWidth = 0;
	private int mBorderHeight = 0;
	// （1）声明默认颜色值是黑色
	private int defaultColor = 0xFFFFFFFF;

	public RoundImageView(Context context) {
		super(context);
		mContext = context;
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;

	}

	/*
	 * (2)设置这个圆环的厚度，以及内边框和外边框的颜色； 由于android 没有这个圆环，当然我们得自定义它的属性
	 * 所以在res/values/attrs.xml 文件里自定义边框厚度和 内外边框的颜色，然后在下面这个函数里去设定具体的值
	 */
	public void setRoundAttribute(int insideColor,int outsideColor,int borderWidth) {
		/*
		 * TypedArray 顾名思义，被定义了类型的数组，因此它就是 一个用来装载自定义的属性值的容器，被
		 * obtainStyledAttributes(AttributeSet, int[], int, int)
		 * obtainAttributes(AttributeSet, int[]).所调用；google
		 * 说，使用结束后记得调用recycle()来释放它
		 */
		mBorderInsideColor = insideColor;
		mBorderOutsideColor = outsideColor;
		mBorderThickness = borderWidth;
	}

	/*
	 * (4)绘制圆形 由于Google在ImageView中已经有了onDraw方法是专门用来绘制图形的，
	 * 所以这里我们不必自己新建方法，而直接重写父类ImageView的onDraw
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		/*
		 * 声明一个可画的对象，并从ImageView对象中 获取一个具体的对象（没有即为null），即实例化
		 */
		Drawable drawable = getDrawable();
		// 如果我们要画的图片为空，那么就没有继续画圆环的必要了，返回
		if (drawable == null) {
			return;
		}

		// 如果我们要画的图片的宽度或高度不符合要求，也没有画的必要
		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		/*
		 * 同时应该注意到9Patch的图片也是有边框的，那么我们要加边框的 图片的应用类是继承了 NinePathcDrawable
		 * 的话，那么人家本身 有边框，我们也就没有加圆形边框的必要了，这个是个人理解； 不晓得对否
		 */
		if (drawable.getClass() == NinePatchDrawable.class) {
			return;
		}
		/*
		 * 参考Google 官方文档，Android 画一个view 要经历两个过程 第一个是计算measure ,即计算这个view
		 * 所需要多大的尺寸空间 第二个是布局layout ,即要根据开发者的要求来放在哪个位置 所以，这里我们要先测量
		 */

		this.measure(0, 0);
		// 获得该图片，转换成Bitmap 格式，好处理
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		// 要知道图片大小,才能计算出接下来所画圆形的半径
		if (mBorderWidth == 0) {
			mBorderWidth = getWidth();
		}

		if (mBorderHeight == 0) {
			mBorderHeight = getHeight();
		}
		// 半径
		int radius = 0;
		/*
		 * 这里要根据我们在layout中是否设置了内外边框颜色的需求而动手画圆 三种情况：A:border_inside_color 和
		 * border_outside_color 什么都不设 B:设置了外边框 C:设置了内外边框，这时候就意味着要画个圆环了
		 */
		// 取宽度与高度中最大的值为参考，注意画外圆时加上圆环厚度
		if (mBorderInsideColor != defaultColor && mBorderOutsideColor != defaultColor) {
			radius = (mBorderWidth < mBorderHeight ? mBorderWidth : mBorderHeight) / 2 - 2 * mBorderThickness;
			// 画内圆
			drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
			// 画外圆
			drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2, mBorderOutsideColor);
		} else if (mBorderInsideColor != defaultColor && mBorderOutsideColor == defaultColor) {// 定义画一个边框
			radius = (mBorderWidth < mBorderHeight ? mBorderWidth : mBorderHeight) / 2 - mBorderThickness;
			drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
		} else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {// 定义画一个边框
			radius = (mBorderWidth < mBorderHeight ? mBorderWidth : mBorderHeight) / 2 - mBorderThickness;
			drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderOutsideColor);
		} else {// 没有边框
			radius = (mBorderWidth < mBorderHeight ? mBorderWidth : mBorderHeight) / 2;
		}

		// 画圆形图像
		Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius);
		canvas.drawBitmap(roundBitmap, mBorderWidth / 2 - radius, mBorderHeight / 2 - radius, null);
	}

	/**
	 * 根据颜色和半径画圆边框，中间是空的
	 */
	private void drawCircleBorder(Canvas canvas, int radius, int color) {
		// 新建画笔对象
		Paint paint = new Paint();
		/*
		 * 先来了解下画笔的属性方法 setAntiAlias: 设置画笔的锯齿效果，true则去掉锯齿； setColor: 设置画笔颜色；
		 * setARGB: 设置画笔的a,r,p,g值。 setAlpha: 设置Alpha值,即透明度 setTextSize: 设置字体尺寸
		 * setStyle: 设置画笔风格，空心或者实心； setStrokeWidth: 设置空心的边框宽度。 setFilterBitmap
		 * 如设为true，则图像在动画进行中会滤掉对Bitmap图像的优化操作，加快显示 setDither
		 * 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
		 */
		// 这里我们不要锯齿（难看），处理抖动，加快显示，空心，再设置颜色和边框宽度
		paint.setAntiAlias(true);
		paint.setColor(color);
		paint.setDither(true);
		paint.setFilterBitmap(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(mBorderThickness);
		canvas.drawCircle(mBorderWidth / 2, mBorderHeight / 2, radius, paint);
	}

	/**
	 * 获取裁剪后的圆形图片
	 * 
	 * @param radius
	 *            半径
	 */
	public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;

		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		int squareWidth = 0, squareHeight = 0;
		int x = 0, y = 0;
		Bitmap squareBitmap;
		if (bmpHeight > bmpWidth) {// 高大于宽
			squareWidth = squareHeight = bmpWidth;
			x = 0;
			y = (bmpHeight - bmpWidth) / 2;
			// 截取正方形图片
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);
		} else if (bmpHeight < bmpWidth) {// 宽大于高
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);
		} else {
			squareBitmap = bmp;
		}

		if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) {
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter, diameter, true);

		} else {
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2, scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		// bitmap回收(recycle导致在布局文件XML看不到效果)
		// bmp.recycle();
		// squareBitmap.recycle();
		// scaledSrcBmp.recycle();
		bmp = null;
		squareBitmap = null;
		scaledSrcBmp = null;
		return output;
	}

}
