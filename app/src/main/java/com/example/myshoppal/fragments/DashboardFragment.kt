package com.example.myshoppal.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.activities.SettingsActivity
import com.example.myshoppal.adapter.DashboardListAdapter
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.models.Product
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if use option menu in frgment that must be included
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionSetting ->{
                startActivity(Intent(activity,SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getDashBoardItems()
    }

    fun successDashboardItemList(dashBoardItemList:ArrayList<Product>){
        hideProgressDialog()

        if (dashBoardItemList.size >0){
            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_yet.visibility = View.GONE

            rv_dashboard_items.layoutManager = GridLayoutManager(activity,2)
            rv_dashboard_items.setHasFixedSize(true)

            val adapter = DashboardListAdapter(requireActivity(),dashBoardItemList)
            rv_dashboard_items.adapter = adapter


        }else{
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_yet.visibility = View.VISIBLE
        }
    }

    private fun getDashBoardItems(){
        showProgressDialog("Please wait")
        FireStoreClass().getDashboardItemsList(this)
    }



}