package fr.pradyna.ppe4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                        final List<Map<String, String>> lesProduits = new ArrayList<>();
                        ArrayList<String> arrayListProduits = new ArrayList<>();

                        try {
                            JSONArray JSONArray = new JSONArray(response);
                            for (int i = 0; i < JSONArray.length(); i++) {
                                JSONObject item = JSONArray.getJSONObject(i);

                                String libelle = item.getString("libelle");

                                Map<String, String> map = new HashMap<>();
                                map.put("id", item.getString("id"));
                                map.put("libelle", libelle);
                                lesProduits.add(i, map);

                                arrayListProduits.add(libelle);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ListeProduitsActivity.this, android.R.layout.simple_list_item_1, arrayListProduits);
                        lst.setAdapter(arrayAdapter);

                        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                AlertDialog alertDialog = new AlertDialog.Builder(ListeProduitsActivity.this).create();

                                final Map<String, String> produit = lesProduits.get(position);

                                alertDialog.setTitle("Demande de confirmation");
                                alertDialog.setMessage("Vous voulez vraiment supprimer le produit " + produit.get("libelle"));
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Valider",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                RequestQueue queue = Volley.newRequestQueue(ListeProduitsActivity.this);
                                                String url = Constants.API_URL + "/api/produit/delete";

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
                                                                    if (!(new JSONObject(response)).getBoolean("error")) {
                                                                        affichageListeProduits();
                                                                        Toast.makeText(ListeProduitsActivity.this, "Opération réussie", Toast.LENGTH_LONG).show();
                                                                    } else {
                                                                        Toast.makeText(ListeProduitsActivity.this, "Echec de l'opération", Toast.LENGTH_LONG).show();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(ListeProduitsActivity.this, "Erreur, " + error.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }) {
                                                    protected Map<String, String> getParams() {
                                                        Map<String, String> params = new HashMap<>();
                                                        params.put("id", produit.get("id"));
                                                        return params;
                                                    }
                                                };
                                                queue.add(stringRequest);
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });
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
