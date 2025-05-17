/*ackage com.example.pfe;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CoursManagementActivity extends AppCompatActivity {

    ListView listView;
    DatabaseReference reference;
    ArrayList<Cour> coursList;
    CoursAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours_management);

        listView = findViewById(R.id.cours_list_view);
        coursList = new ArrayList<>();
        adapter = new CoursAdapter(this, coursList);
        listView.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference("uploads").child("cours");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coursList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Cour cours = ds.getValue(Cour.class);
                    if (cours != null) {
                        coursList.add(cours);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CoursManagementActivity.this, "Erreur de chargement des cours", Toast.LENGTH_SHORT).show();
            }
        });
    }
}*/
