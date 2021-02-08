package com.ngangavictor.testsubscription.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.SkuDetails
import com.ngangavictor.testsubscription.MainActivity.Companion.billingClient
import com.ngangavictor.testsubscription.R
import com.ngangavictor.testsubscription.adapters.ProductsAdapter
import com.ngangavictor.testsubscription.databinding.DialogSkuBinding

class SKUDialog(var skuDetailsList: List<SkuDetails>):DialogFragment() {

    lateinit var binding:DialogSkuBinding

    lateinit var productsAdapter: ProductsAdapter

//    lateinit var billingClient: BillingClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=DataBindingUtil.inflate(inflater, R.layout.dialog_sku,container,false)

        val layoutManager=LinearLayoutManager(requireContext())
        layoutManager.orientation=LinearLayoutManager.VERTICAL
        binding.recyclerViewSKU.layoutManager=layoutManager

        initProductAdapter(skuDetailsList)

        binding.buttonClose.setOnClickListener {
            dialog?.dismiss()
        }

        return binding.root
    }

    private fun initProductAdapter(skuDetailsList: List<SkuDetails>) {
//        Log.e("LIST SIZE",skuDetailsList.size.toString())
        productsAdapter = ProductsAdapter(skuDetailsList) {
            val billingFlowParams = BillingFlowParams
                .newBuilder()
                .setSkuDetails(it)
                .build()
            billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
        }
        binding.recyclerViewSKU.adapter = productsAdapter
    }
}