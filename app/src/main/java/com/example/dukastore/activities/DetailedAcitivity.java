package com.example.dukastore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dukastore.R;
import com.example.dukastore.models.RecommendModel;
import com.example.dukastore.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DetailedAcitivity extends AppCompatActivity {
    ImageView detailedImg;
    TextView rating,name,description,price,quantity;
    Button  addToCart,buyNow;
    ImageView addItems,removeItems;
    int totalQuantity=1;
    int  totalPrice=0;
    //recommend products
    RecommendModel recommendModel=null;
    //show All
    ShowAllModel showAllModel=null;

    private FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_acitivity);

        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        final Object obj=getIntent().getSerializableExtra("detailed");
        if(obj instanceof RecommendModel){
            recommendModel=(RecommendModel) obj;
        }else if(obj instanceof ShowAllModel){
            showAllModel=(ShowAllModel) obj;
        }

        detailedImg=findViewById(R.id.detailed_img);
        quantity=findViewById(R.id.quantity);
        name=findViewById(R.id.detailed_name);
        description=findViewById(R.id.detailed_desc);
        price=findViewById(R.id.detailed_price);
        rating=findViewById(R.id.detailed_rating_bar);
        addToCart=findViewById(R.id.add_to_cart);
        buyNow=findViewById(R.id.buy_now);
        addItems=findViewById(R.id.add_item);
        removeItems=findViewById(R.id.remove_item);


        //recommend products
        if (recommendModel != null) {
            Glide.with(getApplicationContext()).load(recommendModel.getImg_url()).into(detailedImg);
            name.setText(recommendModel.getName());
            price.setText(recommendModel.getPrice());
            description.setText(recommendModel.getDescription());
            String priceString = recommendModel.getPrice();
            int price = 0; // Default price value

            try {
                // Try parsing the priceString to an integer
                price = Integer.parseInt(priceString);
            } catch (NumberFormatException e) {
                // Handle the case where priceString is not a valid integer
                e.printStackTrace(); // Log the exception for debugging
                // You can also show a toast message or set a default price here
            }

            totalPrice = price * totalQuantity;
        }
        // Show all products
        if(showAllModel!=null){
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            price.setText(showAllModel.getPrice());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            int price = Integer.parseInt(showAllModel.getPrice());
            totalPrice=price*totalQuantity;

        }
        //Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to move to AddAddressActivity
                Intent intent = new Intent(DetailedAcitivity.this, AddressActivity.class);

                // Pass the relevant model information
                if (recommendModel != null) {
                    intent.putExtra("item", recommendModel);
                } else if (showAllModel != null) {
                    intent.putExtra("item", showAllModel);
                } else {
                    // Handle the case where neither model is available
                    Log.w("DetailedActivity", "No item available to pass!");
                    return; // Prevent proceeding without a valid item
                }

                // Pass the total price
                intent.putExtra("totalPrice", totalPrice);

                startActivity(intent);
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }

            private void addToCart() {
                String saveCurrentTime, saveCurrentDate;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                saveCurrentDate = currentDate.format(new Date());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calForDate.getTime());

                final HashMap<String, Object> cartMap = new HashMap<>();


                cartMap.put("ProductName", name.getText().toString());
                cartMap.put("ProductPrice",price.getText().toString());  // Store price per item
                cartMap.put("currentTime", saveCurrentTime);
                cartMap.put("Date", saveCurrentDate);
                cartMap.put("totalQuantity", quantity.getText().toString());
                // Calculate total price based on stored price and quantity
                int totalPrice = Integer.parseInt(price.getText().toString()) * Integer.parseInt(quantity.getText().toString());
                cartMap.put("totalPrice", totalPrice);

                firestore.collection("AddToCart1").document(auth.getCurrentUser().getUid())
                        .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(DetailedAcitivity.this, "Added To Cart,Continue to shop", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity < 100000000) {
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    // Calculate total price based on the appropriate model price
                    int price = 0;
                    if (recommendModel != null) {
                        price = Integer.parseInt(recommendModel.getPrice());
                    } else if (showAllModel != null) {
                        price = Integer.parseInt(showAllModel.getPrice());
                    } else {
                        // Handle potential case where neither model has a price
                        Log.w("DetailActivity", "No price available for either model!");
                    }

                    totalPrice = price * totalQuantity;

// Update the detailed_price TextView with formatted total price
                    TextView detailedPrice = findViewById(R.id.detailed_price);

// Format totalPrice as an integer (%d format specifier for integers)
                    detailedPrice.setText(String.format("%d", totalPrice)); // Format as an integer
                    // If you need floating-point format:
                    // detailedPrice.setText(String.format("%.2f", formattedTotalPrice));
                }
            }
        });
        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 1) {
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));

                    // Calculate total price based on the appropriate model price
                    int price = 0;
                    if (recommendModel != null) {
                        price = Integer.parseInt(recommendModel.getPrice());
                    } else if (showAllModel != null) {
                        price = Integer.parseInt(showAllModel.getPrice());
                    } else {
                        // Handle potential case where neither model has a price
                        Log.w("DetailActivity", "No price available for either model!");
                    }

                    totalPrice = price * totalQuantity;

                    // Update the detailed_price TextView with formatted total price
                    TextView detailedPrice = (TextView) findViewById(R.id.detailed_price);
                    detailedPrice.setText(String.format("%d", totalPrice)); // Format as an integer
                }
            }
        });


        }
}