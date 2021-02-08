package com.ngangavictor.testsubscription

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.billingclient.api.*
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ngangavictor.testsubscription.adapters.ProductsAdapter
import com.ngangavictor.testsubscription.databinding.ActivityMainBinding
import com.ngangavictor.testsubscription.dialog.SKUDialog

class MainActivity : AppCompatActivity(), PurchasesUpdatedListener {

    companion object {
        lateinit var billingClient: BillingClient
    }

    lateinit var binding: ActivityMainBinding

    private val skuList = listOf("basic_subscription")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        Glide.with(this)
            .load(R.drawable.chicken)
            .circleCrop()
            .into(binding.imageView)

        setupBillingClient()

        binding.extendedFab.setOnClickListener { onLoadProductsClicked() }

    }

    private fun setupBillingClient() {
        billingClient = BillingClient
            .newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(p0: BillingResult) {
                if (p0.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.e("BILLING","| startConnection | RESULT OK")
//                    println("BILLING | startConnection | RESULT OK")
                } else {
                    Log.e("BILLING","| startConnection | RESULT ${p0.responseCode}")
//                    println("BILLING | startConnection | RESULT: $p0")
                }
            }

            override fun onBillingServiceDisconnected() {
//                println("BILLING | onBillingServiceDisconnected | DISCONNECTED")
                Log.e("BILLING","| onBillingServiceDisconnected | DISCONNECTED")
            }

        })
    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        Log.e("onPurchasesUpdated","${p0.responseCode}")
//        println("onPurchasesUpdated: ${p0.responseCode}")
    }

    private fun onLoadProductsClicked() {
        if (billingClient.isReady) {
            val params = SkuDetailsParams
                .newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.SUBS)
                .build()
            billingClient.querySkuDetailsAsync(params) { responseCode, skuDetailsList ->
                if (responseCode.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.e("querySkuDetailsAsync","responseCode: ${responseCode.responseCode}")
//                    println("querySkuDetailsAsync, responseCode: $responseCode")
                    Log.e("LIST SIZE",skuDetailsList?.size.toString())
                    if (skuDetailsList != null) {
                        //load dialog
                            val skuDialog=SKUDialog(skuDetailsList)
                        skuDialog.show(supportFragmentManager,"sku dialog")
                    }
                } else {
                    Log.e("querySkuDetailsAsync","responseCode: ${responseCode.responseCode}")
//                    println("Can't querySkuDetailsAsync, responseCode: $responseCode")
                }
            }
        } else {
            Log.e("MSG","Billing Client not ready")
//            println("Billing Client not ready")
        }
    }

}