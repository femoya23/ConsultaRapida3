package crapida.app.consultarapida;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class telaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        BottomNavigationView bottomNavigateView = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        bottomNavigateView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.action_avaliar:
                        Toast.makeText(telaPrincipal.this, "Abrir avaliar", Toast.LENGTH_SHORT).show();
                    break;
                    case R.id.action_buscar:
                        startActivity(new Intent(telaPrincipal.this, filtroBuscaActivity.class));;
                    break;
                    case R.id.action_consultas:
                        Toast.makeText(telaPrincipal.this, "Abrir consultas", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_perfil:
                        startActivity(new Intent(telaPrincipal.this, perfil.class));;
                        break;
                } return true;
            }
        });


    }
}
