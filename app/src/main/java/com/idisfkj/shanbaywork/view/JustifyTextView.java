package com.idisfkj.shanbaywork.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.idisfkj.shanbaywork.R;

import java.util.TreeMap;

/**
 * 自定义TextView实现左右对齐
 * Created by idisfkj on 16/10/28.
 * Email : idisfkj@qq.com.
 */
public class JustifyTextView extends TextView {

    private int mLineY;
    private int mViewWidth;
    private static int start = Integer.MAX_VALUE;
    private static int end = Integer.MAX_VALUE;
    private int textHeight;
    private Paint.FontMetrics fm;
    private static TreeMap<Integer, Integer> wordList = new TreeMap<>();
    private static TreeMap<Integer, Integer> temp = new TreeMap<>();


    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (wordList != null)
            temp = (TreeMap<Integer, Integer>) wordList.clone();
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mViewWidth = getMeasuredWidth();
        String text = getText().toString();
        mLineY = 0;
        mLineY += getTextSize();
        Layout layout = getLayout();

        if (layout == null) {
            return;
        }

//        Log.v("TAG", "onDraw");

        fm = paint.getFontMetrics();

        textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout
                .getSpacingAdd());

        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
//            Log.v("TAG", lineStart + " / " + lineEnd);
            float width = StaticLayout.getDesiredWidth(text, lineStart,
                    lineEnd, getPaint());
            String line = text.substring(lineStart, lineEnd);
            if (needScale(line)) {
                drawScaledText(canvas, lineStart, line, width);
            } else {
                drawNormalText(canvas, lineStart, line);
            }
            mLineY += textHeight;
        }
    }

    /**
     * 设置点击高亮
     *
     * @param start
     * @param end
     */
    public void setHightLightWord(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * 设置单词列表中的单词高亮
     *
     * @param wordList
     */
    public void setWordList(TreeMap<Integer, Integer> wordList) {
        this.wordList = wordList;
    }

    /**
     * 绘制缩放的文本
     *
     * @param canvas
     * @param lineStart
     * @param line
     * @param lineWidth
     */
    private void drawScaledText(Canvas canvas, int lineStart, String line,
                                float lineWidth) {
        float x = 0;
        int extral = 0;
        //首行缩进2位
        if (isFirstLineOfParagraph(line)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, getPaint());
            //获取当前字符串所占的宽度
            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
            x += bw;
            extral = 3;
            line = line.substring(3);
        }

        int gapCount = line.length() - 1;
        int i = 0;
        if (line.length() > 2 && line.charAt(0) == 12288
                && line.charAt(1) == 12288) {
            String substring = line.substring(0, 2);
//            Log.v("TAG", "aa:" + line.charAt(0) + " " + line.charAt(1) + " " + substring);
            float cw = StaticLayout.getDesiredWidth(substring, getPaint());
            canvas.drawText(substring, x, mLineY, getPaint());
            x += cw;
            i += 2;
        }

        //平分多余的空间
        float d = (mViewWidth - lineWidth) / gapCount;
        for (; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
//            Log.v("TAG", " " + i + "Start=" + start + "End=" + end);
            float cw = StaticLayout.getDesiredWidth(c, getPaint());

            if (!temp.isEmpty() && lineStart + i + extral == temp.get(temp.firstKey()))
                temp.remove(temp.firstKey());

            //绘制高亮
            if ((!temp.isEmpty() && lineStart + i + extral >= temp.firstKey() && lineStart + i + extral < temp.get(temp.firstKey()))
                    || (lineStart + i + extral >= start && lineStart + i + extral < end)) {
//                Log.v("TAG", " " + start + " " + end + " " + lineStart + " " + i + " " + extral);
                TextPaint paint = getPaint();
                //绘制高亮背景
                paint.setColor(getResources().getColor(R.color.colorPrimary));
                canvas.drawRect(x - 5, mLineY - textHeight + 15, x + cw + d + 5, mLineY + textHeight / 2 - 15, paint);
                //绘制高亮文字
                paint.setColor(Color.WHITE);
                canvas.drawText(c, x, mLineY, paint);
            } else {
                TextPaint paint = getPaint();
                paint.setColor(getResources().getColor(R.color.colorLightBlack));
                canvas.drawText(c, x, mLineY, paint);
            }
            x += cw + d;
        }
    }

    /**
     * 绘制正常显示的文本
     *
     * @param canvas
     * @param lineStart
     * @param line
     */
    private void drawNormalText(Canvas canvas, int lineStart, String line) {
        float x = 0;
        for (int i = 0; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
//            Log.v("TAG", " " + i + "Start=" + start + "End=" + end);
            if (!temp.isEmpty() && lineStart + i == temp.get(temp.firstKey()))
                temp.remove(temp.firstKey());
            //绘制高亮
            if ((!temp.isEmpty() && lineStart + i >= temp.firstKey() && lineStart + i < temp.get(temp.firstKey()))
                    || (lineStart + i >= start && lineStart + i < end)) {
//                Log.v("TAG", " " + start + " " + end + " " + lineStart + " " + i);
                TextPaint paint = getPaint();
                //绘制高亮背景
                paint.setColor(getResources().getColor(R.color.colorPrimary));
                canvas.drawRect(x - 5, mLineY - textHeight + 15, x + cw + 5, mLineY + textHeight / 2 - 15, paint);
                //绘制高亮文字
                paint.setColor(Color.WHITE);
                canvas.drawText(c, x, mLineY, paint);
            } else {
                TextPaint paint = getPaint();
                paint.setColor(getResources().getColor(R.color.colorLightBlack));
                canvas.drawText(c, x, mLineY, paint);
            }
            x += cw;
        }
    }

    /**
     * 判断是否为段落开头
     *
     * @param line
     * @return
     */
    private boolean isFirstLineOfParagraph(String line) {
        return line.length() > 3 && line.charAt(0) == ' '
                && line.charAt(1) == ' ';
    }

    /**
     * 是否需要缩放
     *
     * @param line
     * @return
     */
    private boolean needScale(String line) {
        if (line == null || line.length() == 0) {
            return false;
        } else {
            //不为换行,则有多余空间
            return line.charAt(line.length() - 1) != '\n';
        }
    }

}
