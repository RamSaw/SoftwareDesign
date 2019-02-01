package ru.hse.spb.pipeline

/**
 * Class that simulates pipeline. Pipeline is quite similar to ordinary file.
 */
class Pipeline(private val content: String) {
    /**
     * Returns file name for pipe.
     * By default it is empty.
     */
    fun getFileName(): String = ""

    /**
     * Returns content of pipe.
     */
    fun getContent(): String = content
}