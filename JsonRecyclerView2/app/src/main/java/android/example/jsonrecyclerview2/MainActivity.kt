package android.example.jsonrecyclerview2

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {


    private val URLstring = "https://www.mobillium.com/android/doctors.json"
    internal lateinit var doctorsArrayList: ArrayList<Data>
    private var rvAdapter: RvAdapter? = null
    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.my_recycler_view)
        val searchView = findViewById<SearchView>(R.id.doctor_search)




        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                rvAdapter?.filter?.filter(newText)


                return false
            }
        })





        fetchingJSON()

    }


    fun onCheckboxClicked(view: View) {
        val female = findViewById<CheckBox>(R.id.femaleCheck)
        val male = findViewById<CheckBox>(R.id.maleCheck)

        when (view.id) {
            R.id.maleCheck -> female.isChecked = false
            R.id.femaleCheck -> male.isSelected = false
        }
        when (view.id) {

            R.id.femaleCheck -> male.isSelected = false
            R.id.maleCheck -> female.isChecked = false
        }

        rvAdapter?.onCheckboxClicked(view)

    }


    private fun fetchingJSON() {
           //showSimpleProgressDialog(this, "Yükleniyor...", "", true)

        val stringRequest = StringRequest(Request.Method.GET, URLstring,
            Response.Listener { response ->
                Log.d("strrrrr", ">>$response")

                try {

                    //   removeSimpleProgressDialog()
                    val obj = JSONObject(response)

                    doctorsArrayList = ArrayList()
                    val dataArray = obj.getJSONArray("doctors")

                    for (i in 0 until dataArray.length()) {

                        val jobj2 = dataArray.getJSONObject(i)
                        val name = jobj2.getString("full_name")
                        val imgurl = jobj2.getString("image")
                        val jobj3 = JSONObject(imgurl)
                        val getImage = jobj3.getString("url")
                        val statu = jobj2.getString("user_status")
                        val gender = jobj2.getString("gender")

                        val m = Data(name, statu, gender, getImage)
                        doctorsArrayList.add(m)

                    }

                    setupRecycler()


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                //displaying the error in toast if occurrs
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            })

        // request queue
        val requestQueue = Volley.newRequestQueue(this)

        requestQueue.add(stringRequest)


    }


    private fun setupRecycler() {

        rvAdapter = RvAdapter(this, doctorsArrayList) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(INTENT_PARCELABLE, it)
            startActivity(intent)
        }

        recyclerView!!.adapter = rvAdapter
        recyclerView!!.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

    }

    companion object {
        val INTENT_PARCELABLE = "OBJECT_INTENT"


        private var mProgressDialog: ProgressDialog? = null

        fun removeSimpleProgressDialog() {
            try {
                if (mProgressDialog != null) {
                    if (mProgressDialog!!.isShowing) {
                        mProgressDialog!!.dismiss()
                        mProgressDialog = null
                    }
                }
            } catch (ie: IllegalArgumentException) {
                ie.printStackTrace()

            } catch (re: RuntimeException) {
                re.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun showSimpleProgressDialog(
            context: Context, title: String,
            msg: String, isCancelable: Boolean,
        ) {
            try {
                if (mProgressDialog == null) {
                    mProgressDialog = ProgressDialog.show(context, title, msg)
                    mProgressDialog!!.setCancelable(isCancelable)
                }

                if (!mProgressDialog!!.isShowing) {
                    mProgressDialog!!.show()
                }

            } catch (ie: IllegalArgumentException) {
                ie.printStackTrace()
            } catch (re: RuntimeException) {
                re.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


}
