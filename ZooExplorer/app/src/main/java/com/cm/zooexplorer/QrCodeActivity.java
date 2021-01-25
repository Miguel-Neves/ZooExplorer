package com.cm.zooexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class QrCodeActivity extends AppCompatActivity {
    public static String HABITAT_ID = "com.cm.zooexplorer.extra.HABITAT_ID";
    CodeScanner codeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                Intent intent = new Intent();
                intent.putExtra(HABITAT_ID, Integer.parseInt(result.getText()));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        codeScanner.startPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        codeScanner.releaseResources();
    }
}
