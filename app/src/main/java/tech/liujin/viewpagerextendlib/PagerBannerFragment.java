package tech.liujin.viewpagerextendlib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tech.liujin.pager.adapter.BasePagerAdapter;
import tech.liujin.pager.banner.ViewPagerBanner;
import tech.liujin.pager.indicator.DotView;

/**
 * @author Liujin 2018-09-22:8:53
 */
public class PagerBannerFragment extends Fragment {

      private ViewPagerBanner mBanner;

      public static PagerBannerFragment newInstance ( ) {

            Bundle args = new Bundle();

            PagerBannerFragment fragment = new PagerBannerFragment();
            fragment.setArguments( args );
            return fragment;
      }

      @Nullable
      @Override
      public View onCreateView (
          @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState ) {

            return inflater.inflate( R.layout.fragment_view_pager_banner, container, false );
      }

      @Override
      public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {

            super.onViewCreated( view, savedInstanceState );
            initView( view );
      }

      private void initView ( @NonNull final View itemView ) {

            mBanner = itemView.findViewById( R.id.banner );
            mBanner.setBannerAdapter( new FragmentAdapter() );
            mBanner.post( new Runnable() {

                  @Override
                  public void run ( ) {

                        mBanner.startLoop();
                  }
            } );
            mBanner.setPageMargin( 40 );

            DotView dotView = new DotView( getContext() );
            dotView.setupWithBanner( mBanner, Gravity.BOTTOM | Gravity.END, 50 );
      }

      private class FragmentAdapter extends BasePagerAdapter<String, TextView> {

            private int[] mBackGround = {
                getResources().getColor( R.color.orange ),
                getResources().getColor( R.color.red ),
                getResources().getColor( R.color.green ),
                getResources().getColor( R.color.purple ),
                getResources().getColor( R.color.black )
            };

            @Override
            public int getCount ( ) {

                  return mBackGround.length;
            }

            @Override
            protected String getData ( int position ) {

                  return "item " + position;
            }

            @Override
            protected TextView getView ( ViewGroup container, int position ) {

                  TextView textView = new TextView( container.getContext() );
                  textView.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 48 );
                  textView.setGravity( Gravity.CENTER );
                  textView.setTextColor( Color.WHITE );

                  return textView;
            }

            @Override
            protected void bindData ( int position, String data, TextView view ) {

                  view.setText( data );
                  view.setBackgroundColor( mBackGround[ position ] );
            }
      }
}
