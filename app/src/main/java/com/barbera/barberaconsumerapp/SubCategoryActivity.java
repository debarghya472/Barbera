package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryActivity extends AppCompatActivity {
    private ListView sublistview;
    public static ProgressBar progressBarOnSubCategory;
    private String Category;
    private String CategoryIMage;
    private ImageView serviceimage;
    public static String salontype;
    private String collecton;
    private List<String> subCategoryList=new ArrayList<>();
    private SubCategoryAdapter subCategoryAdapter;
    private String serViceType;
    private String subCategoryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_service_activity);

        Intent intent=getIntent();
        Category=intent.getStringExtra("Category");
        CategoryIMage=intent.getStringExtra("CategoryIMage");
        salontype=intent.getStringExtra("SalonType");
        collecton=intent.getStringExtra("Collection");
        serViceType=intent.getStringExtra("ServiceType");
        sublistview=(ListView) findViewById(R.id.new_service_recycler_view);
        TextView title=(TextView)findViewById(R.id.new_service_title);
        progressBarOnSubCategory=(ProgressBar)findViewById(R.id.new_service_progress_bar);
        ImageView cart=(ImageView)findViewById(R.id.new_service_cart);
        serviceimage=(ImageView)findViewById(R.id.new_service_image);
        subCategoryAdapter=new SubCategoryAdapter(subCategoryList);
        LinearLayout cartAndBookLayout=(LinearLayout)findViewById(R.id.addANdbookLayout);

        cartAndBookLayout.setVisibility(View.GONE);
        cart.setVisibility(View.GONE);


      /* cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
                else {
                    startActivity(new Intent(SubCategoryActivity.this, CartActivity.class));
                }
            }
        });*/

        title.setText(Category);

        Glide.with(getApplicationContext()).load(CategoryIMage)
                .apply(new RequestOptions().placeholder(R.drawable.logo)).into(serviceimage);

        if(subCategoryList.size()==0){
            progressBarOnSubCategory.setVisibility(View.VISIBLE);
            FirebaseFirestore.getInstance().collection(collecton).document(Category).collection("SubCategories").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                                    subCategoryList.add(documentSnapshot.getId());
                                }
                                sublistview.setAdapter(subCategoryAdapter);
                                progressBarOnSubCategory.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
        sublistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubCategoryActivity.this, ParlourActivity.class);
                intent.putExtra("Category", Category);
                intent.putExtra("CategoryIMage",CategoryIMage);
                intent.putExtra("SalonType",salontype);
                intent.putExtra("Collection",collecton);
                intent.putExtra("SubCategDoc",subCategoryList.get(position));
                intent.putExtra("ServiceType",serViceType);
                startActivity(intent);
            }
        });



    }
}