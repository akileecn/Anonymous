package cn.aki.anonymous.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import cn.aki.anonymous.R
import cn.aki.anonymous.base.C
import cn.aki.library.utils.ImageUtils
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {
    // 单击打开监听器
    class OpenOnClickListener(val context: Context) : View.OnClickListener {
        override fun onClick(v: View?) {
            if (v?.tag != null) {
                val intent = Intent(context, ImageActivity::class.java)
                intent.putExtra(C.Extra.IMAGE_URL, v.tag as String)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        // 图片
        val imageUrl = intent.getStringExtra(C.Extra.IMAGE_URL)
        ImageUtils.bind(imageUrl, image)
    }

}
