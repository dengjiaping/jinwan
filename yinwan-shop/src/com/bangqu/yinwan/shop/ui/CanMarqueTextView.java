package com.bangqu.yinwan.shop.ui;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class CanMarqueTextView extends TextView{

    public CanMarqueTextView(Context context) 
    {
        super(context,null);
        // TODO Auto-generated constructor stub
    }
    
    public CanMarqueTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
    }
    
    public CanMarqueTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
     }

    
    @Override
    public void onWindowFocusChanged(boolean focused)
    {
        if (focused)
        {
           super.onWindowFocusChanged(focused);
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect)
    {
        if (focused)
        {
           super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public boolean isFocused()
    {
       return true;
    }
 }
