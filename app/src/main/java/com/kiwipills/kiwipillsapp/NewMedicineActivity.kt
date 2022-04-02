package com.kiwipills.kiwipillsapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewMedicineActivity : AppCompatActivity(), OnNavBarListeners {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_medicine)
    }

    override fun onClickTabFragment(intData: Int, strId:String) {
        when(strId){
            "home_tab"->{
                Toast.makeText(this,"click en home" + intData.toString(), Toast.LENGTH_LONG).show()
            }
            "list_tab"->{

            }
            "calendar_tab"->{

            }
            "today_tab"->{

            }
        }
    }
}