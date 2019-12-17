package com.example.kotlinproject1.Game
import android.graphics.Color
import android.os.CountDownTimer
import android.os.Handler
import android.widget.Toast
import com.example.kotlinproject1.R
import com.example.kotlinproject1.Game.CardInterface.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class CardPresenter (view: View, level: Int =8) : Presenter {



    private var mView: View = view
    var itemsCount = level
    var point = 0
    private var items: ArrayList<Card> = arrayListOf()
    private var images: ArrayList<CardImage> = arrayListOf()
    var visibles = arrayListOf<Int>()
    private var isCLickable = true


    init {
        fillList()
    }

    override fun fillInitial() {
        var x = 0
        while (x < itemsCount) {
            val image = images[Random.nextInt(images.size)]
            if (countOf(Card(image)) < 2) {
                items.add(Card(image))
                items.add(Card(image))
                x+=2
            }
        }
        items.shuffle()
    }

    override fun beginGame() {
        fillInitial()
        mView.startGame()
        point = 0
    }





    override fun fillList() {
        images.add(CardImage(R.drawable.a,Color.parseColor("#FF177ED5")))
        images.add(CardImage(R.drawable.b,Color.parseColor("#FF177ED5")))
        images.add(CardImage(R.drawable.c,Color.parseColor("#FF177ED5")))
        images.add(CardImage(R.drawable.d,Color.parseColor("#FF177ED5")))
        images.add(CardImage(R.drawable.e,Color.parseColor("#FF177ED5")))
        images.add(CardImage(R.drawable.f,Color.parseColor("#FF177ED5")))
        images.add(CardImage(R.drawable.g,Color.parseColor("#FF177ED5")))
        images.add(CardImage(R.drawable.h,Color.parseColor("#FF177ED5")))
        images.add(CardImage(R.drawable.i,Color.parseColor("#FF177ED5")))
        images.add(CardImage(R.drawable.j,Color.parseColor("#FF177ED5")))
    }

    override fun getAdapter(): CardAdapter {
        var deckAdapter = CardAdapter(items, mView.getContext()) {
            if (isCLickable) {
                if (!items[it].isVisible) {
                    items[it].isVisible = true
                    visibles.add(it)
                    mView.refreshData(it)
                    if (visibles.size == 2) {
                        isCLickable = false
                        var handler = Handler()
                        handler.postDelayed({
                            if (items[visibles[0]].getImage() == items[visibles[1]].getImage()) {
                                items[visibles[0]].isMatched = true
                                items[visibles[1]].isMatched = true
                                point+=20
                                mView.updateScore(point)
                            } else {
                                items[visibles[0]].isVisible = false
                                items[visibles[1]].isVisible = false
                                point-=2
                                if(point<0){
                                    point=0
                                }
                                mView.updateScore(point)
                            }
                            mView.refreshData(visibles[0])
                            mView.refreshData(visibles[1])
                            visibles.clear()
                            isCLickable = true
                            checkGameEnd()
                        }, 250L)
                        //mView.endTimer()
                    }

                }
            }
        }
        return deckAdapter
    }



    fun checkGameEnd() {

        var i = 0



        for(k in 0 until items.size) {
            if(items[k].isMatched) i++
        }
        if(i == items.size) {
            mView.showEnding()
            items.clear()
        }
    }

    fun countOf(item: Card): Int {
        return Collections.frequency(items, item)
    }
}