package com.android.mvvm_injection_sample.rx_kotlin_sample.extensions

import android.util.Log.d
import com.android.mvvm_injection_sample.rx_kotlin_sample.models.User
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.Exception

fun createObservable(): Observable<Int> {
    return Observable.create { emitter ->
        try {
            if (!emitter.isDisposed) {
                for (i in 0..100) {
                    emitter.onNext(i)
                }
                emitter.onComplete()
            }
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }
}

fun observer(): Observer<Int> {
    return object : Observer<Int> {

        override fun onSubscribe(d: Disposable) {
            d("observer", "onSubscribe")
        }

        override fun onNext(t: Int) {
            d("observer", "onNext $t")
        }

        override fun onError(e: Throwable) {
            d("observer", "onError $e")

        }

        override fun onComplete() {
            d("observer", "onComplete ")
        }
    }
}


fun createSingleObservable(): Single<List<User>> {
    return Single.create { emitter ->
        try {
            if (!emitter.isDisposed) {
                emitter.onSuccess(mUserList)
            }
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }
}

fun singleObserver(): SingleObserver<List<User>> {
    return object : SingleObserver<List<User>> {

        override fun onSubscribe(d: Disposable) {
            d("singleObserver", "onSubscribe")
        }

        override fun onError(e: Throwable) {
            d("singleObserver", "onError $e")

        }

        override fun onSuccess(t: List<User>) {
            t.forEach {
                d("singleObserver", "onSuccess $it")
            }
        }

    }
}

fun createMaybeObservable(): Maybe<MutableList<User>> {
    return Maybe.just(mUserList)
}

fun maybeObserver(): MaybeObserver<List<User>> {
    return object : MaybeObserver<List<User>> {
        override fun onSubscribe(d: Disposable) {
            d("maybeObservable", "onSubscribe")
        }

        override fun onSuccess(t: List<User>) {
            d("maybeObservable", "onSuccess $t")
        }

        override fun onError(e: Throwable) {
            d("maybeObservable", "onError $e")

        }

        override fun onComplete() {
            d("maybeObservable", "onComplete ")
        }
    }
}


fun createCompletableObservable(): Completable {
    return Completable.create { emitter ->
        try {
            if (!emitter.isDisposed) {
                getLocation()
                emitter.onComplete()
            }
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }
}

fun getLocation() {
    Thread.sleep(2000)
    throw Exception("Exception")
    d("location", "Longitude 121")
}

fun completableObserver(): CompletableObserver {
    return object : CompletableObserver {
        override fun onSubscribe(d: Disposable) {
            d("completableObserver", "onSubscribe $d ")

        }

        override fun onComplete() {
            d("completableObserver", "onComplete ")
        }

        override fun onError(e: Throwable) {
            d("completableObserver", "onError $e")
        }
    }
}