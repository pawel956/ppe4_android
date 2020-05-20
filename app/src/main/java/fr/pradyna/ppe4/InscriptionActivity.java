package fr.pradyna.ppe4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        ecouteMenuInscription();
        inscription();
    }

    /**
     * Retour au menu
     */
    private void ecouteMenuInscription() {
        findViewById(R.id.btnInscriptionAccueil).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(InscriptionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void inscription() {
        findViewById(R.id.btnInscriptionValider).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(InscriptionActivity.this);
                String url = Constants.API_URL + "/api/utilisateur/add";

                final EditText nom = findViewById(R.id.editTextInscriptionNom);
                final EditText prenom = findViewById(R.id.editTextInscriptionPrenom);
                final EditText courriel = findViewById(R.id.editTextInscriptionCourriel);
                final EditText telephone = findViewById(R.id.editTextInscriptionTelephone);

                RadioGroup rg_genre = findViewById(R.id.radioGroupInscriptionGenre);
                int radioId = rg_genre.getCheckedRadioButtonId();
                final RadioButton genre = rg_genre.findViewById(radioId);

                EditText ed_dateNaissance = findViewById(R.id.editTextInscriptionDateNaissance);
                final String[] dateNaissance_split = ed_dateNaissance.getText().toString().split("/");
                final String dateNaissance = dateNaissance_split[2] + "-" + dateNaissance_split[1] + "-" + dateNaissance_split[0];

                final EditText mdp = findViewById(R.id.editTextInscriptionMdp);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (!(new JSONObject(response)).getBoolean("error")) {
                                        Toast.makeText(InscriptionActivity.this, "Opération réussie", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(InscriptionActivity.this, "Echec de l'opération", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InscriptionActivity.this, "Erreur, " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("nom", nom.getText().toString());
                        params.put("prenom", prenom.getText().toString());
                        params.put("email", courriel.getText().toString());
                        params.put("telephone", telephone.getText().toString());
                        params.put("genre", genre.getText().toString());
                        params.put("dateNaissance", dateNaissance);
                        params.put("mdp", mdp.getText().toString());
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}
