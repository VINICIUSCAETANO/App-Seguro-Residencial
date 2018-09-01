package com.example.vinicius.appseguroresidencial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    private EditText edtCLiente;
    private EditText edtEndereco;
    private Spinner spnTipoImovel;
    private CheckBox cbCliente;
    private EditText edtAvaliacao;
    private Button btCalcular;
    private Button btNovo;
    private Button btListar;
    private TextView txtValor;
    private Button btRegistrar;
    private double valorSeguro = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCLiente = (EditText) findViewById(R.id.edtCliente);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        spnTipoImovel = (Spinner) findViewById(R.id.spnTipoImovel);
        cbCliente = (CheckBox) findViewById(R.id.cbCliente);
        edtAvaliacao = (EditText) findViewById(R.id.edtAvaliacao);
        btCalcular = (Button) findViewById(R.id.btCalcular);
        btNovo = (Button) findViewById(R.id.btNovo);
        btListar = (Button) findViewById(R.id.btListar);
        txtValor = (TextView) findViewById(R.id.txtValor);
        btRegistrar = (Button) findViewById(R.id.btRegistrar);

        btRegistrar.setVisibility(View.INVISIBLE);
        txtValor.setVisibility(View.INVISIBLE);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.tiposImovel,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item);
        spnTipoImovel.setAdapter(adapter);
    }

    public void calcular(View view) {
        //validar os dados
        //nome do cliente
        if(edtCLiente.getText().toString().trim().isEmpty()) {
            Toast.makeText(this,
                    "Informe corretamente o nome do cliente",
                    Toast.LENGTH_LONG).show();
            edtCLiente.requestFocus();
            return;
        }
        //endereco
        if(edtEndereco.getText().toString().trim().isEmpty()) {
            Toast.makeText(this,
                    "Informe corretamente o enderećo",
                    Toast.LENGTH_LONG).show();
            edtEndereco.requestFocus();
            return;
        }
        //avaliacao
        if(edtAvaliacao.getText().toString().trim().isEmpty() ||
                Double.parseDouble(edtAvaliacao.getText().toString()) <= 0) {
            Toast.makeText(this,
                    "Informe corretamente o valor de avaliação",
                    Toast.LENGTH_LONG).show();
            edtAvaliacao.requestFocus();
            return;
        }

        Double valorAvaliacao = Double.parseDouble(edtAvaliacao.getText().toString());

        String tipo = spnTipoImovel.getSelectedItem().toString();
        switch (tipo) {
            case "Casa":
                valorSeguro = valorAvaliacao * (0.2 / 100.00);
                break;
            case "Apto":
                valorSeguro = valorAvaliacao * (0.15 / 100.00);
                break;
            case "Comercial":
                valorSeguro = valorAvaliacao * (0.25 / 100.00);
                break;
        }

        if(cbCliente.isChecked()) {
            valorSeguro = valorSeguro * 0.9;
        }

        String valorSeguroFormat = String.format("%.2f",valorSeguro);
        txtValor.setText("Valor Estimado do seguro: R$ " + valorSeguroFormat);
        btRegistrar.setVisibility(View.VISIBLE);
        txtValor.setVisibility(View.VISIBLE);
    }

    public void novo(View view) {
        limparCampos();
        edtCLiente.requestFocus();
        btRegistrar.setVisibility(View.INVISIBLE);
        txtValor.setVisibility(View.INVISIBLE);
    }


    public void registrar(View view) {
        String cliente = edtCLiente.getText().toString();
        String tipo = spnTipoImovel.getSelectedItem().toString();
        String valorSeguroFormat = String.format("%.2f", valorSeguro);
        try {
            FileOutputStream fs =
                    openFileOutput("lista.txt", MODE_APPEND);
            PrintWriter pw = new PrintWriter(fs);
            pw.println("Cliente : "+ cliente + ";" +
                "Tipo de Imóvel : " + tipo + ";" +
                "Valor do Seguro: R$ " + valorSeguroFormat );
            pw.close();
            Toast.makeText(this, "Ok! Registro do Imóvel Gravado",
                    Toast.LENGTH_LONG).show();
            limparCampos();
            btRegistrar.setVisibility(View.INVISIBLE);
            txtValor.setVisibility(View.INVISIBLE);
            edtCLiente.requestFocus();
        } catch (IOException e) {
            Toast.makeText(this, "Erro: "+e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void limparCampos() {
        edtCLiente.setText("");
        edtEndereco.setText("");
        edtAvaliacao.setText("");
        cbCliente.setChecked(false);
    }

    public void listar(View view) {
        Intent it = new Intent(this, ListaView.class);
        startActivity(it);
    }
}
