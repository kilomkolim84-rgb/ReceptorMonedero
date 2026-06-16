package com.cesar.receptor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import com.google.firebase.database.*;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    TextView total, contador; ListView lista;
    ArrayList<String> datos = new ArrayList<>(); ArrayAdapter<String> adapter;
    double suma = 0; int conteo = 0;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_main);
        total = findViewById(R.id.total); contador = findViewById(R.id.contador);
        lista = findViewById(R.id.lista);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        lista.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference().addChildEventListener(new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot d, String s) {
                String raw = d.getValue().toString();
                if (raw.contains("monto=")) raw = raw.substring(raw.indexOf("monto=") + 6).replace("}", "");
                raw = raw.replace("S/", "").replace(",", ".").trim();
                try {
                    double monto = Double.parseDouble(raw);
                    suma += monto; conteo++;
                    total.setText("S/ " + String.format("%.2f", suma));
                    contador.setText("Historial - " + conteo + " monedas");
                    datos.add(0, "S/ " + String.format("%.2f", monto));
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {}
            }
            @Override public void onChildChanged(DataSnapshot d, String s) {}
            @Override public void onChildRemoved(DataSnapshot d) {}
            @Override public void onChildMoved(DataSnapshot d, String s) {}
            @Override public void onCancelled(DatabaseError e) { total.setText("Error Firebase"); }
        });
    }
}
