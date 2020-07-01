package com.example.loginchatudemy.utils

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
//CREACION DEL EVENT BUS REACTIVO
object RxBus{
    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any){
        publisher.onNext(event)
    }

    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}