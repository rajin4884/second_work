package com.example.jhj.second_work;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jhj.second_work.MainActivity;
import com.example.jhj.second_work.R;

import database.ContactDB;
import entities.Contact;

public class ContactDetailActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewPhone;
    private Button buttonBack;
    private Button buttonDelete;
    private Button buttonEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        Intent intent1 = getIntent();
        final Contact contact = (Contact) intent1.getSerializableExtra("contact");

        this.textViewName = findViewById(R.id.textViewName);
        this.textViewName.setText(contact.getName());
        this.textViewPhone = findViewById(R.id.textViewPhone);
        this.textViewPhone.setText(contact.getPhone());

        this.buttonBack = (Button) findViewById(R.id.buttonBack);
        this.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ContactDetailActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        this.buttonDelete = (Button) findViewById(R.id.buttonDelete);
        this.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(false);
                builder.setTitle("확인");
                builder.setMessage("진짜로 삭제하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactDB contactDB = new ContactDB(getBaseContext());
                        if(contactDB.delete(contact.getId())){
                            Intent intent1 = new Intent(ContactDetailActivity.this, MainActivity.class);
                            startActivity(intent1);
                        }else{
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getBaseContext());
                            builder1.setCancelable(false);
                            builder1.setMessage("실패");
                            builder1.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();

                                }
                            });

                            builder1.create().show();
                        }
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();


                builder.create();
            }
        });

        this.buttonEdit = (Button) findViewById(R.id.buttonEdit);
        this.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ContactDetailActivity.this, EditContactActivity.class);
                intent1.putExtra("contact", contact);
                startActivity(intent1);
            }
        });

    }
}
