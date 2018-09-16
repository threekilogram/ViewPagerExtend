package tech.threekilogram.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import java.lang.reflect.Field;

/**
 * @author LiuJin
 * @date 2017-12-25
 *     为viewpager 增加 自动轮播功能
 */
public class ExtendViewPager extends ViewPager {

      /**
       * 将viewPager的scroller替换使其可以增加滚动时间
       */
      protected DurationAddableScroller mScroller;

      public ExtendViewPager ( Context context ) {

            this( context, null );
      }

      public ExtendViewPager ( Context context, AttributeSet attrs ) {

            super( context, attrs );
            init();
      }

      protected void init ( ) {

            //反射获得 scroller
            attachViewPagerScroller();
      }

      /**
       * @param b {@link #setClipChildren(boolean)} 和 {@link #setClipToPadding(boolean)} 都将设置为 b
       */
      public void setClip ( boolean b ) {

            setClipChildren( b );
            setClipToPadding( b );
      }

      /**
       * 设置增加滚动时间
       *
       * @param addedDuration 增加的时间
       */
      public void setDurationAdded ( int addedDuration ) {

            if( mScroller != null ) {
                  mScroller.setDurationAdded( addedDuration );
            }
      }

      /**
       * 获取设置的滚动时间增加
       */
      public int getDurationAdded ( ) {

            if( mScroller != null ) {
                  return mScroller.getDurationAdded();
            } else {
                  return 0;
            }
      }

      /**
       * 将viewPager的scroller设置为可增加时间scroller,并且持有它,用于以后更改滚动时间
       */
      private void attachViewPagerScroller ( ) {

            try {
                  Field scrollerInPager = ViewPager.class.getDeclaredField( "mScroller" );
                  scrollerInPager.setAccessible( true );

                  Field interpolator = ViewPager.class.getDeclaredField( "sInterpolator" );
                  interpolator.setAccessible( true );

                  mScroller = new DurationAddableScroller(
                      this.getContext(), (Interpolator) interpolator.get( null ) );

                  scrollerInPager.set( this, mScroller );
            } catch(NoSuchFieldException e) {

                  e.printStackTrace();
            } catch(IllegalAccessException e) {

                  e.printStackTrace();
            }
      }
}