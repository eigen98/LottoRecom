package com.example.lottorecom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
//로또 번호 추첨기
//ContraintLayout사용
//NumberPicker 사용
// TextView
//Button

//중복코드 정리
//shape Drawable 사용

//apply -> 주로 초기화할 때 사용
//when
//Random
//Collection
//람다 함수

class MainActivity : AppCompatActivity() {

    //lazy가 안 되어서 file -> invalidate cache/ restart 누름 됨.
    private val clearButton: Button by lazy{
        findViewById<Button>(R.id.clearButton)
    }

    private val addButton: Button by lazy{
        findViewById<Button>(R.id.addButton)
    }

    private val runButton: Button by lazy{
        findViewById<Button>(R.id.runButton)
    }

    private val numberPicker : NumberPicker by lazy{
        findViewById<NumberPicker>(R.id.numberPicker)
    }

    private val numberTextViewList : List<TextView> by lazy{
        listOf<TextView>(
                findViewById<TextView>(R.id.textView1),
                findViewById<TextView>(R.id.textView2),
                findViewById<TextView>(R.id.textView3),
                findViewById<TextView>(R.id.textView4),
                findViewById<TextView>(R.id.textView5),
                findViewById<TextView>(R.id.textView6)

        )
    }

    private var didRun = false

    private  val pickNumberSet = mutableSetOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        numberPicker.minValue = 1

        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()

    }
    private fun initRunButton(){
        runButton.setOnClickListener {
            val list = getRandomNumber()

            didRun = true

            list.forEachIndexed{index,number ->
                val textView = numberTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true

                setNumberBackground(number, textView)

            }

            Log.d("MainActivity", list.toString())
        }
    }

    private fun initAddButton(){
        addButton.setOnClickListener {

            if(didRun){
                Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener //리턴을할 때 안에있는 fun만 리턴
            }

            if(pickNumberSet.size >= 5){
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(pickNumberSet.contains(numberPicker.value)){
                Toast.makeText(this, "이미 선택한 번호입니다..",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            setNumberBackground(numberPicker.value, textView)



            pickNumberSet.add(numberPicker.value)
        }
    }


    private fun setNumberBackground(number: Int, textView: TextView){
        when(number){//drawable앱에서 가져오는 것이기때문에 Context이용
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yello)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            else ->  textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
        }
    }

    private fun initClearButton(){
        clearButton.setOnClickListener {

            numberTextViewList.forEach{
                it.isVisible = false
            }
            pickNumberSet.clear()
            didRun = false
        }
    }


    private fun getRandomNumber() : List<Int>{
        val numberList = mutableListOf<Int>()
                .apply{
                    for(i in 1..45){
                        if(pickNumberSet.contains(i)){
                            continue
                        }
                        this.add(i)
                    }
        }

        numberList.shuffle() //섞기
        val newList = numberList.toList() + numberList.subList(0, 6 - pickNumberSet.size)//추출하는 함수 subList()
        return newList.sorted()
    }
}