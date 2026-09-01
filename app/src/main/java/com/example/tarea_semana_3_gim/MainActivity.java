package com.example.tarea_semana_3_gim;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private LinearLayout layoutMenu;
    private ScrollView layoutPrograma, layoutFragmento;
    private TextView tvHorario, tvTexto;
    private EditText etEventoId, etDia, etHora, etActividad;
    private EditText etFragmentoId, etTitulo, etAutor, etTextoFragmento;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        layoutMenu = findViewById(R.id.layoutMenu);
        layoutPrograma = findViewById(R.id.layoutPrograma);
        layoutFragmento = findViewById(R.id.layoutFragmento);
        tvHorario = findViewById(R.id.tvHorario);
        tvTexto = findViewById(R.id.tvTexto);

        etEventoId = findViewById(R.id.etEventoId);
        etDia = findViewById(R.id.etDia);
        etHora = findViewById(R.id.etHora);
        etActividad = findViewById(R.id.etActividad);

        etFragmentoId = findViewById(R.id.etFragmentoId);
        etTitulo = findViewById(R.id.etTitulo);
        etAutor = findViewById(R.id.etAutor);
        etTextoFragmento = findViewById(R.id.etTextoFragmento);

        Button btnPrograma = findViewById(R.id.btnPrograma);
        Button btnFragmento = findViewById(R.id.btnFragmento);
        Button btnVolverPrograma = findViewById(R.id.btnVolverPrograma);
        Button btnVolverFragmento = findViewById(R.id.btnVolverFragmento);

        Button btnInsertarEvento = findViewById(R.id.btnInsertarEvento);
        Button btnActualizarEvento = findViewById(R.id.btnActualizarEvento);
        Button btnEliminarEvento = findViewById(R.id.btnEliminarEvento);

        Button btnInsertarFragmento = findViewById(R.id.btnInsertarFragmento);
        Button btnActualizarFragmento = findViewById(R.id.btnActualizarFragmento);
        Button btnEliminarFragmento = findViewById(R.id.btnEliminarFragmento);

        btnPrograma.setOnClickListener(v -> {
            cargarPrograma();
            mostrarPantalla(layoutPrograma);
        });

        btnFragmento.setOnClickListener(v -> {
            cargarFragmento();
            mostrarPantalla(layoutFragmento);
        });

        btnVolverPrograma.setOnClickListener(v -> mostrarPantalla(layoutMenu));
        btnVolverFragmento.setOnClickListener(v -> mostrarPantalla(layoutMenu));

        btnInsertarEvento.setOnClickListener(v -> insertarEvento());
        btnActualizarEvento.setOnClickListener(v -> actualizarEvento());
        btnEliminarEvento.setOnClickListener(v -> eliminarEvento());

        btnInsertarFragmento.setOnClickListener(v -> insertarFragmento());
        btnActualizarFragmento.setOnClickListener(v -> actualizarFragmento());
        btnEliminarFragmento.setOnClickListener(v -> eliminarFragmento());
    }

    private void mostrarPantalla(View pantalla) {
        layoutMenu.setVisibility(View.GONE);
        layoutPrograma.setVisibility(View.GONE);
        layoutFragmento.setVisibility(View.GONE);
        pantalla.setVisibility(View.VISIBLE);
    }

    // ---------- CRUD EVENTOS ----------
    private void insertarEvento() {
        String codigoStr = etEventoId.getText().toString().trim();
        String dia = etDia.getText().toString().trim();
        String hora = etHora.getText().toString().trim();
        String actividad = etActividad.getText().toString().trim();

        if (codigoStr.isEmpty() || dia.isEmpty() || hora.isEmpty() || actividad.isEmpty()) {
            Toast.makeText(this, "Completa código, día, hora y actividad", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoStr);
            long id = dbHelper.insertarEvento(codigo, dia, hora, actividad);
            Toast.makeText(this, id != -1 ? "Evento insertado" : "Ese código ya existe", Toast.LENGTH_SHORT).show();
            limpiarCamposEvento();
            cargarPrograma();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El código debe ser un número", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarEvento() {
        String codigoStr = etEventoId.getText().toString().trim();
        String dia = etDia.getText().toString().trim();
        String hora = etHora.getText().toString().trim();
        String actividad = etActividad.getText().toString().trim();

        if (codigoStr.isEmpty() || dia.isEmpty() || hora.isEmpty() || actividad.isEmpty()) {
            Toast.makeText(this, "Completa código, día, hora y actividad", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int filas = dbHelper.actualizarEvento(Integer.parseInt(codigoStr), dia, hora, actividad);
            Toast.makeText(this, filas > 0 ? "Actualizado" : "No existe ese código", Toast.LENGTH_SHORT).show();
            limpiarCamposEvento();
            cargarPrograma();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El código debe ser un número", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarEvento() {
        String codigoStr = etEventoId.getText().toString().trim();
        if (codigoStr.isEmpty()) {
            Toast.makeText(this, "Ingresa el código a eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int filas = dbHelper.eliminarEvento(Integer.parseInt(codigoStr));
            Toast.makeText(this, filas > 0 ? "Eliminado" : "No existe ese código", Toast.LENGTH_SHORT).show();
            limpiarCamposEvento();
            cargarPrograma();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El código debe ser un número", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCamposEvento() {
        etEventoId.setText("");
        etDia.setText("");
        etHora.setText("");
        etActividad.setText("");
    }

    private void cargarPrograma() {
        Cursor cursor = dbHelper.obtenerEventos();
        StringBuilder sb = new StringBuilder();
        String diaAnterior = "";

        if (cursor.moveToFirst()) {
            do {
                int codigo = cursor.getInt(cursor.getColumnIndexOrThrow("codigo"));
                String dia = cursor.getString(cursor.getColumnIndexOrThrow("dia"));
                String hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"));
                String actividad = cursor.getString(cursor.getColumnIndexOrThrow("actividad"));

                if (!dia.equals(diaAnterior)) {
                    if (!diaAnterior.isEmpty()) sb.append("\n");
                    sb.append(dia).append("\n");
                    diaAnterior = dia;
                }
                sb.append("[Código ").append(codigo).append("] ")
                        .append(hora).append(" - ").append(actividad).append("\n");
            } while (cursor.moveToNext());
        } else {
            sb.append("No hay eventos registrados");
        }
        cursor.close();
        tvHorario.setText(sb.toString());
    }

    // ---------- CRUD FRAGMENTOS ----------
    private void insertarFragmento() {
        String codigoStr = etFragmentoId.getText().toString().trim();
        String titulo = etTitulo.getText().toString().trim();
        String autor = etAutor.getText().toString().trim();
        String texto = etTextoFragmento.getText().toString().trim();

        if (codigoStr.isEmpty() || titulo.isEmpty() || autor.isEmpty() || texto.isEmpty()) {
            Toast.makeText(this, "Completa código, título, autor y texto", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoStr);
            long id = dbHelper.insertarFragmento(codigo, titulo, autor, texto);
            Toast.makeText(this, id != -1 ? "Fragmento insertado" : "Ese código ya existe", Toast.LENGTH_SHORT).show();
            limpiarCamposFragmento();
            cargarFragmento();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El código debe ser un número", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarFragmento() {
        String codigoStr = etFragmentoId.getText().toString().trim();
        String titulo = etTitulo.getText().toString().trim();
        String autor = etAutor.getText().toString().trim();
        String texto = etTextoFragmento.getText().toString().trim();

        if (codigoStr.isEmpty() || titulo.isEmpty() || autor.isEmpty() || texto.isEmpty()) {
            Toast.makeText(this, "Completa código, título, autor y texto", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int filas = dbHelper.actualizarFragmento(Integer.parseInt(codigoStr), titulo, autor, texto);
            Toast.makeText(this, filas > 0 ? "Actualizado" : "No existe ese código", Toast.LENGTH_SHORT).show();
            limpiarCamposFragmento();
            cargarFragmento();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El código debe ser un número", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarFragmento() {
        String codigoStr = etFragmentoId.getText().toString().trim();
        if (codigoStr.isEmpty()) {
            Toast.makeText(this, "Ingresa el código a eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int filas = dbHelper.eliminarFragmento(Integer.parseInt(codigoStr));
            Toast.makeText(this, filas > 0 ? "Eliminado" : "No existe ese código", Toast.LENGTH_SHORT).show();
            limpiarCamposFragmento();
            cargarFragmento();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El código debe ser un número", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCamposFragmento() {
        etFragmentoId.setText("");
        etTitulo.setText("");
        etAutor.setText("");
        etTextoFragmento.setText("");
    }

    private void cargarFragmento() {
        Cursor cursor = dbHelper.obtenerFragmentos();
        StringBuilder sb = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                int codigo = cursor.getInt(cursor.getColumnIndexOrThrow("codigo"));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
                String autor = cursor.getString(cursor.getColumnIndexOrThrow("autor"));
                String texto = cursor.getString(cursor.getColumnIndexOrThrow("texto"));

                sb.append("[Código ").append(codigo).append("] \"").append(titulo).append("\", ").append(autor).append("\n");
                sb.append(texto).append("\n\n");
            } while (cursor.moveToNext());
        } else {
            sb.append("No hay fragmentos registrados");
        }
        cursor.close();
        tvTexto.setText(sb.toString());
    }
}