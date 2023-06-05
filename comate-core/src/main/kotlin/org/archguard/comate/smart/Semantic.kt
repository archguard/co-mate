package org.archguard.comate.smart

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.OrtUtil
import java.net.URI
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import kotlin.io.path.toPath


class Semantic(val tokenizer: HuggingFaceTokenizer, val session: OrtSession, val env: OrtEnvironment) {
    fun embed(
        sequence: String,
    ): FloatArray {
        val tokenized = tokenizer.encode(sequence, true)

        val inputIds = tokenized.ids
        val attentionMask = tokenized.attentionMask
        val typeIds = tokenized.typeIds

        val tensorInput = OrtUtil.reshape(inputIds, longArrayOf(1, inputIds.size.toLong()))
        val tensorAttentionMask = OrtUtil.reshape(attentionMask, longArrayOf(1, attentionMask.size.toLong()))
        val tensorTypeIds = OrtUtil.reshape(typeIds, longArrayOf(1, typeIds.size.toLong()))

        val result = session.run(
            mapOf(
                "input_ids" to OnnxTensor.createTensor(env, tensorInput),
                "attention_mask" to OnnxTensor.createTensor(env, tensorAttentionMask),
                "token_type_ids" to OnnxTensor.createTensor(env, tensorTypeIds),
            ),
        )

        val outputTensor = result.get(0) as OnnxTensor
        val output = outputTensor.floatBuffer.array()

        return output
    }


    companion object {
        fun create(): Semantic {
            val tokenizerPath = Companion::class.java.classLoader.getResource("model/tokenizer.json")!!.toURI()
            val onnxPath =  Companion::class.java.classLoader.getResource("model/model.onnx")!!.toURI()

            try {
                val env: Map<String, String> = HashMap()
                val array: List<String> = tokenizerPath.toString().split("!")
                val fs: FileSystem = FileSystems.newFileSystem(URI.create(array[0]), env)
            } catch (e: Exception) {
//                e.printStackTrace()
            }

            val tokenizer = HuggingFaceTokenizer.newInstance(tokenizerPath.toPath())
            val ortEnv = OrtEnvironment.getEnvironment()
            val sessionOptions = OrtSession.SessionOptions()
            val session = ortEnv.createSession(onnxPath.path, sessionOptions)

            return Semantic(tokenizer, session, ortEnv)
        }
    }
}