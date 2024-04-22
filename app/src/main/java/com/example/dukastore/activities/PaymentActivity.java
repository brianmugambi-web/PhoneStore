package com.example.dukastore.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dukastore.R;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    TextView subTotal,discount,shipping,total,paymentTV;
    private double subtotalAmount = 0.0; // Initialize subtotal
    private double discountAmount = 0.0;   // Initialize discount
    private double shippingCost = 0.0;    // Initialize shipping cost
    private double totalAmount = 0.0;      // Initialize total amount

   //paypal
   PaymentButtonContainer paymentButtonContainer;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);



        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);

        paymentTV = findViewById(R.id.idTV);
        paymentButtonContainer=findViewById(R.id.payment_button_container);


        // Check if "totalBill" is present in the Intent
        String receivedTotalBill = getIntent().getStringExtra("totalBill");
        if (!TextUtils.isEmpty(receivedTotalBill)) {
            try {
                subtotalAmount = Double.parseDouble(receivedTotalBill);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid total bill format received.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle single item purchase scenario (get amount from Intent)
            double singleItemAmount = getIntent().getDoubleExtra("amount", 0.0);
            subtotalAmount = singleItemAmount;
        }

        // Calculate discount (replace calculations with your specific logic)
        // Example: 10% discount for orders above $100
        if (subtotalAmount > 1000.0) {
            discountAmount = subtotalAmount * 0.1;
        }

        // Calculate shipping cost (replace calculations with your specific logic)
        // Example: Free shipping for orders above $50, $5 flat rate otherwise
        if (subtotalAmount > 50.0) {
            shippingCost = 0.0;
        } else {
            shippingCost = 5.0;
        }

        // Calculate total amount
        totalAmount = subtotalAmount - discountAmount + shippingCost;

        // Display formatted values on screen
        subTotal.setText("ksh" + subtotalAmount);
        discount.setText("ksh" + discountAmount);
        shipping.setText("ksh" + shippingCost);
        total.setText("ksh" + totalAmount);

        paymentButtonContainer.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value(String.format("%.2f", totalAmount))
                                                        .build()
                                        )
                                        .build()
                        );
                        OrderRequest order = new OrderRequest(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                Toast.makeText(PaymentActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );

    }


        }



