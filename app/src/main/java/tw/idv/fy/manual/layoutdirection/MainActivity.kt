package tw.idv.fy.manual.layoutdirection

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Faty", "LAYOUT_DIRECTION_LTR     = $LAYOUT_DIRECTION_LTR")
        Log.d("Faty", "LAYOUT_DIRECTION_RTL     = $LAYOUT_DIRECTION_RTL")
        Log.d("Faty", "LAYOUT_DIRECTION_INHERIT = $LAYOUT_DIRECTION_INHERIT")
        Log.d("Faty", "LAYOUT_DIRECTION_LOCALE  = $LAYOUT_DIRECTION_LOCALE")

        val item0 = findViewById<FrameLayout>(R.id.view0)[0]
        val view1 = findViewById<ImageView>(R.id.view1)
        val view2 = findViewById<ImageView>(R.id.view2)

        item0.post {
            Log.v("Faty", "(InLaid) layoutDirection = ${item0.layoutDirection}")
            Log.v("Faty", "(InLaid) isLayoutDirectionResolved = ${item0.isLayoutDirectionResolved}")
            Log.v("Faty", "(InLaid) canResolveLayoutDirection = ${item0.canResolveLayoutDirection()}")
        }

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
            Log.i("Faty", "(before) layoutDirection = ${item2.layoutDirection}")
            Log.i("Faty", "(before) isLayoutDirectionResolved = ${item2.isLayoutDirectionResolved}")
            Log.i("Faty", "(before) canResolveLayoutDirection = ${item2.canResolveLayoutDirection()}")
            item2.layoutDirection = LAYOUT_DIRECTION_LOCALE
            Log.i("Faty", "(after)  layoutDirection = ${item2.layoutDirection}")
            Log.i("Faty", "(after)  isLayoutDirectionResolved = ${item2.isLayoutDirectionResolved}")
            Log.i("Faty", "(after)  canResolveLayoutDirection = ${item2.canResolveLayoutDirection()}")
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