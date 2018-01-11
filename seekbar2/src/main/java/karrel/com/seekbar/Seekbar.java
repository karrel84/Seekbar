package karrel.com.seekbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.karrel.mylibrary.RLog;

import karrel.com.seekbar.databinding.LayoutSeekbarBinding;

//import com.karrel.mylibrary.RLog;

/**
 * Created by Rell on 2018. 1. 11..
 */

public class Seekbar extends FrameLayout implements GestureDetector.OnGestureListener {

    public interface OnSeekbarListener {

        void onChangedTick(int tick);
    }

    enum ThumbStatus {
        normal, selected, press
    }

    private LayoutSeekbarBinding mBinding;
    private GestureDetector mGestureDetector; // 제스쳐 디택터
    private float mThumbLeft; // thumb의 x 좌표
    private int mLineHeight = 6; // 백 그라운드 라인의 높이
    private Paint mPaint; // 라인의 페인트
    private Paint mBackLinePaint; // 백그라운드 라인의 페인트
    private Rect mLineRact = new Rect(); // 라인 랙트
    private Rect mBackLineRact = new Rect(); // 백그라운드 라인 랙트
    private int mLineColor = Color.parseColor("#51459e"); // 라인색상
    private int mBackLineColor = Color.parseColor("#b1b2b5"); // 백그라운드 라인색상
    private int[] mTickLoc; // 틱 좌표
    private int mTick = 5; // 시크바에 들어가는 포지션수
    private int mCurrentTick = 0;
    private int mTickGap; // 포지션과 다음포지션과의 간격
    private OnSeekbarListener mOnSeekbarListener; // thumb 의 이동에 따른 리스너
    private boolean mIsAnim = true;// 애니메이션


    public void setOnSeekbarListener(OnSeekbarListener onSeekbarListener) {
        this.mOnSeekbarListener = onSeekbarListener;
    }

    public Seekbar(Context context) {
        super(context);
        init();
    }

