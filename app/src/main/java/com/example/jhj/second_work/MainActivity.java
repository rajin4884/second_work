package com.example.jhj.second_work;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import adapters.ContactListAdapter;
import database.ContactDB;
import entities.Contact;


public class MainActivity extends AppCompatActivity {
    //데이타베이스
    private Button buttonAdd;
    private ListView listViewContents;



    // 웹뷰
    private EditText url_String;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 데이타베이스
        this.buttonAdd = findViewById(R.id.buttonAdd);
        this.buttonAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(intent1);
            }
        });
        final ContactDB contactDB = new ContactDB(this);
        this.listViewContents = findViewById(R.id.listViewContacts);
        this.listViewContents.setAdapter(new ContactListAdapter(this,contactDB.findAll()));
        this.listViewContents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact = contactDB.findAll().get(i);
                Intent intent1 = new Intent(MainActivity.this, ContactDetailActivity.class);
                intent1.putExtra("contact", contact);
                startActivity(intent1);
            }
        });




        //탭 추가 코드
        TabHost tabh = (TabHost)findViewById(R.id.tabh);
        tabh.setup();

        TabHost.TabSpec tab1 = tabh.newTabSpec("1").setContent(R.id.tab1).setIndicator("tab1");
        TabHost.TabSpec tab2 = tabh.newTabSpec("2").setContent(R.id.tab2).setIndicator("tab2");
        TabHost.TabSpec tab3 = tabh.newTabSpec("3").setContent(R.id.tab3).setIndicator("tab3");
        TabHost.TabSpec tab4 = tabh.newTabSpec("4").setContent(R.id.tab4).setIndicator("tab4");

        tabh.addTab(tab1);
        tabh.addTab(tab2);
        tabh.addTab(tab3);
        tabh.addTab(tab4);



        //webview 코드
        webView = (WebView)findViewById(R.id.webview);
        Button button = (Button)findViewById(R.id.go);
        url_String = (EditText)findViewById(R.id.url);


        webView.getSettings().setJavaScriptEnabled(true); //자바스크립트를 사용할 수 있도록 설정
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted (WebView view, String url, Bitmap favicon){
                String urlString = webView.getUrl().toString();
                url_String.setText(urlString);
            }
        });
        webView.loadUrl("http://hywoman.ac.kr");

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String urlString = url_String.getText().toString();
                if(urlString.startsWith("http") != true)
                    urlString = "http://"+urlString;

                webView.loadUrl(urlString);
            }
        });

        url_String.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER)
                {
                    String urlString = url_String.getText().toString();

                    if (urlString.startsWith("http") != true)
                        urlString = "http://" + urlString;
                    webView.loadUrl(urlString);
                    return true;
                }
                return false;
                }
        });


    }



    //thread 코드
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            TextView myTextView = (TextView)findViewById(R.id.myTextView);
            myTextView.setText(string);
        }
    };

    public void buttonClick(View view)
    {
        Runnable runnable = new Runnable() {
            public void run() {
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                SimpleDateFormat dateformat =
                        new SimpleDateFormat("HH:mm:ss MM/dd/yyyy",
                                Locale.US);
                String dateString =
                        dateformat.format(new Date());
                bundle.putString("myKey", dateString);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        };
        Thread myThread = new Thread(runnable);
        myThread.start();
    }
}