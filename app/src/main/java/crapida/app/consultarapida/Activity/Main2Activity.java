package crapida.app.consultarapida.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import crapida.app.consultarapida.R;

public class Main2Activity extends AppCompatActivity {

    // Timer da splash screen
    //private static int SPLASH_TIME_OUT = 3000;
public FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FirebaseApp.initializeApp(this);

        new Handler().postDelayed(new Runnable() {
            //Exibindo splash com um timer.
            @Override
            public void run() {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                firebaseAuth = FirebaseAuth.getInstance();
                verificaUsuarioLogado();

                // Fecha esta activity
                finish();
            }
        }, 2000);

    }

    private boolean verificaUsuarioLogado() {
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Main2Activity.this, TelaPrin.class);
            startActivity(intent);
            finish();
            return true;

        }else {
            Intent intent = new Intent(Main2Activity.this, MainActivity.class);
            startActivity(intent);
            return false;
        }

    }

}
