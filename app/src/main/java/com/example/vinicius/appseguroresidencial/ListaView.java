package com.example.vinicius.appseguroresidencial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class ListaView extends AppCompatActivity {

    private ListView lstSeguros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_view);

        lstSeguros = (ListView) findViewById(R.id.lstSeguros);


        ArrayAdapter<String> adapter = new
                ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        lstSeguros.setAdapter(adapter);

        try {
            FileInputStream fs = openFileInput("lista.txt");
            Scanner entrada = new Scanner(fs);
            while (entrada.hasNextLine()) {
                String linha = entrada.nextLine();
                String[] partes = linha.split(";");
                adapter.add(partes[0]+"\n"
                        + partes[1]+"\n"
                        + partes[2]);
            }
            lstSeguros.setAdapter(adapter);
        } catch (IOException e) {
            Toast.makeText(this, "Não há nenhum registro cadastrado",
                    Toast.LENGTH_LONG).show();
        }
    }
}

