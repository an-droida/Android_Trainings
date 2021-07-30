package com.android.mvvm_injection_sample.rx_kotlin_sample

import android.annotation.SuppressLint
import android.util.Log
import com.android.mvvm_injection_sample.rx_kotlin_sample.extensions.*
import io.reactivex.rxjava3.core.Observable

fun main() {
    mappingAndGroupOperators()
    mergeOperators()
    concatOperators()
    startWithOperators()
    zipOperator()
    zipOperatorsTwo()
    createObservables()
    createSingleObservables()
    createMaybeObservables()
    createCompletableObs()
}

@SuppressLint("CheckResult")
fun mappingAndGroupOperators() {
    flatMapOperator()
        .flatMap {
            getUserProfile(it.id!!)
        }
        .subscribe(
            {
                Log.d("flatMapOperator", "onNext : $it")
            },
            {
                Log.d("flatMapOperator", "onError : $it")
            },
            {
                Log.d("flatMapOperator", "onComplete")
            }
        )
    flatMapOperatorTwo()
        .flatMap {
            Observable.fromIterable(it)
        }
        .flatMap {
            getUserProfile(it.id!!)
        }
        .subscribe(
            {
                Log.d("flatMapOperatorTwo", "onNext : $it")
            },
            {
                Log.d("flatMapOperatorTwo", "onError : $it")
            },
            {
                Log.d("flatMapOperatorTwo", "onComplete")
            }
        )

    groupByOperator()
        .groupBy {
            it.age
        }
//            .filter {
//                it.key == 22
//            }
        .subscribe(
            { group ->
                group.subscribe(
                    {
                        Log.d("groupByOperator", "Key : ${group.key} - value : $it")
                    },
                    {
                        Log.d("groupByOperator", "onError : $it")
                    }
                )
                Log.d("groupByOperator", "onNext : $group")
            },
            {
                Log.d("groupByOperator", "onError : $it")
            },
            {
                Log.d("groupByOperator", "onComplete")
            }
        )

    groupByOperator()
        .groupBy {
            it.age
        }
        .flatMapSingle {
            it.toList()
        }
        .subscribe(
            {
                Log.d("groupByOperator", "onNext : $it")
            },
            {
                Log.d("groupByOperator", "onError : $it")
            },
            {
                Log.d("groupByOperator", "onComplete")
            }
        )
}

@SuppressLint("CheckResult")
private fun mergeOperators() {
    mergeOperator()
        .subscribe(
            {
                Log.d("mergeOperator", "onNext : $it")
            },
            {
                Log.d("mergeOperator", "onError : $it")
            },
            {
                Log.d("mergeOperator", "onComplete")
            }
        )
}

@SuppressLint("CheckResult")
private fun concatOperators() {
    concatOperator()
        .subscribe(
            {
                Log.d("concatOperators", "onNext : $it")
            },
            {
                Log.d("concatOperators", "onError : $it")
            },
            {
                Log.d("concatOperators", "onComplete")
            }
        )
}

@SuppressLint("CheckResult")
private fun startWithOperators() {
    startWithOperator()
        .subscribe(
            {
                Log.d("startWithOperators", "onNext : $it")
            },
            {
                Log.d("startWithOperators", "onError : $it")
            },
            {
                Log.d("startWithOperators", "onComplete")
            }
        )
}

@SuppressLint("CheckResult")
private fun zipOperator() {
    zipOperators()
        .subscribe(
            {
                Log.d("zipOperator", "onNext : $it")
            },
            {
                Log.d("zipOperator", "onError : $it")
            },
            {
                Log.d("zipOperator", "onComplete")
            }
        )
}

@SuppressLint("CheckResult")
private fun zipOperatorsTwo() {
    zipOperatorTwo()
        .subscribe({
            it.forEach {
                Log.d("zipOperatorTwo", "onNext : $it")
            }
        },
            {
                Log.d("zipOperatorTwo", "onError : $it")
            },
            {
                Log.d("zipOperatorTwo", "onComplete")
            }

        )
}


private fun createObservables() {
    createObservable().subscribe(observer())
}

private fun createSingleObservables() {
    createSingleObservable().subscribe(singleObserver())
}

private fun createMaybeObservables() {
    createMaybeObservable().subscribe(maybeObserver())
}

private fun createCompletableObs() {
    createCompletableObservable().subscribe(completableObserver())
}