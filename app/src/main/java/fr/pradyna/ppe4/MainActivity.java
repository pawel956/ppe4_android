package fr.pradyna.ppe4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ecouteMenu((ImageButton) findViewById(R.id.btnMenuInscription), InscriptionActivity.class);
        ecouteMenu((ImageButton) findViewById(R.id.btnMenuListeProduits), ListeProduitsActivity.class);
    }

    /**
     * Ouvrir l'activity correspondante
     * @param btn
     * @param classe
     */
    private void ecouteMenu(ImageButton btn, final Class classe) {
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, classe);
                startActivity(intent);
            }
        });
    }
}
