package com.android.mvvm_injection_sample

/**
An Observable is where the data stream comes from, it does some work and emits values.
An Operator has the capability to modify the data from one form to another.
An Observer receives the values.


Adding dependency:
dependencies {
    implementation "io.reactivex.rxjava2:rxjava:2.2.7"
    implementation "io.reactivex.rxjava2:rxandroid:2.1.1"
}
 **/

import android.annotation.SuppressLint
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@SuppressLint("CheckResult")
fun main() {
    exampleJust()
    exampleFrom()
    getObservableFromList(
        listOf(
            "Apple",
            "Orange",
            "Banana"
        )
    ).subscribe { println("Received: $it") }
    exampleInterval()
    exampleFlowable()
    emitterTypesExample()
    asyncExamples()
    transformerExample()
    operatorsExamples()
    disposableExample()
    operatorsExamples()
}


@SuppressLint("CheckResult")
fun exampleJust() {

    /**  just converts the string "Hello Reactive World" to an Observable and the subscribe method receives the value. **/
    Observable.just("Hello Reactive World")
        .subscribe { value -> println(value) }


    /**  We want to know when the item is received if there’s an error and when it completes. **/
    Observable.just("Apple", "Orange", "Banana")
        .subscribe(
            { value -> println("Received: $value") }, // onNext
            { error -> println("Error: $error") },    // onError
            { println("Completed!") }                 // onComplete
        )
}

@SuppressLint("CheckResult")
fun exampleFrom() {

    /** From Examples **/
    Observable.fromArray("Apple", "Orange", "Banana")
        .subscribe { println(it) }


    Observable.fromIterable(listOf("Apple", "Orange", "Banana"))
        .subscribe(
            { value -> println("Received: $value") },      // onNext
            { error -> println("Error: $error") },         // onError
            { println("Completed") }                       // onComplete
        )
}


/**  The function will create an Observable of the type string ,
 * and then it will read each item of the list and perform a check if it’s empty,
 * and if it’s the case an error is supposed to show,
 * otherwise, go to the next until complete. **/

fun getObservableFromList(myList: List<String>) =
    Observable.create<String> { emitter ->
        myList.forEach { kind ->
            if (kind == "") {
                emitter.onError(Exception("There's no value to show"))
            }
            emitter.onNext(kind)
        }
        emitter.onComplete()
    }


/** This function will create an infinite sequence of ticks, separated by the specified duration. **/

@SuppressLint("CheckResult")
fun exampleInterval() {

    Observable.intervalRange(
        10L,     // Start
        5L,      // Count
        0L,      // Initial Delay
        1L,      // Period
        TimeUnit.SECONDS
    ).subscribe { println("Result we just received: $it") }

    Observable.interval(1000, TimeUnit.MILLISECONDS)
        .subscribe { println("Result we just received: $it") }

}


/** Backpressure is the process of handling an emitter that will produce a lot of items very fast.
 *
 *  Instead of an Observable you can use a Flowable which will handle the Backpressure
 *  for you since it takes it into consideration whereas Observable do not.
 **/

@SuppressLint("CheckResult")
fun exampleFlowable() {
    val observable = PublishSubject.create<Int>()

    /**  This code might result in OutOfMemoryException if the device is not top notch. **/

//     observable
//        .observeOn(Schedulers.computation())
//        .subscribe (
//            {
//                println("The Number Is: $it")
//            },
//            {t->
//                print(t.message)
//            }
//        )

    /** In order to handle the backpressure in this situation, we will convert it to Flowable .
    This code is using the Backpressure strategy DROP which will
    drop some of the items in order to preserve memory capabilities.
     **/
    observable
        .toFlowable(BackpressureStrategy.DROP)
        .observeOn(Schedulers.computation())
        .subscribe(
            {
                println("The Number Is: $it")
            },
            { t ->
                print(t.message)
            }
        )

    for (i in 0..1000000) {
        observable.onNext(i)
    }
}


