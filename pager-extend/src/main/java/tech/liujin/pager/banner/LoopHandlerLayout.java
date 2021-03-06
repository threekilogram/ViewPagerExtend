package tech.liujin.pager.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import java.lang.ref.WeakReference;

/**
 * 该view是一个包装了{@link LoopHandler}的{@link FrameLayout}可以定时执行一些任务
 *
 * @author LiuJin
 * @date 2017-12-25
 */
public class LoopHandlerLayout extends FrameLayout {

      private static final String TAG = LoopHandlerLayout.class.getSimpleName();

      /**
       * help startLoop
       */
      protected LoopHandler    mLoopHandler;
      /**
       * true is looping
       */
      protected boolean        isAutoLoop;
      /**
       * startLoop time
       */
      protected int            mLoopTime;
      /**
       * 设置loop时行为,并且在合适的时候loop下一个
       */
      protected OnLoopListener mOnLoopListener;
      /**
       * flag for touch paused
       */
      protected boolean isTouchPaused = false;

      public LoopHandlerLayout ( @NonNull Context context ) {

            this( context, null );
      }

      public LoopHandlerLayout ( @NonNull Context context, @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public LoopHandlerLayout (
          @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
            init();
      }

      /**
       * init field
       */
      protected void init ( ) {

            mLoopHandler = new LoopHandler( this );
      }

      public void setOnLoopListener (
          OnLoopListener onLoopListener ) {

            mOnLoopListener = onLoopListener;
      }

      public OnLoopListener getOnLoopListener ( ) {

            return mOnLoopListener;
      }

      protected void setClip ( boolean clipOrNot ) {

            setClipChildren( clipOrNot );
            setClipToPadding( clipOrNot );
      }

      public void onTouchPauseLoop ( MotionEvent event ) {

            int action = event.getAction();
            if( action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_OUTSIDE ) {

                  if( isTouchPaused ) {
                        isTouchPaused = false;
                        if( isAutoLoop ) {
                              mLoopHandler.sendDelayedLoop( mLoopTime );
                        }
                  }
            } else {

                  if( !isTouchPaused ) {
                        isTouchPaused = true;
                        mLoopHandler.removeLoop();
                  }
            }
      }

      @Override
      protected void onDetachedFromWindow ( ) {

            super.onDetachedFromWindow();
            /* release */
            mLoopHandler.removeCallbacksAndMessages( null );
      }

      /**
       * true : 正在loop
       */
      public boolean isAutoLoop ( ) {

            return isAutoLoop;
      }

      /**
       * 开始loop
       */
      public void startLoop ( ) {

            startLoop( 2400 );
      }

      /**
       * 开始loop
       *
       * @param loopTime loop间隔
       */
      public void startLoop ( int loopTime ) {

            isAutoLoop = true;
            mLoopTime = loopTime;
            mLoopHandler.sendDelayedLoop( loopTime );
      }

      /**
       * 结束loop
       */
      public void stopLoop ( ) {

            isAutoLoop = false;
            mLoopHandler.removeLoop();
      }

      /**
       * 辅助发送延时消息
       */
      private static class LoopHandler extends Handler {

            private static final int WHAT_LOOP = 1569;

            /**
             * ref to view
             */
            private WeakReference<LoopHandlerLayout> mRef;

            LoopHandler ( LoopHandlerLayout banner ) {

                  mRef = new WeakReference<>( banner );
            }

            private void sendDelayedLoop ( int delayed ) {

                  if( isLooping() ) {
                        return;
                  }
                  sendEmptyMessageDelayed( WHAT_LOOP, delayed );
            }

            private boolean isLooping ( ) {

                  return hasMessages( WHAT_LOOP );
            }

            private void removeLoop ( ) {

                  if( isLooping() ) {
                        removeMessages( WHAT_LOOP );
                  }
            }

            @Override
            public void handleMessage ( Message msg ) {

                  LoopHandlerLayout loopHandlerLayout = mRef.get();
                  if( loopHandlerLayout == null ) {
                        return;
                  }

                  if( msg.what == WHAT_LOOP ) {
                        OnLoopListener loopListener = loopHandlerLayout.mOnLoopListener;
                        if( loopListener != null ) {
                              loopListener.onLoop( loopHandlerLayout );
                        }
                        if( loopHandlerLayout.isAutoLoop ) {
                              sendEmptyMessageDelayed(
                                  WHAT_LOOP,
                                  loopHandlerLayout.mLoopTime
                              );
                        }
                  }
            }
      }

      /**
       * 设置loop行为
       */
      public interface OnLoopListener {

            /**
             * 当loop时回调该方法
             *
             * @param layout 布局
             */
            void onLoop ( LoopHandlerLayout layout );
      }
}
