package com.example.radarmapview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator on 2018/4/28 15:23
 * <p>
 * 邮箱：xjs250@163.com
 */
public class RadarMapView extends View {
    //字体的大小
    private float textSize=22;
    //线的颜色
    private int lineColor=Color.BLACK;
    //文本的颜色
    private int cotentColor=Color.BLACK;
    //纬度值的颜色
    private int valueColor=Color.GREEN;
    //数据个数
    private int count = 6;
    //网格最大半径
    private float radius;
    //中心X
    private int centerX;
    //中心Y
    private int centerY;
    //雷达区画笔
    private Paint mainPaint;
    //文本画笔
    private TextPaint textPaint;
    //分数画笔
    private TextPaint gardePaint;

    //数据区画笔
    private Paint valuePaint;
    //标题文字
    private List<String> titles;
    //各维度分值
    private List<Double> data;
    //数据最大值
    private float maxValue = 100;
    private float angle;

    private StaticLayout layout;
    /**
     * 表示是否是单层
     */
    private boolean isSing=false;
    /**
     * 获取多行显示文本的高度
     */
    private float dy;
    /**
     * 获取画分数的文本的高度
     */
    private float fontGradeHeight;
    //绘制多边形的path
    private Path path;
    //直线的path
    private Path Linespath;
    //分数的path
    private Path Regionpath;
    private int mWidth=400;
    private int mHeight=300;

    public RadarMapView(Context context) {
        this(context, null);
    }

