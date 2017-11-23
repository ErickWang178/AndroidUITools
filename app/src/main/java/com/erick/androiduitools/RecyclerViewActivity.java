package com.erick.androiduitools;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/23 0023.
 * RecyclerView的Item滑出屏幕
 */

public class RecyclerViewActivity extends AppCompatActivity {
    private static final String TAG = "110";

    private RecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<>();
    private MyAdapter mAdapter;

    MyAdapter.MyViewHolder viewHolder = null;
    MyAdapter.MyViewHolder oldViewHolder = null;
    private int mClickPosition;
    private int mOldClickPosition = -1;
    private View mClickView = null;

    public interface UpdaListListener{
        void updateList(int newPos);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        for (int i=0; i<20; i++){
            mList.add(i + "");
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        final LinearLayoutManager layoutManger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManger);
        mAdapter = new MyAdapter();
        mAdapter.setUpdateListener(new UpdaListListener() {
            @Override
            public void updateList(int newPos) {
                mClickPosition = newPos;

                if (mOldClickPosition >= 0 && mOldClickPosition != newPos){
                    oldViewHolder = (MyAdapter.MyViewHolder) mRecyclerView.findViewHolderForAdapterPosition(mOldClickPosition);
                    if (oldViewHolder != null)
                        oldViewHolder.view.setVisibility(View.GONE);
                }

                mOldClickPosition = newPos;
            }
        });

        mRecyclerView.setAdapter(mAdapter);





        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstItemPosition = layoutManger.findFirstVisibleItemPosition();
                int lastItemPosition = layoutManger.findLastVisibleItemPosition();

//                if (mClickView == null){
//                    mClickView = layoutManger.findViewByPosition(mClickPosition);
//                }

                if (viewHolder == null){
                    //取得指定位置的ViewHolder
                    viewHolder = (MyAdapter.MyViewHolder) mRecyclerView.findViewHolderForAdapterPosition(mClickPosition);
                }

                if (mClickPosition < firstItemPosition || mClickPosition > lastItemPosition){
//                   if (mClickView != null) {
//                       mClickView.findViewById(R.id.view).setBackgroundColor(Color.RED);
//                       mClickView = null;
//
//                       Log.d(TAG, "onScrolled: 执行了 变色 操作");
//
//                   }
                    if (viewHolder != null){
                        viewHolder.view.setBackgroundColor(Color.RED);
                        mClickPosition = -1;
                        viewHolder = null;
                        Log.d(TAG, "onScrolled: 执行了 变色 操作");
                    }
                }

                Log.d(TAG, "onScrolled: firstItemPosition==" + firstItemPosition
                        + ",lastItemPosition==" + lastItemPosition + ",clickpos==" + mClickPosition);
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        private UpdaListListener mUpdateListener;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: --------");
            String data = mList.get(position);

            holder.view.setVisibility(View.GONE);
            holder.view.setBackgroundColor(Color.GREEN);
            holder.index.setText(data);
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        public void setUpdateListener(UpdaListListener listener){
            mUpdateListener = listener;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            Button btnOpen;
            View view;
            TextView index;

            public MyViewHolder(View v) {
                super(v);

                view = v.findViewById(R.id.view);
                index = (TextView) v.findViewById(R.id.index);

                btnOpen = (Button) v.findViewById(R.id.btn_show_view);
                btnOpen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.setVisibility(View.VISIBLE);

                        if (mUpdateListener != null){
                            mUpdateListener.updateList(getAdapterPosition());
                        }
                    }
                });
            }
        }
    }
}
