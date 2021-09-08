package tw.idv.fy.manual.layoutdirection

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.view.View.LAYOUT_DIRECTION_LOCALE
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val item0 = findViewById<FrameLayout>(R.id.view0)[0]
        val view1 = findViewById<ImageView>(R.id.view1)
        val view2 = findViewById<ImageView>(R.id.view2)

        view1.post {

            val widthSpec = View.MeasureSpec.makeMeasureSpec(item0.width, View.MeasureSpec.EXACTLY)
            val heightSpec = View.MeasureSpec.makeMeasureSpec(item0.height, View.MeasureSpec.EXACTLY)

            val item1 = layoutInflater.inflate(R.layout.item_tv, null)
            item1.measure(widthSpec, heightSpec)
            item1.layout(item0.left, item0.top, item0.right, item0.bottom)

            val bitmap = Bitmap.createBitmap(
                view1.width, view1.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            canvas.translate(item1.left.toFloat(), item1.top.toFloat())
            item1.draw(canvas)

            view1.setImageBitmap(bitmap)
        }

        view2.post {

            val widthSpec = View.MeasureSpec.makeMeasureSpec(item0.width, View.MeasureSpec.EXACTLY)
            val heightSpec = View.MeasureSpec.makeMeasureSpec(item0.height, View.MeasureSpec.EXACTLY)

            val item2 = layoutInflater.inflate(R.layout.item_tv, null)
            item2.layoutDirection = LAYOUT_DIRECTION_LOCALE
            item2.measure(widthSpec, heightSpec)
            item2.layout(item0.left, item0.top, item0.right, item0.bottom)

            val bitmap = Bitmap.createBitmap(
                view1.width, view1.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            canvas.translate(item2.left.toFloat(), item2.top.toFloat())
            item2.draw(canvas)

            view2.setImageBitmap(bitmap)
        }
    }
}