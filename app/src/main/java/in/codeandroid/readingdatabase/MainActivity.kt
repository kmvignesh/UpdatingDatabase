package `in`.codeandroid.readingdatabase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var numberList: MutableList<Number> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dbHandler = DBHandler(this)
        dbHandler.getWritableDb()
        rv_number.layoutManager = LinearLayoutManager(this)
        numberList = dbHandler.getAllData()
        rv_number.adapter = NumberAdapter(this)

    }

    class NumberAdapter(val activity: MainActivity) : RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NumberViewHolder {
            return NumberViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_number, p0, false))
        }

        override fun getItemCount(): Int {
            return activity.numberList.size
        }

        override fun onBindViewHolder(p0: NumberViewHolder, p1: Int) {
            p0.tvNumber.text = "Item ${activity.numberList[p1].number}"

            if (activity.numberList[p1].isOddNumber) {
                p0.tvStatus.text = "Odd Number"
            } else {
                p0.tvStatus.text = "Even Number"
            }
        }

        class NumberViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val tvNumber = v.findViewById<TextView>(R.id.tv_number)
            val tvStatus = v.findViewById<TextView>(R.id.tv_status)
        }
    }

}
