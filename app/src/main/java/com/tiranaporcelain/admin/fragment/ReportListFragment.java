package com.tiranaporcelain.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.JDF;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.activity.SelectCustomerActivity;
import com.tiranaporcelain.admin.adapter.ReportListAdapter;
import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.models.db.Report;
import com.tiranaporcelain.admin.models.db.ReportDao;
import com.tiranaporcelain.admin.utils.DaoManager;
import com.tiranaporcelain.admin.utils.LocaleUtils;
import com.tiranaporcelain.admin.utils.ViewUtils;

import org.greenrobot.greendao.query.QueryBuilder;
import org.parceler.Parcels;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTouch;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mphj on 1/29/18.
 */

public class ReportListFragment extends Fragment implements DateSetListener {

    public static final int REQUEST_PERSON = 1;



    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.person_name)
    TextView personName;

    @BindView(R.id.fromDate)
    EditText fromDateInput;

    @BindView(R.id.toDate)
    EditText toDateInput;

    @BindView(R.id.checkbox_deficit)
    CheckBox deficit;

    @BindView(R.id.checkbox_pay_debit)
    CheckBox payDebit;

    @BindView(R.id.checkbox_work)
    CheckBox work;

    String personNameText;

    public static ReportListFragment newInstance(){
        return new ReportListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            personName.setText(savedInstanceState.getString("text"));
            personName.setTag(savedInstanceState.getInt("person_name"));
            fromDate = (Calendar) savedInstanceState.getSerializable("from_date");
            toDate = (Calendar) savedInstanceState.getSerializable("to_date");
            loadListAsync();
        }
        return view;
    }

    void loadListAsync() {
        Observable.create(new ObservableOnSubscribe<List<Report>>() {

            @Override
            public void subscribe(ObservableEmitter<List<Report>> e) throws Exception {
                e.onNext(loadList());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Report>>() {
                    @Override
                    public void accept(List<Report> reports) throws Exception {
                        ReportListAdapter reportListAdapter = new ReportListAdapter(reports, getActivity());
                        recyclerView.setAdapter(reportListAdapter);
                    }
                });
    }

    List<Report> loadList() {
        QueryBuilder queryBuilder = DaoManager.session().getReportDao().queryBuilder();
        if (fromDate != null) {
            queryBuilder.where(ReportDao.Properties.Date.ge(fromDate.getTime().getTime()));
        }
        if (toDate != null) {
            queryBuilder.where(ReportDao.Properties.Date.le(toDate.getTime().getTime()));
        }
        queryBuilder.where(ReportDao.Properties.PersonId.eq(personName.getTag()));
        List<Report> reports = queryBuilder.list();
        reports = validateReports(reports);
        reports = validateReports(reports);
        return reports;
    }


    List<Report> validateReports(List<Report> reports) {
        for (int i = 0; i < reports.size(); i++) {
            Report report = reports.get(i);
            if (!work.isChecked()) {
                if (report.getType() == Report.TYPE_WORKING_REPORT) {
                    reports.remove(i);
                    continue;
                }
            }
            if (!deficit.isChecked() && !payDebit.isChecked()) {
                if (report.getType() == Report.TYPE_TRANSACTION) {
                    reports.remove(i);
                    continue;
                }
            }
            if (!deficit.isChecked()) {
                if (report.getTransactionType() == Report.TRANSACTION_DEBT
                        || report.getTransactionType() == Report.TRANSACTION_INSURANCE
                        || report.getTransactionType() == Report.TRANSACTION_OTHERS
                        || report.getTransactionType() == Report.TRANSACTION_HELP) {
                    reports.remove(i);
                    continue;
                }
            }
            if (!payDebit.isChecked()) {
                if (report.getTransactionType() == Report.TRANSACTION_SALARY) {
                    reports.remove(i);
                    continue;
                }
            }
        }
        return reports;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("text", personName.getText().toString());
        if (personName.getTag() != null)
            outState.putInt("person_name", (int) personName.getTag());
        outState.putSerializable("from_date", fromDate);
        outState.putSerializable("to_date", toDate);
        super.onSaveInstanceState(outState);
    }

    public void setupRecyclerView(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.select_person)
    void onRequestPerson() {
        startActivityForResult(
                new Intent(getActivity(), SelectCustomerActivity.class),
                REQUEST_PERSON
        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PERSON :
                if (resultCode == RESULT_OK) {
                    Customer customer = Parcels.unwrap(data.getParcelableExtra("customer"));
                    personName.setTag(customer.getId().intValue());
                    personName.setText(customer.getName() + " (" + LocaleUtils.e2f(customer.getPhone()) + ")");
                }
                break;
        }
    }


    Calendar fromDate;
    Calendar toDate;

    @OnTouch({R.id.fromDate, R.id.toDate})
    boolean onDateTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (!ViewUtils.isInViewBound(view, motionEvent))
                return true;
            if (view.getId() == R.id.fromDate) {
                getDatePicker(fromDate, view.getId()).show(getFragmentManager(), "DatePicker");
            } else {
                getDatePicker(toDate, view.getId()).show(getFragmentManager(), "DatePicker");
            }
        }
        return true;
    }


    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        EditText date = null;
        if (id == R.id.fromDate) {
            date = fromDateInput;
            fromDate = calendar;
        } else {
            date = toDateInput;
            toDate = calendar;
        }
        JDF currentDate = new JDF(calendar);
        date.setText(LocaleUtils.e2f(currentDate.getIranianDate()));
        date.setTag(calendar.getTime().getTime());
        date.clearFocus();
        loadListAsync();
    }


    @OnCheckedChanged({R.id.checkbox_work, R.id.checkbox_pay_debit, R.id.checkbox_deficit})
    void onClick() {
        loadListAsync();
    }

    public DatePicker getDatePicker(Calendar currentDate, int id) {
        DatePicker.Builder builder = new DatePicker.Builder()
                .future(true)
                .theme(R.style.DatePickerDialog)
                .id(id);
        if (currentDate != null)
            builder.date(currentDate);
        return builder.build(this);
    }

}
