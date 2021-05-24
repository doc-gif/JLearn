package com.example.jlearn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.jlearn.databinding.ActivityTestAncientBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.apache.commons.csv.CSVFormat
import java.io.BufferedReader
import java.io.InputStreamReader

class TestAncient : AppCompatActivity() {
    private lateinit var binding: ActivityTestAncientBinding
    private lateinit var realm: Realm

    private var wordCJ = false
    private var wordJC = false
    private var exCJ = false
    private var exJC = false
    private var kindOfQueWordCJ = 0
    private var kindOfQueWordJC = 0
    private var kindOfQueExCJ = 0
    private var kindOfQueExJC = 0
    private var kindQuestion = 0
    private var searchCorrect = 0
    private var searchCheck = 0
    private var howManyQue = 0
    private var howQue = 0
    private var numF = 0
    private var numE = 0
    private var cause1 = 0
    private var cause2 = 0
    private var cause3 = 0
    private var cause4 = 0
    private var cause5 = 0
    private var cause6 = 0
    private var totalError = 0
    private var indexMeaning = 0
    private var wordsNumberList: MutableList<Double> = mutableListOf()
    private var answerNumberList: MutableList<Double> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestAncientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        realm = Realm.getDefaultInstance()

        createData()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val checkState = sharedPreferences.getBoolean("correct", false)
        indexMeaning = if (!checkState) 1 else 4

        initialIndex(binding.correctGroup, binding.noneCorrect.id)
        initialIndex(binding.checkGroup, binding.noneCheck.id)
        initialIndex(binding.countGroup, binding.all.id)
        initialIndex(binding.orderGroup, binding.order.id)

