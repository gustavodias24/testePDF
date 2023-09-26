package com.benicio.testepdf;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;


import com.itextpdf.text.Anchor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button botao;

    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;
    // creating a bitmap variable
    // for storing our images
    Bitmap logoEmpresabmp, logoEpresaScaledbmp;
    Bitmap bmpTemplate, scaledbmpTemplate;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botao = findViewById(R.id.botao);

        logoEmpresabmp = BitmapFactory.decodeResource(getResources(), R.drawable.brasao);
        logoEpresaScaledbmp = Bitmap.createScaledBitmap(logoEmpresabmp, 104, 104, false);

        bmpTemplate = BitmapFactory.decodeResource(getResources(), R.drawable.templaterelatorio);
        scaledbmpTemplate = Bitmap.createScaledBitmap(bmpTemplate, 792, 1120, false);

        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        botao.setOnClickListener( view -> {
            generatePDF();
        });
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }




    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledbmpTemplate, 1, 1, paint);

        canvas.drawBitmap(logoEpresaScaledbmp, 76, 15, paint);


        title.setTextSize(25);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));

        canvas.drawText("PROJETO TESTE 123", 212, 173, title);
        canvas.drawText("Teste da Silva", 246, 226, title);

        int espacamentoEntrePontos = 25;
        int espacamentoEntreLinhas = 15;
        int max = 11;
        int index = 1;

        List<ExemploModel> lista = new ArrayList<>();

        lista.add(new ExemploModel(
                "26/09/2023",
                "https://earth.google.com/web/@-23.16335618,139.1763157,61.7453514a,364285.84619913d,35y,323.98457218h,0t,0r",
                "Gustavo Dias",
                "Ávore",
                "teste de observação")
        );

        lista.add(new ExemploModel(
                "26/09/2023",
                "https://earth.google.com/web/@-23.16335618,139.1763157,61.7453514a,364285.84619913d,35y,323.98457218h,0t,0r",
                "Gustavo Dias",
                "Ávore",
                "teste de observação")
        );

        lista.add(new ExemploModel(
                "26/09/2023",
                "https://earth.google.com/web/@-23.16335618,139.1763157,61.7453514a,364285.84619913d,35y,323.98457218h,0t,0r",
                "Gustavo Dias",
                "Ávore",
                "teste de observação")
        );
        title.setTextSize(10);

        int startX = 94;
        int startY = 303;

        for ( ExemploModel exemplo : lista){
            canvas.drawText(String.format("(%s) - %s - %s", index, exemplo.getData(), exemplo.getTipo()), startX, startY, title);
            startY += espacamentoEntreLinhas;
            canvas.drawText(String.format("Obs.: %s", exemplo.getObs()), startX, startY, title);
            startY += espacamentoEntreLinhas;

            canvas.drawText(String.format("Link: %s", exemplo.getLink()), startX, startY, title);
            startY += espacamentoEntrePontos;
            index++;
        }
//        title.setTextAlign(Paint.Align.CENTER);

        pdfDocument.finishPage(myPage);

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        if (!directory.exists()) {
            directory.mkdirs(); // Crie o diretório se ele não existir.
        }

        File file = new File(directory, "JUJUBADOCE.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(MainActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

}