@SuppressLint("CheckResult")
fun emitterTypesExample() {
    /** Flowable
     * It works exactly like an Observable but it supports Backpressure.
     * **/
    Flowable.just("This is a Flowable")
        .subscribe(
            { value -> println("Received: $value") },
            { error -> println("Error: $error") },
            { println("Completed") }
        )


    /** Maybe
     * This class is used when you’d like to return a single optional value.
     * The methods are mutually exclusive, in other words, only one of them is called.
     * If there is an emitted value, it calls onSuccess , if there’s no value,
     * it calls onComplete or if there’s an error, it calls onError .
     * **/
    Maybe.just("This is a Maybe")
        .subscribe(
            { value -> println("Received: $value") },
            { error -> println("Error: $error") },
            { println("Completed") }
        )


    /** Single
     * It’s used when there’s a single value to be returned.
     * If we use this class and there is a value emitted, onSuccess will be called.
     * If there’s no value, onError will be called.
     * **/
    Single.just("This is a Single")
        .subscribe(
            { v -> println("Value is: $v") },
            { e -> println("Error: $e") }
        )


    /** Completable
     * A completable won’t emit any data, what it does is let you know
     * whether the operation was successfully completed.
     * If it was, it calls onComplete and if it wasn’t it calls onError .
     * A common use case of completable is for REST APIs,
     * where successful access will return HTTP 204, and errors can ranger from HTTP 301,
     * HTTP 404, HTTP 500, etc. We might do something with the information.
     * **/

    Completable.create { emitter ->
        emitter.onComplete()
        emitter.onError(Exception())
    }

    /** You can also manually call the methods doOnSubscribe, doOnNext, doOnError, doOnComplete. **/
    Observable.just("Hello")
        .doOnSubscribe { println("Subscribed") }
        .doOnNext { s -> println("Received: $s") }
        .doAfterNext { println("After Receiving") }
        .doOnError { e -> println("Error: $e") }
        .doOnComplete { println("Complete") }
        .doFinally { println("Do Finally!") }
        .doOnDispose { println("Do on Dispose!") }
        .subscribe { println("Subscribe") }
}


/** Schedulers **/

@SuppressLint("CheckResult")
fun asyncExamples() {
    /** subscribeOn
     * you get to decide which thread your Emitter
     * (such as Observable, Flowable, Single, etc) is executed.
     **/

    /**
     * Scheduler.io()
     * This is the most common types of Scheduler that are used.
     * They’re generally used for IO related stuff, such as network requests,
     * file system operations, and it’s backed by a thread pool.
     *
     * A Java Thread Pool represents a group of worker threads
     * that are waiting for the job and reuse many times.
     * **/
    Observable.just("Apple", "Orange", "Banana")
        .subscribeOn(Schedulers.io())
        .subscribe { v -> println("Received: $v") }


    /** Scheduler.computation()
     * This is quite similar to IO as it’s also backed up
     *  by the thread pool, however, the number of threads that can be used is fixed
     *  to the number of cores present in the device. Say you have 2 cores,
     *  it means you’ll get 2 threads, 4 cores, 4 threads, and so on.
     *  **/
    Observable.just("Apple", "Orange", "Banana")
        .subscribeOn(Schedulers.computation())
        .subscribe { v -> println("Received: $v") }


    /** Scheduler.newThread()
     * The name here is self-explanatory,
     * as it will create a new thread for each active Observable.
     * You may want to be careful using this one as if there are a
     * high number of Observable actions it may cause instability.
     *
     * Remember, you can also set how many concurrent threads you want running, so you could do.
     * subscribeOn(Schedulers.newThread(), 8)
     * to have a maximum of 8 concurrent threads.
     * **/
    Observable.just("Apple", "Orange", "Banana")
        .subscribeOn(Schedulers.newThread())
        .subscribe { v -> println("Received: $v") }


    /** Scheduler.single()
     * This Scheduler is backed up by a single thread.
     * No matter how many Observable there are, it will only run in a single thread.
     * Think about it as a replacement for the main thread.
     * **/
    Observable.just("Apple", "Orange", "Banana")
        .subscribeOn(Schedulers.single())
        .subscribe { v -> println("Received: $v") }


    /** Scheduler.trampoline()
     * This will run on whatever the current thread is.
     * If it’s the main thread, it will run the code on the queue of the main thread.
     * Similar to Immediate Scheduler, it also blocks the thread.
     * The trampoline may be used when we have more than one Observable
     * and we want them to execute in order.
     * **/
    Observable.just("Apple", "Orange", "Banana")
        .subscribeOn(Schedulers.trampoline())
        .subscribe { v -> println("Received: $v") }


    /** Executor Scheduler
     * This is a custom IO Scheduler, where we can set a custom pool of threads by
     * specifying how many threads we want in that pool.
     * It can be used in a scenario where the number of Observable
     * can be huge for IO thread pool.
     * **/
    val executor = Executors.newFixedThreadPool(10)
    val pooledScheduler = Schedulers.from(executor)

    Observable.just("Apple", "Orange", "Banana")
        .subscribeOn(pooledScheduler)
        .subscribe { v -> println("Received: $v") }


    /** AndroidSchedulers.mainThread()
     * Calling this on observeOn will bring the thread back to the Main UI thread,
     * and thus make any modification you need to your UI.
     * **/

    /** observeOn
     * The method subscribeOn() will instruct the source Observable
     * which thread to emit the items on and push the emissions on our Observer.
     * But if it finds an observeOn() in the chain, it switches the emissions
     * using the selected scheduler for the remaining operation.
     * **/
    Observable.just("Apple", "Orange", "Banana")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { v -> println("Received: $v") }
}


