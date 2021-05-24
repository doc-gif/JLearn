package com.example.jlearn

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.jlearn.databinding.ActivityResultBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import io.realm.Realm
import io.realm.kotlin.where

class Result : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var binding: ActivityResultBinding

    private var totalQue = 0f
    private var howManyCorrect = 0f
    private var howManyIncorrect = 0f
    private var correctRate = 0
    private var oneMoreFlag = false
    private var mPieChart: PieChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        realm = Realm.getDefaultInstance()

        val settings = realm.where<SettingsData>().findFirst()!!
        val totals = settings.totalQue
        val correct = settings.howManyCorrect
        val incorrect = settings.howManyIncorrect
//        val keyTangoEJ = settings.wordCJ
//        val keyTangoJE = settings.wordJC
//        val keyExampleEJ = settings.exCJ
//        val keyExampleJE = settings.exJC
//        val keyNumF = settings.numF
//        val keyNumE = settings.numE
//        val keySearchCorrect = settings.searchCorrect
//        val keySearch = settings.searchCheck
//        val keyHowManyQue = settings.howManyQue
//        val keyHowQue = settings.howQue

        totalQue = totals.toFloat()
        howManyCorrect = correct.toFloat()
        howManyIncorrect = incorrect.toFloat()
        correctRate = (howManyCorrect / totalQue * 100).toInt()

        binding.moreTrain.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("もう一度学習しますか？")
                .setNegativeButton("キャンセル") { _, _ -> }
                .setPositiveButton("学習する") { _, _ ->
                    /***
                     * oneMoreFlag
                     * true:もう一度学習する
                     */
                    oneMoreFlag = true
                    val intent = Intent(this, TestScreen::class.java)
                    startActivity(intent)
                    settings.oneMoreFlag = oneMoreFlag
                }
                .show()
        }

        binding.toHome.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("ホームに戻りますか？")
                .setNegativeButton("キャンセル") { _, _ -> }
                .setPositiveButton("ホームに戻る") { _, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .show()
        }
        setupPieChartView()
    }

    private fun setValue() {
        /**
         * データ
         */
        val value = listOf(howManyCorrect, howManyIncorrect)
        val label = listOf("正解", "不正解")
        val entry = ArrayList<PieEntry>()
        for(i in value.indices) {
            entry.add( PieEntry(value[i], label[i]) )
        }
        /**
         * ラベル
         */
        val dataSet = PieDataSet(entry, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val pieData = PieData(dataSet)
        pieData.setValueTextSize(0f)
        pieData.setValueTextColor(Color.WHITE)

        mPieChart?.data = pieData
    }

    private fun setupPieChartView() {
        //値を直接代入しないとすべての値が0.0として扱われる。
        //ex.mPieChart?.centerText = "$correctRate%" → 0.0%
        mPieChart = findViewById(R.id.pieChart)

        mPieChart?.setUsePercentValues(true)
        mPieChart?.centerText = "$correctRate%"
        mPieChart?.setCenterTextSize(30f)

        //グラフの右下に説明をいれる
        val desc = Description()
        desc.text = ""
        mPieChart?.description = desc

        //ラベルの位置の設定
        val legend: Legend? = mPieChart?.legend
        legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

        setValue()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}