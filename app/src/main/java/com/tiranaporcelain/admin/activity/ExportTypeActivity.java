package com.tiranaporcelain.admin.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.utils.PdfReportExporter;
import com.tiranaporcelain.admin.utils.XlsReportExporter;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ExportTypeActivity extends BaseActivity {

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.submit)
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_type);
        ButterKnife.bind(this);
    }



    @OnClick(R.id.submit)
    void submit() {
        final int id = getIntent().getIntExtra("id", 0);
        if (id == 0) {
            finish();
            return;
        }
        submit.setEnabled(false);
        if (radioGroup.getCheckedRadioButtonId() == R.id.radio_button_pdf) {
            exportPdf(id);
        } else {
            exportXls(id);
        }
    }


    void exportPdf(final int id) {
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                PdfReportExporter pdfReportExporter = PdfReportExporter.byId(id);
                pdfReportExporter.export("report_" + id + ".pdf");
                e.onNext(PdfReportExporter.destinationFolder + File.separator + "report_" + id + ".pdf");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        try{

                            File file = new File(s);
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            target.setDataAndType(Uri.fromFile(file), "application/pdf");
                            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            Intent intent = Intent.createChooser(target, "Open pdf");
                            startActivity(intent);
                        }catch (Exception e){

                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        submit.setEnabled(true);
                        finish();
                    }
                })
                .subscribe();
    }



    void exportXls(final int id) {
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                XlsReportExporter pdfReportExporter = XlsReportExporter.byId(id);
                pdfReportExporter.export("report_" + id + ".xls");
                e.onNext(PdfReportExporter.destinationFolder + File.separator + "report_" + id + ".xls");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        try{

                            File file = new File(s);
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            target.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            Intent intent = Intent.createChooser(target, "Open pdf");
                            startActivity(intent);
                        }catch (Exception e){

                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        submit.setEnabled(true);
                        finish();
                    }
                })
                .subscribe();
    }
}