/** Transformers
 * With a transformer, we can avoid repeating some code by
 * applying the most commonly used chains among your Observable,
 * we’ll be chaining subscribeOn and observeOn to a couple of Observable below.
 * **/

@SuppressLint("CheckResult")
fun transformerExample() {
    Observable.just("Apple", "Orange", "Banana")
        .compose(applyObservableAsync())
        .subscribe { v -> println("The First Observable Received: $v") }

    Observable.just("Water", "Fire", "Wood")
        .compose(applyObservableAsync())
        .subscribe { v -> println("The Second Observable Received: $v") }
}

fun <T> applyObservableAsync(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}


/** Operators **/

@SuppressLint("CheckResult")
fun operatorsExamples() {

    /** map()
     * Transforms values emitted by an Observable stream into a single value.
     * **/
    Observable.just("Water", "Fire", "Wood")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map { m -> m + " 2" }
        .subscribe { v -> println("Received: $v") }


    /** flatMap()
     * Unlike the map() operator, the flatMap() will transform each value
     * in an Observable stream into another Observable,
     * which are then merged into the output Observable after processing.
     * Let’s do a visual representation of the difference between those:
     * **/
//     map():
//     input: Observable<T>
//     transformation: (T -> R)
//     output: Observable<R>
//
//     flatMap():
//     input: Observable<T>
//     transformation: (T -> Observable<R>)
//     output: Observable<R>

    Observable.just("Water", "Fire", "Wood")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap { m ->
            Observable.just(m + " 2")
                .subscribeOn(Schedulers.io())
        }
        .subscribe { v -> println("Received: $v") }


    /** zip()
     * The zip() operator will combine the values of multiple
     * Observable together through a specific function.
     * **/
    Observable.zip(
        Observable.just(
            "Roses", "Sunflowers", "Leaves", "Clouds", "Violets", "Plastics"
        ),
        Observable.just(
            "Red", "Yellow", "Green", "White or Grey", "Purple"
        ),

        BiFunction<String, String, String> { type, color ->
            "$type are $color"
        }
    ).subscribe { v -> println("Received: $v") }


    /** concat()
     * As the name suggests, concat() will concatenate (join together) two or more Observable.
     * **/
    val test1 = Observable.just("Apple", "Orange", "Banana")
    val test2 = Observable.just("Microsoft", "Google")
    val test3 = Observable.just("Grass", "Tree", "Flower", "Sunflower")

    Observable.concat(test1, test2, test3)
        .subscribe { x -> println("Received: " + x) }


    /** merge()
     * works similarly to concat(), except merge will intercalate
     * the emissions from both Observable , whereas concat()
     * will wait for one to finish to show another.
     **/
    Observable.merge(
        Observable.interval(250, TimeUnit.MILLISECONDS).map { i -> "Apple" },
        Observable.interval(150, TimeUnit.MILLISECONDS).map { i -> "Orange" })
        .take(10)
        .subscribe { v -> println("Received: $v") }


    /** filter()
     * Filter the values according to a set condition.
     * **/
    Observable.just(2, 30, 22, 5, 60, 1)
        .filter { x -> x < 10 }
        .subscribe { x -> println("Received: $x") }


    /** repeat()
     * This operator will repeat the emission of the values however many times we may need.
     * **/
    Observable.just("Apple", "Orange", "Banana")
        .repeat(2)
        .subscribe { v -> println("Received: $v") }


    /** take()
     * The take() operator is meant to grab however many emissions you’d like.
     * **/
    Observable.just("Apple", "Orange", "Banana")
        .take(2)
        .subscribe { v -> println("Received: $v") }
}


/** Disposable
 * A Disposable will release memory, resources, and threads used by an Observable.
 * So, the main purpose of disposable is to free up system resources and make your app more stable.
 *
 * Remember when dealing with dispose() you should use it when you know
 * for certain that your thread has finished. The most elegant way of
 * doing it is disposing of your Observable through the activity lifecycle,
 * especially onDestroy.
 * **/
fun disposableExample() {
    val compositeDisposable = CompositeDisposable()

    val observableOne = Observable.just("Tree")
        .subscribe { v -> println("Received: $v") }
    val observableTwo = Observable.just("Blue")
        .subscribe { v -> println("Received: $v") }

    compositeDisposable.add(observableOne)
    compositeDisposable.add(observableTwo)
    compositeDisposable.clear()


}
//class MainActivity : AppCompatActivity() {
//    val source = PublishSubject.create<String>()
//    val disposables = CompositeDisposable()
//    var count = 0
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        disposables.add(
//            source.subscribe(
//                { v -> textView.text = v },
//                { e -> textView.text = "Error: $e" }
//            )
//        )
//        button.setOnClickListener {
//            source.onNext("Counter: ${++count}")
//            if (count == 10) {
//                source.onComplete()
//            }
//        }
//    }
//    override fun onDestroy() {
//        super.onDestroy()
//        disposables.clear()
//    }
//}
