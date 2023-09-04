import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.b.R;

import org.tensorflow.lite.Delegate;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.gpu.GpuDelegate;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class tf extends AppCompatActivity {

    private Interpreter tflite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Load the TensorFlow Lite model from the assets folder
        try {
            tflite = new Interpreter((ByteBuffer) loadModelFile(), (Interpreter.Options) getGpuDelegate());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print input/output tensor shapes
        int numInputs = tflite.getInputTensorCount();
        int numOutputs = tflite.getOutputTensorCount();
        Log.d("TFLite", "Number of inputs: " + numInputs);
        Log.d("TFLite", "Number of outputs: " + numOutputs);
        for (int i = 0; i < numInputs; i++) {
            Log.d("TFLite", "Input shape: " + Arrays.toString(tflite.getInputTensor(i).shape()));
        }
        for (int i = 0; i < numOutputs; i++) {
            Log.d("TFLite", "Output shape: " + Arrays.toString(tflite.getOutputTensor(i).shape()));
        }
        if (tflite != null) {
            numInputs = tflite.getInputTensorCount();
            numOutputs = tflite.getOutputTensorCount();
            Log.d("TFLite", "Number of inputs: " + numInputs);
            Log.d("TFLite", "Number of outputs: " + numOutputs);
            for (int i = 0; i < numInputs; i++) {
                Log.d("TFLite", "Input shape: " + Arrays.toString(tflite.getInputTensor(i).shape()));
            }
            for (int i = 0; i < numOutputs; i++) {
                Log.d("TFLite", "Output shape: " + Arrays.toString(tflite.getOutputTensor(i).shape()));
            }
        } else {
            Log.e("TFLite", "Interpreter object is null");
        }
    }


    // Load the TensorFlow Lite model file
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Initialize a GPU delegate
    private Delegate getGpuDelegate() {
        CompatibilityList compatList = new CompatibilityList();

        if (compatList.isDelegateSupportedOnThisDevice()) {
            // if the device has a supported GPU, add the GPU delegate
            return new GpuDelegate();
        }
        // if the GPU is not supported, return null so that the interpreter falls back to CPU
        return null;
    }



}
