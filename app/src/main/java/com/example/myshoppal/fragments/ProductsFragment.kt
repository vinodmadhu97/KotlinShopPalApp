package com.example.myshoppal.fragments


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle

import android.view.*
import android.widget.TextView
import android.widget.Toast


import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.activities.AddProductActivity
import com.example.myshoppal.adapter.ProductListAdapter
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.models.Product
import kotlinx.android.synthetic.main.fragment_products.*


// TODO Step 1: Rename the HomeFragment as ProductsFragment as well rename the xml files accordingly.
/**
 * A products fragment.
 */
class ProductsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_products, container, false)
        val textView: TextView = root.findViewById(R.id.tv_no_product_yet)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_product_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_add_product ->{
                startActivity(Intent(activity,AddProductActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getProductsFromFireStore()
    }

    private fun getProductsFromFireStore(){
        showProgressDialog("Please wait!")
        FireStoreClass().getProductList(this)
    }

    fun successProductListFromFireStore(productList : ArrayList<Product>){
        hideProgressDialog()

        if (productList.size > 0){
            rv_products_items.visibility = View.VISIBLE
            tv_no_product_yet.visibility = View.GONE

            rv_products_items.layoutManager = LinearLayoutManager(activity)
            rv_products_items.setHasFixedSize(true)

            val productAdapter = ProductListAdapter(requireActivity(),productList,this)
            rv_products_items.adapter = productAdapter

        }else{
            rv_products_items.visibility = View.GONE
            tv_no_product_yet.visibility = View.VISIBLE
        }
    }

    fun deleteProductItem(productId: String){
        //Toast.makeText(requireActivity(),"$productId deleted",Toast.LENGTH_LONG).show()
        showAlertDialogToDeleteProduct(productId)
    }

    fun productDeleteSuccess(){
        hideProgressDialog()
        Toast.makeText(requireActivity(),"Product is deleted",Toast.LENGTH_LONG).show()

        //RELOADING THE PAGE
        getProductsFromFireStore()
    }

    private fun showAlertDialogToDeleteProduct(productId : String){

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Do your want to delete ?")
        builder.setIcon(R.drawable.ic_baseline_cancel_24)

        builder.setPositiveButton("Yes"){dialogInterface,_ ->
            FireStoreClass().deleteProductItem(this,productId)
            dialogInterface.dismiss()
            showProgressDialog("Deleting!")
        }

        builder.setNegativeButton("No"){dialogInterface,_ ->
            dialogInterface.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }
}