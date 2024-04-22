package com.example.dukastore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.dukastore.R;
import com.example.dukastore.databinding.ActivityMainBinding;
import com.example.dukastore.fragments.HomeFragment;
import com.example.dukastore.fragments.ProfileFragment;
import com.example.dukastore.fragments.cartFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;

    private cartFragment  cartFragment1;

    TextView headerName,headerEmail;

    com.example.dukastore.models.userModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeFragment = new HomeFragment();
        profileFragment=new ProfileFragment();
        cartFragment1=new cartFragment();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        drawerLayout = binding.drawerLayout;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setBackground(null);
//
//        // Set listener for item selection
//       bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//           @Override
//           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//               int itemId= item.getItemId();
//               if(itemId==R.id.bottom_home){
//                   homeFragment=new HomeFragment();
//               }
//               if(itemId==R.id.bottom_profile){
//                   profileFragment=new ProfileFragment();
//               }
//               if(itemId==R.id.botom_cart){
//                   cartFragment1=new cartFragment();
//               }
//               return false;
//           }
//       });




        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.cartFragment, R.id.Log_out)
                .setOpenableLayout(drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView=navigationView.getHeaderView(0);
        headerName=headerView.findViewById(R.id.nav_header_name);
        headerEmail=headerView.findViewById(R.id.nav_header_email);
        CircleImageView headerImg=headerView.findViewById(R.id.nav_header_img);


        database.getReference().child("users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModel = snapshot.getValue(com.example.dukastore.models.userModel.class);
                        if (userModel != null) {
                            // Check if the activity is still valid before updating UI
                            if (!isFinishing() && !isDestroyed()) {
                                headerName.setText(userModel.getName());
                                headerEmail.setText(userModel.getEmail());

                                Glide.with(MainActivity.this)
                                        .load(userModel.getProfileImg())
                                        .into(headerImg);
                            }
                        } else {
                            // Handle the case where userModel is null
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event
                    }
                });



        navigationView.bringToFront();
         navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.Log_out) {
                logoutMenu();
                return true;
            }
            if (id == R.id.nav_home) {
                navController.navigate(R.id.nav_home);

                return true;
            }
            if (id == R.id.nav_profile) {
                navController.navigate(R.id.nav_profile);

                return true;
            }
            if(id==R.id.cartFragment){
                navController.navigate(R.id.cartFragment);
                return true;
            }if(id==R.id.share){
                shareApp();
                return true;
            }
            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareMessage = "Check out this cool app!";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logoutMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Perform logout actions here, such as clearing session data, etc.
            finish(); // Finish the activity to logout
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
