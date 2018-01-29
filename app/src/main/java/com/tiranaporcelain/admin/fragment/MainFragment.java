package com.tiranaporcelain.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.activity.ReportWorkActivity;
import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.models.db.Report;
import com.tiranaporcelain.admin.models.db.ReportDao;
import com.tiranaporcelain.admin.utils.DaoManager;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mphj on 11/7/2017.
 */

public class MainFragment extends Fragment {


    @BindView(R.id.person_container)
    LinearLayout personContainer;

    @BindView(R.id.person_container_404)
    TextView emptyPersonContainer;

    public MainFragment(){

    }

    public static MainFragment newInstance(){
        return new MainFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        personContainer.removeAllViewsInLayout();

        Calendar beginOfDay = Calendar.getInstance();
        beginOfDay.set(Calendar.HOUR_OF_DAY, 0);
        beginOfDay.set(Calendar.MINUTE, 0);
        beginOfDay.set(Calendar.SECOND, 0);
        beginOfDay.set(Calendar.MILLISECOND, 0);

        Calendar endOfDay = Calendar.getInstance();
        endOfDay.set(Calendar.HOUR_OF_DAY, 24);
        endOfDay.set(Calendar.MILLISECOND, 0);
        endOfDay.set(Calendar.MINUTE, 0);
        endOfDay.set(Calendar.SECOND, 0);

        List<Customer> list = DaoManager.session().getCustomerDao().loadAll();
        boolean isAnythingThere = false;
        for (Customer customer : list) {
            int size = (int) DaoManager.session().getReportDao().queryBuilder()
                    .where(ReportDao.Properties.PersonId.eq(customer.getId()))
                    .where(ReportDao.Properties.Type.eq(Report.TYPE_WORKING_REPORT))
                    .where(ReportDao.Properties.Date.ge(beginOfDay.getTime().getTime()))
                    .where(ReportDao.Properties.Date.le(endOfDay.getTime().getTime()))
                    .count();
            if (size == 0) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.person_small_item, null, false);
                ViewHolder viewHolder = new ViewHolder(view);
                viewHolder.title.setText(customer.getName());
                view.   setTag(customer.getId().intValue());
                TransitionManager.beginDelayedTransition(personContainer);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), ReportWorkActivity.class);
                        i.putExtra("customer_id", (int) v.getTag());
                        v.getContext().startActivity(i);
                    }
                });
                personContainer.addView(view);
                isAnythingThere = true;
            }
        }

        if (!isAnythingThere) {
            emptyPersonContainer.setVisibility(View.VISIBLE);
        } else {
            emptyPersonContainer.setVisibility(View.GONE);
        }
    }

    static class ViewHolder {

        @BindView(R.id.title)
        TextView title;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
