package com.example.loginchatudemy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginchatudemy.R
import com.example.loginchatudemy.RateDialog.RateDialog
import com.example.loginchatudemy.adapter.RatesAdapter
import com.example.loginchatudemy.models.NewRateEvent
import com.example.loginchatudemy.models.Rate
import com.example.loginchatudemy.toast
import com.example.loginchatudemy.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_rates.*
import kotlinx.android.synthetic.main.fragment_rates.view.*
import kotlinx.android.synthetic.main.fragment_rates.view.fabRating
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class RatesFragment : Fragment() {
    //INICIO VARIABLES
    private lateinit var _view: View
    private lateinit var adapter: RatesAdapter
    private lateinit var scrollListener: RecyclerView.OnScrollListener
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
        //INICIO LLAMADO DE METODOS
        sepUpRecyclerView()
        setUpFab()
        setUpRatesDB()
        setUpCurrentUser()
        subscribeToRating()
        subscribeToNewRating()
        //FIN LLAMADO DE METODOS
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
        scrollListener = object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && _view.fabRating.isShown){
                    fabRating.hide()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    _view.fabRating.show()
                }
            }
        }
        _view.recyclerView.addOnScrollListener(scrollListener)
    }
    private fun setUpFab() {
        _view.fabRating.setOnClickListener { fragmentManager?.let { it1 -> RateDialog().show(it1, "") } }
    }
    private fun hasUserRated(rates: ArrayList<Rate>): Boolean{
        var result = false
        rates.forEach{
            if (it.userId == currentUser.uid){
                result = true
            }
        }
        return result
    }
    private fun removeFABIfRated(rated: Boolean){
        if (rated){
            _view.fabRating.hide()
            _view.recyclerView.removeOnScrollListener(scrollListener)
        }
    }
    private fun saveRate(rate: Rate){
        val newRating = HashMap<String, Any>()
        newRating["userId"] = rate.userId
        newRating["text"] = rate.text
        newRating["rate"] = rate.rate
        newRating["createAt"] = rate.createAt
        newRating["profileImgUrl"] = rate.profileImgUrl
        ratesDBRef.add(newRating)
            .addOnCompleteListener { activity!!.toast("Opinion enviada") }
            .addOnFailureListener { activity!!.toast("Error al enviar, intenta de nuevo")}
    }
    private fun subscribeToRating(){
        ratesSubscription = ratesDBRef
            .orderBy("createAt", Query.Direction.DESCENDING)
            .addSnapshotListener(object: EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot>{
                override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                    exception?.let {
                        activity!!.toast("Exception!")
                        return
                    }
                    snapshot?. let {
                        ratesList.clear()
                        val rates = it.toObjects(Rate::class.java)
                        ratesList.addAll(rates)
                        removeFABIfRated(hasUserRated(ratesList))
                        adapter.notifyDataSetChanged()
                        _view.recyclerView.smoothScrollToPosition(0)
                    }
                }
            })
    }
    private fun subscribeToNewRating(){
        rateBusListener = RxBus.listen(NewRateEvent::class.java).subscribe{saveRate(it.rate)}
    }
    override fun onDestroyView() {
        _view.recyclerView.removeOnScrollListener(scrollListener)
        rateBusListener.dispose()
        ratesSubscription?.remove()
        super.onDestroyView()
    }
    //FIN METODOS
}