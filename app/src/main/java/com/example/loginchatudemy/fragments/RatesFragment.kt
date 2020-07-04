 package com.example.loginchatudemy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginchatudemy.R
import com.example.loginchatudemy.RateDialog.RateDialog
import com.example.loginchatudemy.adapter.RatesAdapter
import com.example.loginchatudemy.models.NewRateEvent
import com.example.loginchatudemy.models.Rate
import com.example.loginchatudemy.toast
import com.example.loginchatudemy.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_rates.view.*


 class RatesFragment : Fragment() {
    //INICIO VARIABLES
    private lateinit var _view: View
    private lateinit var adapter: RatesAdapter
    private val ratesList: ArrayList<Rate> = ArrayList()
     private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
     private lateinit var currentUser: FirebaseUser
     private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
     private lateinit var ratesDBRef: CollectionReference
     private var ratesSubscription: ListenerRegistration? = null
     private lateinit var rateBusListener: Disposable
    //FIN VARIABLES
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _view =  inflater.inflate(R.layout.fragment_rates, container, false)
        sepUpRecyclerView()
        setUpFab()
        setUpRatesDB()
        setUpCurrentUser()
        subscribeToNewRating()
        return _view
    }
     //INICIO METODOS
     private fun setUpRatesDB(){
         ratesDBRef = store.collection("rates")
     }
     private fun setUpCurrentUser(){
         currentUser = mAuth.currentUser!!
     }
     private fun sepUpRecyclerView(){
         val layoutManager = LinearLayoutManager(context)
         adapter = RatesAdapter(ratesList)
         _view.recyclerView.setHasFixedSize(true)
         _view.recyclerView.layoutManager = layoutManager
         _view.recyclerView.itemAnimator = DefaultItemAnimator()
         _view.recyclerView.adapter = adapter
     }
     private fun setUpFab() {
         _view.fabRating.setOnClickListener { fragmentManager?.let { it1 -> RateDialog().show(it1, "") } }
     }
     private fun saveRate(rate: Rate){
         val newRating = HashMap<String, Any>()
         newRating["text"] = rate.text
         newRating["rate"] = rate.rate
         newRating["createAt"] = rate.createAt
         newRating["profileImgUrl"] = rate.profileImgUrl
         ratesDBRef.add(newRating)
             .addOnCompleteListener { activity!!.toast("Opinion enviada") }
             .addOnFailureListener { activity!!.toast("Error al enviar, intenta de nuevo")}
     }
     private fun subscribeToRating(){}
     private fun subscribeToNewRating(){
         RxBus.listen(NewRateEvent::class.java).subscribe{saveRate(it.rate)}
     }
     //FIN METODOS
}
