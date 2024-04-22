package com.example.dukastore.activities;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

public class App extends Application {



    @Override
    public void onCreate() {
        super.onCreate();

        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                "AZTNEfY4vF41blEtNXwYDfNAyP5zu5qPwZUlU8Z855sNZUa1hYvdi2deWBdBFnWjewl198zcLm0-6u6X",
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                "com.example.dukastore://paypalpay"


        ));
    }
}
