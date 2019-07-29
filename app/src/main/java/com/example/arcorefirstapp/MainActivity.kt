package com.example.arcorefirstapp

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.app.ActivityManager
import android.os.Build
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import android.view.Gravity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ux.TransformableNode


class MainActivity : AppCompatActivity() {

    val MIN_OPENGL_VERSION = 3.0

    var arFragment: ArFragment? = null
    var lampPostRenderable:ModelRenderable? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return
        }

        setContentView(R.layout.activity_main)

        arFragment = getSupportFragmentManager().findFragmentById(R.id.ux_fragment) as ArFragment

        ModelRenderable.builder().setSource(this, Uri.parse("porsche-cayman.sfb")).build()
            .thenAccept({ renderable -> lampPostRenderable = renderable }).exceptionally({ throwable ->
                val toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            })

        arFragment!!.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            if (lampPostRenderable == null){
                return@setOnTapArPlaneListener
            }
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment!!.getArSceneView().scene)

            val lamp = TransformableNode(arFragment!!.getTransformationSystem())
            lamp.setParent(anchorNode)
            lamp.setRenderable(lampPostRenderable)
            lamp.select()
        }


    }
    fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e("MYTAG", "Sceneform requires Android N or later")
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show()
            activity.finish()
            return false
        }
        val openGlVersionString =
            (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).deviceConfigurationInfo.glEsVersion
        if (java.lang.Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e("MYTAG", "Sceneform requires OpenGL ES 3.0 later")
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG).show()
            activity.finish()
            return false
        }
        return true
    }
}
