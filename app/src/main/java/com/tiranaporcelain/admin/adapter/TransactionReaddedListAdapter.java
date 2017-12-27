package com.tiranaporcelain.admin.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.OnObjectItemClick;
import com.tiranaporcelain.admin.models.db.TransactionReadded;
import com.tiranaporcelain.admin.utils.LocaleUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mphj on 11/10/2017.
 */

public class TransactionReaddedListAdapter extends RecyclerView.Adapter<TransactionReaddedListAdapter.TransactionReaddedListViewHolder>{
    List<TransactionReadded> list;
    FragmentActivity fragmentActivity;
    OnObjectItemClick<TransactionReadded> onObjectItemClick;

    public TransactionReaddedListAdapter(List<TransactionReadded> list, FragmentActivity fragmentActivity){
        this.list = list;
        this.fragmentActivity = fragmentActivity;
    }

    public TransactionReaddedListAdapter(List<TransactionReadded> list, FragmentActivity fragmentActivity, OnObjectItemClick<TransactionReadded> click){
        this.list = list;
        this.fragmentActivity = fragmentActivity;
        this.onObjectItemClick = click;
    }

    @Override
    public TransactionReaddedListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.transaction_readded_list_item, viewGroup, false);
        return new TransactionReaddedListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionReaddedListViewHolder viewHolder, int i) {
        final TransactionReadded transactionReadded = list.get(i);
        viewHolder.title.setText(transactionReadded.getDescription());
        viewHolder.icon.setImageResource(
                (transactionReadded.getType() == TransactionReadded.INC)? R.drawable.ic_plus : R.drawable.ic_remove
        );
        viewHolder.leftText.setText(LocaleUtils.englishNumberToArabic("" + (int)transactionReadded.getPrice()) + " تومان");
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onObjectItemClick != null){
                    onObjectItemClick.onClick(v, transactionReadded);
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TransactionReaddedListViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.container)
        RelativeLayout container;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.leftText)
        TextView leftText;

        @BindView(R.id.icon)
        ImageView icon;

        public TransactionReaddedListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