    public Seekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Seekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        RLog.d();
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_seekbar, this, true);
        setWillNotDraw(false);
        mGestureDetector = new GestureDetector(this);
        setupPaint();
        setupBackLineRect();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);

                mTickLoc = new int[mTick];
                mTickGap = (getWidth() - mBinding.thumb.getWidth()) / (mTick - 1);
                for (int i = 0; i < mTickLoc.length; i++) {
                    mTickLoc[i] = i * mTickGap + (mBinding.thumb.getWidth() / 2);

                    RLog.d(mTickLoc[i] + "");
                }

                // init position
                mThumbLeft = mTickLoc[0] - (mBinding.thumb.getWidth() / 2);
                setThumbX(mIsAnim);

                onChangeStatus(ThumbStatus.normal);
            }
        });
    }

    private void setupBackLineRect() {
        int left = 0;
        int top = getHeight() / 2 - (mLineHeight / 2);
        int right = getWidth();
        int bottom = top + mLineHeight;
        mBackLineRact.set(left, top, right, bottom);
    }

    private void setupPaint() {
        mPaint = new Paint();
        mPaint.setColor(mLineColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mBackLinePaint = new Paint();
        mBackLinePaint.setColor(mBackLineColor);
        mBackLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // up 이벤트 발생시 thumb 를 맞는 위치로 옮겨야함.
        if (event.getAction() == MotionEvent.ACTION_UP) {
//            RLog.d("up!!");
            // 적당한 위치로 thumb를 이동
            // 5구간이라는 가정

            // 현재 thumb의 위치가 어느 구간이랑 가까운가? 계산
            Pair<Integer, Integer> pair = calPosition((int) event.getRawX());
            int position = pair.second;
            if (position == 0) position += mTickGap;
            if (position == getWidth()) position -= mTickGap;
            mThumbLeft = position - (mBinding.thumb.getWidth() / 2);
            setThumbX(mIsAnim);

            // tick 계산
            calTick(event);

            onChangeStatus(ThumbStatus.selected);

            return true;
        }

        return mGestureDetector.onTouchEvent(event);
    }

    private void calTick(MotionEvent event) {
        // 현재 thumb의 위치가 어느 구간이랑 가까운가? 계산
        Pair<Integer, Integer> pair = calPosition((int) event.getRawX());

        int tick = pair.first;

        if (tick != mCurrentTick) {
            // event 발생
//            RLog.e("currentTick : " + tick);
            if (mOnSeekbarListener != null) {
                mOnSeekbarListener.onChangedTick(tick);
            }
        }

        mCurrentTick = tick;

        // text 변경
        onChangText();
    }

    // thumb의 상태 변경 select, normal, press
    private void onChangeStatus(ThumbStatus status) {
        switch (status) {
            case normal: // 평소상태
                mBinding.thumb.setSelected(false);
                mBinding.thumb.setSelected(false);
                break;
            case selected: // 선택됨
                mBinding.thumb.setSelected(true);
                mBinding.thumb.setPressed(false);
                break;
            case press: // 눌리고있는중
                mBinding.thumb.setSelected(false);
                mBinding.thumb.setPressed(true);
                break;
        }

    }

    // thum의 텍스트 변경
    private void onChangText() {
        mBinding.thumb.setText(mCurrentTick + "");
    }

    private void setThumbX(boolean isAnim) {
        if (isAnim) {
            mBinding.thumb.animate()
                    .translationX(mThumbLeft)
                    .start();
        } else {
            mBinding.thumb.setX(mThumbLeft);
        }

        invalidate();
    }

    // 현재 thumb의 위치가 어느 구간이랑 가까운가 계산
    private Pair<Integer, Integer> calPosition(int point) {
        int tick;
//        RLog.d("point : " + point);

        for (int i = mTickLoc.length - 1; i >= 0; i--) {
            tick = i;
//            RLog.d("mTickLoc[i] : " + mTickLoc[i]);

            if (mTickLoc[i] < point) {
                int value;
                if (mTickLoc[i] + (mTickGap / 2) < point) {
                    tick++;
                    value = mTickLoc[i] + mTickGap;
                } else {
                    value = mTickLoc[i];
                }

                if (tick <= 0) tick = 0;
                if (mTick <= tick) tick = mTick - 1;

//                RLog.d("value : " + value);
                return Pair.create(tick, value);
            }

        }
        return Pair.create(0, 0);
    }

    @Override
    public boolean onDown(MotionEvent e) {
//        RLog.d();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
//        RLog.d();

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        RLog.d();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        // thumb 의 위치를 이동시켜준다.
        mThumbLeft = moveThumb(e2);
        setThumbX(false);
        // tick 계산
        calTick(e2);
//        RLog.d();
        onChangeStatus(ThumbStatus.press);
        return true;
    }

    // thumb 의 위치를 이동시켜준다.
    private float moveThumb(MotionEvent e2) {
        float offset = mBinding.thumb.getWidth() / 2;
        // thumb 의 중심을 기준으로 이동시켜준다.
        // thumb가 화면밖으로 넘어가면안된다.
        float left = e2.getRawX() - offset;
        float right = left + mBinding.thumb.getWidth();

        // 좌측이 0보다 작으면 옵셋처리
        if (left < 0) left = 0;
        // 우측이 화면을 넘어가지 않게 처리
        if (right > getWidth()) left = getWidth() - mBinding.thumb.getWidth();
        return left;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        // draw back line
        int left = mTickLoc[0];
        int top = getHeight() / 2 - (mLineHeight / 2);
        int right = mTickLoc[mTickLoc.length - 1];
        int bottom = top + mLineHeight;
        mBackLineRact.set(left, top, right, bottom);
        canvas.drawRect(mBackLineRact, mBackLinePaint);

        // draw line
        left = mTickLoc[0];
        top = getHeight() / 2 - (mLineHeight / 2);
        right = (int) (mThumbLeft + mBinding.thumb.getWidth() / 2);
        bottom = top + mLineHeight;
        mLineRact.set(left, top, right, bottom);
        canvas.drawRect(mLineRact, mPaint);

    }

    @Override
    public void onLongPress(MotionEvent e) {
//        RLog.d();

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        RLog.d();
        return false;
    }

    public void setThumbBackground(@DrawableRes int resBackground) {
        mBinding.thumb.setBackgroundResource(resBackground);
    }

    public void setThumbTextColor(ColorStateList colors) {

        mBinding.thumb.setTextColor(colors);
    }

    public void setThumbTextColor(int color) {
        mBinding.thumb.setTextColor(color);
    }

}