    public RadarMapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.RadarMapView);
        lineColor=a.getColor(R.styleable.RadarMapView_lineColor,lineColor);
        cotentColor=a.getColor(R.styleable.RadarMapView_cotentColor,cotentColor);
        valueColor=a.getColor(R.styleable.RadarMapView_valueColor,valueColor);
        textSize=a.getDimensionPixelSize(R.styleable.RadarMapView_textSize,22);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpectMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpectSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpectMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpectSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpectMode == MeasureSpec.AT_MOST
                && heightSpectMode == MeasureSpec.AT_MOST) {
            mWidth+=getPaddingLeft()+getPaddingRight();
            mHeight+=getPaddingBottom()+getPaddingTop();
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpectMode == MeasureSpec.AT_MOST) {
            mWidth+=getPaddingLeft()+getPaddingRight();
            setMeasuredDimension(mWidth, heightSpectSize);
        } else if (heightSpectMode == MeasureSpec.AT_MOST) {
            mHeight+=getPaddingBottom()+getPaddingTop();
            setMeasuredDimension(widthSpectSize, mHeight);
        }
    }

    private void init() {

        mainPaint = new Paint();
        mainPaint.setColor(lineColor);
        mainPaint.setAntiAlias(true);
//        mainPaint.setAlpha(50);
        mainPaint.setStrokeWidth(1);
        mainPaint.setStyle(Paint.Style.STROKE);
        //文本
        textPaint = new TextPaint();
        textPaint.setColor(cotentColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);
        //分数
        gardePaint=new TextPaint();
        gardePaint.setColor(cotentColor);
        gardePaint.setTextAlign(Paint.Align.CENTER);
        gardePaint.setTextSize(textSize);
        gardePaint.setStrokeWidth(1);
        gardePaint.setAntiAlias(true);

        //维度值
        valuePaint=new Paint();
        valuePaint.setColor(valueColor);
        valuePaint.setAntiAlias(true);
//        valuePaint.setAlpha(50);
        valuePaint.setStyle(Paint.Style.FILL);

        titles = new ArrayList<>(count);
        titles.add("学科素养");
        titles.add("人际交往");
        titles.add("空间智能");
        titles.add("数学智能");
        titles.add("审美表现");
        titles.add("道德品质");

        data=new ArrayList<>();
        data.add(94.0);
        data.add(100.0);
        data.add(88.0);
        data.add(85.0);
        data.add(99.0);
        data.add(90.0);

        path = new Path();
        Linespath=new Path();
        Regionpath=new Path();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(w, h) / 2 * 0.6f;
        centerX = w / 2;
        centerY = h / 2;
        //一旦size发生改变，重新绘制
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolygon(canvas);
        drawLines(canvas);
        drawTitle(canvas);
        drawRegion(canvas);
    }

    /**
     * 绘制多边形
     *
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        //1度=1*PI/180   360度=2*PI   那么我们每旋转一次的角度为2*PI/内角个数
        //中心与相邻两个内角相连的夹角角度
        angle = (float) (2 * Math.PI / count);
        //每个蛛丝之间的间距
        float r = radius / (count - 1);
        //当前半径
        float curR = r * (count-1);
        path.reset();
        for (int i = 0; i < count; i++) {
            if(!isSing){
                curR = r * i;
                path.reset();
            }
            for (int j = 0; j < count; j++) {
                if (j == 0) {
                    path.moveTo(centerX + curR, centerY);
                } else {
                    //对于直角三角形sin(x)是对边比斜边，cos(x)是底边比斜边，tan(x)是对边比底边
                    //因此可以推导出:底边(x坐标)=斜边(半径)*cos(夹角角度)
                    //               对边(y坐标)=斜边(半径)*sin(夹角角度)
                    float x = (float) (centerX + curR * Math.cos(angle * j));
                    float y = (float) (centerY + curR * Math.sin(angle * j));
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制直线
     */
    private void drawLines(Canvas canvas) {
        Linespath = new Path();
        for (int i = 0; i < count; i++) {
            Linespath.reset();
            Linespath.moveTo(centerX, centerY);
            float x = (float) (centerX + radius * Math.cos(angle * i));
            float y = (float) (centerY + radius * Math.sin(angle * i));
            Linespath.lineTo(x, y);
            canvas.drawPath(Linespath, mainPaint);
        }
    }


    /**
     * 绘制标题文字
     *
     * @param canvas
     */
    private void drawTitle(Canvas canvas) {
        if (count != titles.size() && count!=data.size()) {
            return;
        }
       //计算分数的高度
        Paint.FontMetrics fontGradeMetrics = gardePaint.getFontMetrics();
        fontGradeHeight = fontGradeMetrics.descent - fontGradeMetrics.ascent;
        //计算文字的高度
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        //绘制文字时不让文字和雷达图形交叉,加大绘制半径
        float textRadius = radius + fontHeight;
        double pi = Math.PI;
        //预留出文字和分数的高度
        float offsetH=fontHeight+fontGradeHeight;
        for (int i = 0; i < count; i++) {
            float x = (float) (centerX + textRadius * Math.cos(angle * i));
            float y = (float) (centerY + textRadius * Math.sin(angle * i));
            //当前绘制标题所在顶点角度
            float degrees = angle * i;
            //从右下角开始顺时针画起,与真实坐标系相反
            String number=String.valueOf(data.get(i).doubleValue());
            String grade=number.substring(0,number.indexOf("."));
            //获取文字宽度
            float dis=textPaint.measureText(titles.get(i))/(titles.get(i).length()-1);

            if (degrees >= 0 && degrees <= pi / 2) {//第四象限 0-90
                newLine(canvas,titles.get(i),x+dis, y-fontHeight);

                canvas.drawText(grade ,x+dis,y+dy+fontGradeHeight,gardePaint);

            } else if (degrees >= 3*pi / 2 && degrees <= pi*2) {//第三象限 270-360
                newLine(canvas,titles.get(i),x+dis, y-offsetH);

                canvas.drawText(grade ,x+dis,y+dy,gardePaint);


            } else if (degrees > pi/2 && degrees <=  pi ) {//第二象限 90-180
                newLine(canvas,titles.get(i),x-dis, y-fontHeight);

                canvas.drawText(grade ,x-dis,y+dy+fontGradeHeight,gardePaint);


            } else if (degrees >= pi && degrees <= 3 * pi/2) {//第一象限 180-270
                newLine(canvas,titles.get(i),x-dis, y-offsetH);

                canvas.drawText(grade ,x-dis,y+dy,gardePaint);

            }
        }

    }

    /**
     * 多行绘制文本
     * @param canvas
     * @param text
     * @param x
     * @param y
     */
    private void newLine(@NonNull Canvas canvas, @NonNull String text, float x, float y){
        layout = new StaticLayout(text,textPaint,(int) DensityUtils.px2dp(getContext(),300), Layout.Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
        canvas.save();
        canvas.translate(x,y);
        layout.draw(canvas);
        canvas.restore();
        //获取文字的高度
        dy=layout.getHeight()-fontGradeHeight;
        layout=null;
    }
    /**
     * 绘制覆盖区域
     */
    private void drawRegion(Canvas canvas){
        valuePaint.setAlpha(80);
        for (int i = 0; i < count; i++) {
            //计算该数值与最大值比例
            Double perCenter = data.get(i)/maxValue;
            //小圆点所在位置距离圆心的距离
            double perRadius=perCenter*radius;
            float x = (float) (centerX + perRadius * Math.cos(angle * i));
            float y = (float) (centerY + perRadius * Math.sin(angle * i));
            if(i==0){
                Regionpath.moveTo(x,y);
            }else {
                Regionpath.lineTo(x,y);
            }
            //绘制小圆点
            canvas.drawCircle(x,y,2,valuePaint);
        }
        //闭合覆盖区域
        Regionpath.close();
        valuePaint.setStyle(Paint.Style.STROKE);
        //绘制覆盖区域外的连线
        canvas.drawPath(Regionpath, valuePaint);
        //填充覆盖区域
        valuePaint.setAlpha(80);
        valuePaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(Regionpath,valuePaint);
    }

    //设置数值种类
    public void setCount(int count) {
        this.count = count;
        postInvalidate();
    }

    //设置蜘蛛网颜色
    public void setMainPaint(Paint mainPaint) {
        this.mainPaint = mainPaint;
        postInvalidate();
    }

    //设置标题颜色
    public void setTextPaint(TextPaint textPaint) {
        this.textPaint = textPaint;
    }

    //设置标题
    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    /**
     * 设置分数
     * @param gardePaint
     */
    public void setGardePaint(TextPaint gardePaint) {
        this.gardePaint = gardePaint;
        postInvalidate();
    }

    //设置覆盖局域颜色
    public void setValuePaint(Paint valuePaint) {
        this.valuePaint = valuePaint;
        postInvalidate();
    }

    //设置数值
    public void setData(List<Double> data) {
        this.data = data;
        postInvalidate();
    }

    public List<Double> getData() {
        return data;
    }

    //设置最大数值
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }



}
