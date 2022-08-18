package android.example.jsonrecyclerview2

import android.app.Activity
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val doctor = intent.getParcelableExtra<Data>(MainActivity.INTENT_PARCELABLE)

        val name = findViewById<TextView>(R.id.detailTextView)
        val img = findViewById<ImageView>(R.id.detailImageView)
        val statu = findViewById<TextView>(R.id.statu)
        val info = findViewById<TextView>(R.id.info)
        val next = findViewById<ImageButton>(R.id.nextButton)

        if (doctor != null) {
            name.text = doctor.name
            img.let {
                Picasso.get().load(doctor.imgurl).into(img)
            }
            if (doctor.statu == "premium"){
                statu.text = doctor.statu
                info.text = "Randevu al"
                next.setOnClickListener {
                    val intent = Intent(this,RandevuActivity::class.java)
                    startActivity(intent)
                }
            }else{
                info.text ="Premium paket Al"
                next.setOnClickListener {
                    val intent = Intent(this,PaymentActivity::class.java)
                    startActivity(intent)
                }
            }


        }




    }
}