        binding.start.setOnClickListener { onStartButtonTapped() }

    }

    private fun createData() {
        val reader = BufferedReader(InputStreamReader(resources.assets.open("sample.csv")))
        reader.use {
            val records = CSVFormat.EXCEL.parse(reader)
            realm.beginTransaction()
            val target = realm.where<WordsData>().findAll()
            target.deleteAllFromRealm()
            records.records.forEach { record ->
                val obj = realm.createObject<WordsData>()
                obj.number = record[0].toInt()
            }
            realm.commitTransaction()
        }
    }

    private fun initialIndex(radioGroup: RadioGroup, id: Int) = radioGroup.check(id)

    private fun checkKind() {
        wordCJ = binding.wordCJ.isChecked
        wordJC = binding.wordJC.isChecked
        exCJ = binding.exCJ.isChecked
        exJC = binding.exJC.isChecked
        kindOfQueWordCJ = if (wordCJ) 1 else 0
        kindOfQueWordJC = if (wordJC) 10 else 0
        kindOfQueExCJ = if (exCJ) 100 else 0
        kindOfQueExJC = if (exJC) 1000 else 0
    }

    private fun indexOfCorrect() {
        when (binding.correctGroup.checkedRadioButtonId) {
            binding.correct.id -> searchCorrect = 1
            binding.incorrect.id -> searchCorrect = 2
            binding.unanswered.id -> searchCorrect = 3
            binding.noneCorrect.id -> searchCorrect = 4
        }
    }

    private fun indexOfCheck() {
        when (binding.checkGroup.checkedRadioButtonId) {
            binding.check.id -> searchCheck = 1
            binding.unchecked.id -> searchCheck = 2
            binding.noneCheck.id -> searchCheck = 3
        }
    }

    private fun indexOfCount() {
        when (binding.countGroup.checkedRadioButtonId) {
            binding.ten.id -> howManyQue = 1
            binding.twenty.id -> howManyQue = 2
            binding.thirty.id -> howManyQue = 3
            binding.forty.id -> howManyQue = 4
            binding.fifty.id -> howManyQue = 5
            binding.all.id -> howManyQue = 6
        }
    }

    private fun indexOfOrder() {
        when (binding.orderGroup.checkedRadioButtonId) {
            binding.order.id -> howQue = 1
            binding.random.id -> howQue = 2
        }
    }

    private fun onDestroyList(list: MutableList<Double>) {
        for (i in 0 until list.size) list.removeAt(0)
    }

    private fun selectCorrect() {
        when (kindQuestion) {
            1 -> onLoopCorrect(1,0)
            10 -> onLoopCorrect(10,1935)
            11 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(10,1935)
            }
            100 -> onLoopCorrect(100,1935 * 2)
            101 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(100,1935 * 2)
            }
            110 -> {
                onLoopCorrect(10,1935)
                onLoopCorrect(100,1935 * 2)
            }
            111 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(10,1935)
                onLoopCorrect(100,1935 * 2)
            }
            1000 -> onLoopCorrect(1000,1935 * 3)
            1001 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(1000,1935 * 3)
            }
            1010 -> {
                onLoopCorrect(10,1935)
                onLoopCorrect(1000,1935 * 3)
            }
            1011 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(10,1935)
                onLoopCorrect(1000,1935 * 3)
            }
            1100 -> {
                onLoopCorrect(100,1935 * 2)
                onLoopCorrect(1000,1935 * 3)
            }
            1101 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(100,1935 * 2)
                onLoopCorrect(1000,1935 * 3)
            }
            1110 -> {
                onLoopCorrect(10,1935)
                onLoopCorrect(100,1935 * 2)
                onLoopCorrect(1000,1935 * 3)
            }
            1111 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(10,1935)
                onLoopCorrect(100,1935 * 2)
                onLoopCorrect(1000,1935 * 3)
            }
        }
    }

    private fun onLoopCorrect(x: Int, y: Int) {
        for (j in 1..indexMeaning) {
            when (searchCorrect) {
                1 -> { //正解
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        val realmResult = realm.where<Correct>()
                                .equalTo("number", i- y)
                                .equalTo("kindQuestion", x)
                                .equalTo("indexMeaning", j)
                                .greaterThan("howManyCorrect", 1)
                                .findFirst()
                        if (realmResult != null) wordsNumberList.add(i.toDouble() + (j.toDouble()/10))
                    }
                }
                2 -> { //不正解
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        val realmResult = realm.where<Correct>()
                                .equalTo("number", i - y)
                                .equalTo("kindQuestion", x)
                                .equalTo("indexMeaning", j)
                                .lessThan("howManyCorrect", 2)
                                .findFirst()
                        if (realmResult != null) wordsNumberList.add(i.toDouble() + (j.toDouble()/10))
                    }
                }
                3 -> { //未正解
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        val realmResult = realm.where<Correct>()
                                .equalTo("number", i - y)
                                .findFirst()
                        if (realmResult == null) wordsNumberList.add(i.toDouble() + (j.toDouble()/10))
                    }
                }
                //指定なし
                4 -> {
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        wordsNumberList.add(i.toDouble() + (j.toDouble()/10))
                    }
                }
            }
        }
    }

    private fun selectCheck() {
        when (kindQuestion) {
            1 -> onLoopCheck(1,0)
            10 -> onLoopCheck(10,1935)
            11 -> {
                onLoopCheck(1,0)
                onLoopCheck(10,1935)
            }
            100 -> onLoopCheck(100,1935 * 2)
            101 -> {
                onLoopCheck(1,0)
                onLoopCheck(100,1935 * 2)
            }
            110 -> {
                onLoopCheck(10,1935)
                onLoopCheck(100,1935 * 2)
            }
            111 -> {
                onLoopCheck(1,0)
                onLoopCheck(10,1935)
                onLoopCheck(100,1935 * 2)
            }
            1000 -> onLoopCheck(1000,1935 * 3)
            1001 -> {
                onLoopCheck(1,0)
                onLoopCheck(1000,1935 * 3)
            }
            1010 -> {
                onLoopCheck(10,1935)
                onLoopCheck(1000,1935 * 3)
            }
            1011 -> {
                onLoopCheck(1,0)
                onLoopCheck(10,1935)
                onLoopCheck(1000,1935 * 3)
            }
            1100 -> {
                onLoopCheck(100,1935 * 2)
                onLoopCheck(1000,1935 * 3)
            }
            1101 -> {
                onLoopCheck(1,0)
                onLoopCheck(100,1935 * 2)
                onLoopCheck(1000,1935 * 3)
            }
            1110 -> {
                onLoopCheck(10,1935)
                onLoopCheck(100,1935 * 2)
                onLoopCheck(1000,1935 * 3)
            }
            1111 -> {
                onLoopCheck(1,0)
                onLoopCheck(10,1935)
                onLoopCheck(100,1935 * 2)
                onLoopCheck(1000,1935 * 3)
            }
        }
    }

    private fun onLoopCheck(x: Int, y: Int) {
        for (j in 1..indexMeaning) {
            when (searchCheck) {
                1 -> { //チェックあり
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        val realmResult = realm.where<CheckList>()
                                .equalTo("number", i - y)
                                .equalTo("kindQuestion", x)
                                .equalTo("indexMeaning", j)
                                .findFirst()
                        if (realmResult == null) wordsNumberList.remove(i.toDouble() + (j.toDouble()/10))
                    }
                }
                2 -> { //チェックなし
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        val realmResult = realm.where<CheckList>()
                                .equalTo("number", i - y)
                                .equalTo("kindQuestion", x)
                                .equalTo("indexMeaning", j)
                                .findFirst()
                        if (realmResult != null) wordsNumberList.remove(i.toDouble() + (j.toDouble()/10))
                    }
                }
            }
        }
    }

    private fun createAnswerList() {
        when (kindQuestion) {
            1 -> selectAnswerNumber(0)
            10 -> selectAnswerNumber(1935)
            11 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935)
            }
            100 -> selectAnswerNumber(1935 * 2)
            101 -> {
                selectAnswerNumber( 0)
                selectAnswerNumber(1935 * 2)
            }
            110 -> {
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 2)
            }
            111 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 2)
            }
            1000 -> selectAnswerNumber(1935 * 3)
            1001 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935 * 3)
            }
            1010 -> {
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 3)
            }
            1011 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 3)
            }
            1100 -> {
                selectAnswerNumber(1935 * 2)
                selectAnswerNumber(1935 * 3)
            }
            1101 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935 * 2)
                selectAnswerNumber(1935 * 3)
            }
            1110 -> {
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 2)
                selectAnswerNumber(1935 * 3)
            }
            1111 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 2)
                selectAnswerNumber(1935 * 3)
            }
        }
    }

    private fun selectAnswerNumber(x: Int) {
        here@
        for (j in 1..indexMeaning) {
            here@
            for (i in (numF + x)..(numE + x)) {
                when (j) {
                    2 -> if (realm.where<WordsData>().equalTo("number", i - x).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                    3 -> if (realm.where<WordsData>().equalTo("number", i - x).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                    4 -> if (realm.where<WordsData>().equalTo("number", i - x).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                }
                answerNumberList.add(i.toDouble() + (j.toDouble()/10))
            }
        }
    }

    private fun onStartButtonTapped() {
        val intent = Intent(this, TestScreen::class.java)
        onDestroyList(wordsNumberList)
        onDestroyList(answerNumberList)

        indexOfCorrect()
        indexOfCheck()
        indexOfCount()
        indexOfOrder()
        checkKind()

        cause1 = if (!wordCJ && !wordJC && !exCJ && !exJC) {
            Toast.makeText(applicationContext, "【出題範囲】を指定してください。", Toast.LENGTH_SHORT).show()
            1
        } else 0

        cause2 = if (binding.numF.text.isEmpty() || binding.numE.text.isEmpty()) {
            Toast.makeText(applicationContext,"【出題範囲(番号指定)】が未入力です。", Toast.LENGTH_LONG).show()
            1
        } else {
            numF = binding.numF.text.toString().toInt()
            numE = binding.numE.text.toString().toInt()
            0
        }

        cause3 = if (numE - numF + 1 <= 3 && cause2 == 0) {
            Toast.makeText(applicationContext, "【出題範囲】は4問以上にしてください。", Toast.LENGTH_LONG).show()
            1
        } else 0

        cause4 = if (numF <= 0 || numE >= 1936) {
            Toast.makeText(applicationContext,"【出題範囲】は1~1935の間にしてください。", Toast.LENGTH_LONG).show()
            1
        } else 0

        totalError = cause1 + cause2 + cause3 + cause4

        if (totalError > 0) return

        kindQuestion = kindOfQueWordCJ + kindOfQueWordJC + kindOfQueExCJ + kindOfQueExJC
        selectCorrect()
        cause5 = if (wordsNumberList.size == 0) {
            Toast.makeText(applicationContext,"【正解・不正解】で指定された範囲に問題がありません。", Toast.LENGTH_LONG).show()
            1
        } else 0

        selectCheck()
        cause6 = if (wordsNumberList.size == 0) {
            Toast.makeText(applicationContext,"【チェック有無】で指定された範囲に問題がありません。", Toast.LENGTH_LONG).show()
            1
        } else 0

        totalError += cause5 + cause6

        createAnswerList()

        realm.executeTransaction {
            val target = realm.where<SettingsData>().findAll()
            target.deleteAllFromRealm()
            val obj = realm.createObject<SettingsData>()
            obj.wordCJ = wordCJ
            obj.wordJC = wordJC
            obj.exCJ = exCJ
            obj.exJC = exJC
            obj.numF = numF
            obj.numE = numE
            obj.searchCorrect = searchCorrect
            obj.searchCheck = searchCheck
            obj.howManyQue = howManyQue
            obj.howQue = howQue
            obj.oneMoreFlag = false
        }

        realm.executeTransaction {
            val target = realm.where<WordsNumberList>().findAll()
            target.deleteAllFromRealm()
            for (i in 0 until wordsNumberList.size) {
                val realmObject = realm.createObject<WordsNumberList>()
                realmObject.number = wordsNumberList[i]
            }
        }

        realm.executeTransaction {
            val target = realm.where<AnswerNumberList>().findAll()
            target.deleteAllFromRealm()
            for (i in 0 until answerNumberList.size) {
                val realmObject = realm.createObject<AnswerNumberList>()
                realmObject.number = answerNumberList[i]
            }
        }
        if (totalError == 0) startActivity(intent)
    }
}