package com.tiranaporcelain.admin.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alirezaafkar.sundatepicker.components.JDF;
import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.OnObjectItemClick;
import com.tiranaporcelain.admin.models.db.Check;
import com.tiranaporcelain.admin.models.db.CheckDao;
import com.tiranaporcelain.admin.utils.DaoManager;
import com.tiranaporcelain.admin.utils.LocaleUtils;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mphj on 11/20/17.
 */

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.CheckViewHolder> {

    List<Check> list;

    FragmentActivity fragmentActivity;

    OnObjectItemClick<Check> onObjectItemClick;

    public CheckListAdapter(List<Check> list, FragmentActivity fragmentActivity){
        this.list = list;
        this.fragmentActivity = fragmentActivity;
    }

    public CheckListAdapter(List<Check> list, FragmentActivity fragmentActivity, OnObjectItemClick<Check> click){
        this.list = list;
        this.fragmentActivity = fragmentActivity;
        this.onObjectItemClick = click;
    }

    @Override
    public CheckViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.check_list_item, viewGroup, false);
        return new CheckViewHolder(itemView);
    }

    private String dateToString(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        JDF jdf = new JDF(calendar);
        return jdf.getIranianDate();
    }

    @Override
    public void onBindViewHolder(final CheckViewHolder viewHolder, int i) {
        final Check check = list.get(i);
        if (check.isPaied()) {
            viewHolder.setPaied.setVisibility(View.GONE);
        }
        viewHolder.subText.setText(
                "تاریخ سررسید : "
                + dateToString(check.getDueDate())
        );
        viewHolder.text.setText("چک ("
                + LocaleUtils.englishNumberToArabic("" + check.getSerial())
                + ")"
        );
        viewHolder.setPaied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckDao checkDao = DaoManager.session().getCheckDao();
                check.setPaied(true);
                checkDao.save(check);
                viewHolder.setPaied.setVisibility(View.GONE);
            }
        });
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onObjectItemClick != null){
                    onObjectItemClick.onClick(v, check);
                    return;
                }
                //BottomSheetDialogFragment bottomSheetDialogFragment = CustomerSettingDialog.create(customer);
                //bottomSheetDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CheckViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text)
        public TextView text;

        @BindView(R.id.subText)
        public TextView subText;

        @BindView(R.id.loading)
        public ImageView loading;

        @BindView(R.id.setPaied)
        public Button setPaied;

        @BindView(R.id.container)
        CardView container;

        public CheckViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}