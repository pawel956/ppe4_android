package fr.pradyna.ppe4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListeProduitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_produits);
        ecouteMenuListeProduits();
        affichageListeProduits();
    }

    /**
     * Retour au menu
     */
    private void ecouteMenuListeProduits() {
        findViewById(R.id.btnListeProduitsAccueil).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ListeProduitsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void affichageListeProduits() {
        RequestQueue queue = Volley.newRequestQueue(ListeProduitsActivity.this);

        String url = Constants.API_URL + "/api/produit/all";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ListView lst = findViewById(R.id.list_produits_all);

                        ArrayList<String> lesProduits = new ArrayList<>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = jsonArray.getJSONObject(i);
                                lesProduits.add(item.getString("libelle"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ListeProduitsActivity.this, android.R.layout.simple_list_item_1, lesProduits);
                        lst.setAdapter(arrayAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListeProduitsActivity.this, "Erreur, " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }
}
